package ru.praktika95.bot.handle.parsing;

public interface ParsingConstants {
    int connectionTime = 3000;
    String site = "https://ekb.kassir.ru/category?";
    String agent = "Yandex/21.8.3.614";
    String referrer = "https://yandex.ru/";
    String errorConnect = "Ошибка подключения, попробуйте повторить позже";
    String errorHandle = "Ошибка обработки, попробуйте повторить позже";
    String dateReg = "\"date\".+},";
    String dashWithWhitespaces = " - ";
    String whitespaces = " ";
    String rub = " руб.";
    String href = "href";
    String nul = "null";
    String start_min = "start_min";
    String end_max = "end_max";
    String nullStr = "";
}