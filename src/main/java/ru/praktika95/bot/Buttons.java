package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Buttons {

    @SuppressWarnings("unchecked")
    public InlineKeyboardMarkup createButtons(String typeButtons)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method method = Buttons.class.getDeclaredMethod(typeButtons, String.class);
        List<List<InlineKeyboardButton>> rowList = (List<List<InlineKeyboardButton>>) method.invoke(new Buttons(), typeButtons);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private List<List<InlineKeyboardButton>> createRowList(String typeButtons, String[] stringButtons){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for(int i = 0; i < stringButtons.length; i+=4) {
            LinkedHashMap<String, String> buttons = new LinkedHashMap<>();
            buttons.put(stringButtons[i], stringButtons[i+1]);
            buttons.put(stringButtons[i+2], stringButtons[i+3]);
            rowList.add(createButton(buttons, typeButtons));
        }
        return rowList;
    }

    private List<List<InlineKeyboardButton>> main(String typeButtons){
        String[] stringButtons = new String[] {
                "Мероприятия", "show", "Помощь", "help"
        };
        List<List<InlineKeyboardButton>> rowList = createRowList(typeButtons,stringButtons);
        return rowList;
    }

    private List<List<InlineKeyboardButton>> date(String typeButtons){
        String[] stringButtons = new String[] {
                "Сегодня", "today", "Завтра", "tomorrow",
                "На этой неделе", "thisWeek", "На следующей неделе", "nextWeek",
                "В этом месяце", "thisMonth", "В следующем месяце", "nextMonth"
        };
        List<List<InlineKeyboardButton>> rowList = createRowList(typeButtons,stringButtons);
        return rowList;
    }

    private List<List<InlineKeyboardButton>> category(String typeButtons) {
        String[] stringButtons = new String[] {
                "Театр", "theatre",
                "Кино", "movie",
                "Концерт", "concert",
                "Все мероприятия", "allEvents"
        };
        List<List<InlineKeyboardButton>> rowList = createRowList(typeButtons,stringButtons);
        return rowList;
    }

    private List<InlineKeyboardButton> createButton(Map<String, String> buttons, String typeButtons) {
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (Map.Entry<String, String> entry : buttons.entrySet()) {
            keyboardButtonsRow.add(InlineKeyboardButton
                    .builder()
                    .text(entry.getKey())
                    .callbackData(typeButtons + " " + entry.getValue())
                    .build()
            );
        }
        return keyboardButtonsRow;
    }
}
