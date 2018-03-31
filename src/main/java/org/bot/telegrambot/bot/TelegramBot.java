package org.bot.telegrambot.bot;

import org.bot.telegrambot.Config;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;


public class TelegramBot extends TelegramLongPollingBot {

    private final TelegramBotService telegramBotService;
    private final Config config;

    public TelegramBot(TelegramBotService telegramBotService, Config config) {
        this.telegramBotService = telegramBotService;
        this.config = config;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            telegramBotService.handleUpdate(update);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

}
