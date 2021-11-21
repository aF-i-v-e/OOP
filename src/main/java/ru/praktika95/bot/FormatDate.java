package ru.praktika95.bot;

public class FormatDate {

    private static final String[] seasons = new String[] {
            "Янв.", "Февр.", "Март", "Апр.", "Май", "Июнь", "Июль", "Авг.", "Сент.", "Окт.", "Нояб.", "Дек."
    };

    public static String userFormatDate(String simpleDate) {
        if (simpleDate == null)
            return "";
        String[] date = simpleDate.split(" - ");
        String[] dateOne = date[0].split("-");
        if (date.length > 1){
            String[] dateTwo = date[1].split("-");
            return Integer.parseInt(dateOne[2]) + " " + seasons[Integer.parseInt(dateOne[1]) - 1] + " - " +
                    Integer.parseInt(dateTwo[2]) + " " + seasons[Integer.parseInt(dateTwo[1]) - 1];
        }
        else
            return Integer.parseInt(dateOne[2]) + " " + seasons[Integer.parseInt(dateOne[1]) - 1];
    }
}
