package ru.praktika95.bot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class App {

    public static void main(String[] args) throws TelegramApiException {
        Bot bot = new Bot("AlisaRushBot", "2042186727:AAHrOhFORwT88bSuqZbqj6ly4CXoqoRt37g");
        bot.botConnect();
    }
}