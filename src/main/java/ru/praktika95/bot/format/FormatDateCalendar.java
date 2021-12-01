package ru.praktika95.bot.format;

import java.util.Calendar;

public class FormatDateCalendar {
    public static String formatDate(Calendar calendar) {
        String date = Integer.toString(calendar.get(Calendar.DATE));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        return date + '.' + month + '.' + year;
    }
}
