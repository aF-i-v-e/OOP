package ru.praktika95.bot;

public interface Handler {
    void handle(BotResponse response, String typeButtons);
}
