package org.bot.telegrambot.bot.commands;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;

@Service
public class TestCommandHandler implements CommandHandler {

    @CommandMatcher(MessageMatcher = "!test.*")
    public String handleTestCommand(Update update) {
        User from = update.getMessage().getFrom();
        return "Testing: " + from.toString();
    }

}
