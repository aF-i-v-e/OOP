package ru.praktika95.bot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class App {

    public static void main(String[] args) throws TelegramApiException {
        Bot bot = new Bot("EventsEkbBot", "");
        bot.botConnect();
    }
}