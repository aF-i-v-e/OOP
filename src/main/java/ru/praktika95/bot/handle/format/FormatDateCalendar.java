package ru.praktika95.bot.handle.format;

import ru.praktika95.bot.handle.services.chService.CommandHandlerConstants;

import java.util.Calendar;

public class FormatDateCalendar {
    public static String formatDate(Calendar calendar) {
        String date = Integer.toString(calendar.get(Calendar.DATE));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        return date + CommandHandlerConstants.dot + month + CommandHandlerConstants.dot + year;
    }
}
