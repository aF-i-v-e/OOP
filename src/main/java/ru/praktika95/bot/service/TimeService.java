package ru.praktika95.bot.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TimeService {
    public static String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        LocalDate localDate = LocalDate.now();
        return dtf.format(localDate);
    }
}
