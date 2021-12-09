package ru.praktika95.bot.handle.services.chUtils;

public class StringFormatService {
    public static String getStringFormatPatternWithTwoArguments(String pattern, String arg1, String arg2) {
        return String.format(pattern, arg1,arg2);
    }
}
