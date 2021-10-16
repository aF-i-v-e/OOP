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

    private List<List<InlineKeyboardButton>> createRowList(String typeButtons, String[] stringButtons){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for(int i = 0; i < stringButtons.length; i+=4) {
            LinkedHashMap<String, String> buttons = new LinkedHashMap<>();
            buttons.put(stringButtons[i], stringButtons[i+1]);
            buttons.put(stringButtons[i+2], stringButtons[i+3]);
            rowList.add(createButton(buttons, typeButtons, null));
        }
        return rowList;
    }

    private List<List<InlineKeyboardButton>> main(String typeButtons){
        String[] stringButtons = new String[] {
                "Мероприятия", "show", "Помощь", "help"
        };
        return createRowList(typeButtons,stringButtons);
    }

    private List<List<InlineKeyboardButton>> date(String typeButtons){
        String[] stringButtons = new String[] {
                "Сегодня", "today", "Завтра", "tomorrow",
                "На этой неделе", "thisWeek", "На следующей неделе", "nextWeek",
                "В этом месяце", "thisMonth", "В следующем месяце", "nextMonth"
        };
        return createRowList(typeButtons,stringButtons);
    }

    private List<List<InlineKeyboardButton>> category(String typeButtons) {
        String[] stringButtons = new String[] {
                "Театр", "theatre",
                "Музеи", "museum",
                "Концерт", "concert",
                "Все мероприятия", "allEvents"
        };
        return createRowList(typeButtons,stringButtons);
    }

    private List<List<InlineKeyboardButton>> events(String typeButtons, String number) {
        String[] stringButtons = new String[] {
                "Просмотреть мероприятие", "event" + number
        };
        return createRowList(typeButtons, stringButtons);
    }

    private List<List<InlineKeyboardButton>> nextEvents(String typeButtons, String number) {
        String[] stringButtons = new String[] {
                "Показать ещё", "next"
        };
        return createRowList(typeButtons, stringButtons);
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
