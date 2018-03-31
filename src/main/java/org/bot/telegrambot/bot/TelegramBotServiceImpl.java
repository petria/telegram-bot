package org.bot.telegrambot.bot;

import org.bot.telegrambot.Config;
import org.bot.telegrambot.bot.commands.CommandMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Service
public class TelegramBotServiceImpl implements CommandLineRunner, TelegramBotService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Config config;

    private TelegramBot telegramBot;


    @Override
    public void handleUpdate(Update update) {
        try {
            sendToCommandHandlers(update);
        } catch (InvocationTargetException | IllegalAccessException | TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendToCommandHandlers(Update update) throws InvocationTargetException, IllegalAccessException, TelegramApiException {
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String beanName : names) {
            Object obj = applicationContext.getBean(beanName);
            Class<?> objClz = obj.getClass();
            if (org.springframework.aop.support.AopUtils.isAopProxy(obj)) {
                objClz = org.springframework.aop.support.AopUtils.getTargetClass(obj);
            }
            for (Method m : objClz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(CommandMatcher.class)) {
                    Annotation annotation = m.getAnnotation(CommandMatcher.class);
                    CommandMatcher commandMatcher = (CommandMatcher) annotation;
                    if (update.getMessage().getText().matches(commandMatcher.MessageMatcher())) {
                        String response = (String) m.invoke(obj, update);
                        sendReplyMessage(update, response);
                    }
                }
            }
        }
    }

    private void sendReplyMessage(Update update, String responseMessage) throws TelegramApiException {
        SendMessage sendMessage= new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(update.getMessage().getChatId())
                .setText(responseMessage);
        telegramBot.sendMessage(sendMessage);
    }

    @Override
    public void run(String... args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            telegramBot = new TelegramBot(this, config);
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
