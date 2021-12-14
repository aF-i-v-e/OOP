package ru.praktika95.bot.handle.services.timeService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeService {
    public static String getCurrentTimePatternWithDot() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TimeConstants.timePatternWithDot);
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);
        return getTimeInDBFormat(date);
    }

    public static String getCurrentTimePatternWithDash() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TimeConstants.timePatternWithDash);
        LocalDate localDate = LocalDate.now();
        String date = dtf.format(localDate);
        return getTimeInDBFormat(date);
    }

    public static String getTimeWithDeltaInDays(int days) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TimeConstants.timePatternWithDash);
        LocalDate localDate = LocalDate.now().plusDays(days);
        String date = dtf.format(localDate);
        return getTimeInDBFormat(date);
    }

    public static String getTimeInDBFormat(String date) {
        String[] dateToArray;
        String separator = "-";
        if (date.charAt(2) == '.') {
            dateToArray = date.split("\\.");
            separator = ".";
        }
        else
            dateToArray = date.split(separator);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < dateToArray.length; i++) {
            sb.append(Integer.parseInt(dateToArray[i]));
            if (i != dateToArray.length-1)
                sb.append(separator);
        }
        return sb.toString();
    }
}
