package ru.praktika95.bot.handle.format;

import ru.praktika95.bot.handle.SeparatorsConst;

import java.util.regex.Pattern;

public class FormatDate {

    private static final String[] seasons = new String[] {
            "Янв.", "Февр.", "Март", "Апр.", "Май", "Июнь", "Июль", "Авг.", "Сент.", "Окт.", "Нояб.", "Дек."
    };

    public static String userFormatDate(String simpleDate) {
        if (simpleDate == null)
            return SeparatorsConst.nullStr;
        if (!Pattern.matches(".+-.+-.+", simpleDate))
            return simpleDate;
        String[] date = simpleDate.split(SeparatorsConst.dashWithWhitespaces);
        String[] dateOne = date[0].split(SeparatorsConst.dash);
        if (date.length > 1){
            String[] dateTwo = date[1].split(SeparatorsConst.dash);
            return Integer.parseInt(dateOne[2]) + SeparatorsConst.whitespaces + seasons[Integer.parseInt(dateOne[1]) - 1] +
                    SeparatorsConst.dashWithWhitespaces +
                    Integer.parseInt(dateTwo[2]) + SeparatorsConst.whitespaces + seasons[Integer.parseInt(dateTwo[1]) - 1];
        }
        else
            return Integer.parseInt(dateOne[2]) + SeparatorsConst.whitespaces + seasons[Integer.parseInt(dateOne[1]) - 1];
    }
}
