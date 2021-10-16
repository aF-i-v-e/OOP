package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.lang.reflect.*;
import java.util.*;

public class Buttons {

    @SuppressWarnings("unchecked")
    public InlineKeyboardMarkup createButtons(String typeButtons, String number, String url)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        List<List<InlineKeyboardButton>> rowList;
        if (Objects.equals(typeButtons, "events"))
            rowList = events(typeButtons, number);
        else if (Objects.equals(typeButtons, "nextEvents"))
            rowList = nextEvents(typeButtons, number);
//        else if (Objects.equals(typeButtons, "event"))
//            rowList = event(typeButtons, url);
        else {
            E.findValue(typeButtons).handle();
            Method method = Buttons.class.getDeclaredMethod(typeButtons, String.class);
            rowList = (List<List<InlineKeyboardButton>>) method.invoke(new Buttons(), typeButtons);
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        assert rowList != null;
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private List<List<InlineKeyboardButton>> main(String typeButtons){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Map<String, String> buttons = Map.of(
                "Мероприятия", "show",
                "Помощь", "help"
        );
        rowList.add(createButton(buttons, typeButtons, null));
        return rowList;
    }

    private List<List<InlineKeyboardButton>> date(String typeButtons){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Map<String, String> buttons = Map.of(
                "Сегодня", "today",
                "Завтра", "tomorrow"
        );
        rowList.add(createButton(buttons, typeButtons, null));
        buttons = Map.of(
                "На этой неделе", "thisWeek",
                "На следующей неделе", "nextWeek"
        );
        rowList.add(createButton(buttons, typeButtons, null));
        buttons = Map.of(
                "В этом месяце", "thisMonth",
                "В следующем месяце", "nextMonth"
        );
        rowList.add(createButton(buttons, typeButtons, null));
        return rowList;
    }

    private List<List<InlineKeyboardButton>> category(String typeButtons) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Map<String, String> buttons = Map.of(
        "Театр", "theatre",
        "Музеи", "museum"
        );
        rowList.add(createButton(buttons, typeButtons, null));
        buttons = Map.of(
        "Концерт", "concert",
        "Все мероприятия", "allEvents"
        );
        rowList.add(createButton(buttons, typeButtons, null));
        return rowList;
    }

    private List<List<InlineKeyboardButton>> events(String typeButtons, String number) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Map<String, String> buttons = Map.of("Просмотреть мероприятие", "event" + number);
        rowList.add(createButton(buttons, typeButtons, null));
        return rowList;
    }

    private List<List<InlineKeyboardButton>> nextEvents(String typeButtons, String number) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Map<String, String> buttons = Map.of("Показать ещё", "next");
        rowList.add(createButton(buttons, typeButtons, null));
        return rowList;
    }

    private List<List<InlineKeyboardButton>> event(String typeButtons, String url) {
        return null;
    }

    private List<InlineKeyboardButton> createButton(Map<String, String> buttons, String typeButtons, String url) {
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (Map.Entry<String, String> entry : buttons.entrySet()) {
            keyboardButtonsRow.add(InlineKeyboardButton
                    .builder()
                    .text(entry.getKey())
                    .callbackData(typeButtons + " " + entry.getValue())
                    .url(url)
                    .build()
            );
        }
        return keyboardButtonsRow;
    }
}
