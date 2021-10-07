package ru.praktika95.bot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class App {

    public static void main(String[] args) throws TelegramApiException {
        String name = "";
        String token = "";
        Bot bot = new Bot("", "");
        bot.botConnect();
    }
}