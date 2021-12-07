package ru.praktika95.bot.handle.format;

import ru.praktika95.bot.handle.services.chService.CommandHandlerConstants;

public class FormatDate {

    private static final String[] seasons = new String[] {
            "Янв.", "Февр.", "Март", "Апр.", "Май", "Июнь", "Июль", "Авг.", "Сент.", "Окт.", "Нояб.", "Дек."
    };

    public static String userFormatDate(String simpleDate) {
        if (simpleDate == null)
            return "";
        if (!simpleDate.contains("-"))
            return simpleDate;
        String[] date = simpleDate.split(CommandHandlerConstants.dashWithWhitespaces);
        String[] dateOne = date[0].split(CommandHandlerConstants.dash);
        if (date.length > 1){
            String[] dateTwo = date[1].split(CommandHandlerConstants.dash);
            return Integer.parseInt(dateOne[2]) + CommandHandlerConstants.whitespaces + seasons[Integer.parseInt(dateOne[1]) - 1] +
                    CommandHandlerConstants.dashWithWhitespaces +
                    Integer.parseInt(dateTwo[2]) + CommandHandlerConstants.whitespaces + seasons[Integer.parseInt(dateTwo[1]) - 1];
        }
        else
            return Integer.parseInt(dateOne[2]) + CommandHandlerConstants.whitespaces + seasons[Integer.parseInt(dateOne[1]) - 1];
    }
}
