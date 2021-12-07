package ru.praktika95.bot.handle.services.timeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeService {
    public static String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TimeConstants.timePatternWithDot);
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);
        return getTimeInDBFormat(date);
    }

    public static String getTimeInDBFormat(String date) {
        if (date.charAt(0) == '0')
            return date.substring(1);
        return date;
    }
}
