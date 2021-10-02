package ru.praktika95.bot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class App {

    public static void main(String[] args) throws TelegramApiException {
        Bot bot = new Bot("EventsEkbBot", "2025588970:AAEk-y-mD2JSubRf98SjY0sxDU324mtj7jo");
        bot.botConnect();
    }
}