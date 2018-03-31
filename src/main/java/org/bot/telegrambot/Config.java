package org.bot.telegrambot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:bot.properties")
public class Config {

    @Value("${telegram.bot_name}")
    private String telegramBotName;

    @Value("${telegram.bot_token}")
    private String telegramBotToken;

    public String getBotName() {
        if (telegramBotName != null) {
            return telegramBotName;
        }
        return "property telegram.bot_name missing !";
    }

    public String getBotToken() {
        if (telegramBotToken != null) {
            return telegramBotToken;
        }
        return "property telegram.bot_token missing !";
//        return "412991638:AAGHKCGzfpGrKTLGOSdy81oaknxE-Qw2a20";
    }
}
