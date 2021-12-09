package ru.praktika95.bot.handle.services.chUtils;

public class RandomService {
    public static int getRandomIntegerBetweenRange(int min, int max) {
        return (int) (Math.random() * ((max - min) + 1)) + min;
    }
}
