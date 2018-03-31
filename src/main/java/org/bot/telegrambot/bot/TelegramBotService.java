package org.bot.telegrambot.bot;

import org.telegram.telegrambots.api.objects.Update;

public interface TelegramBotService {

    void handleUpdate(Update update);

}
