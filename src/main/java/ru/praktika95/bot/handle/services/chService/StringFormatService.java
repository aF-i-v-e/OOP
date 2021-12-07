package ru.praktika95.bot.handle.services.chService;

public class StringFormatService {
    public static String getString(String pattern, String arg1, String arg2) {
        return String.format(pattern, arg1,arg2);
    }
}
