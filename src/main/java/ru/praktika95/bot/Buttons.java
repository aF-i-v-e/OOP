package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.lang.reflect.*;
import java.util.ArrayList;
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

    private List<List<InlineKeyboardButton>> main(String typeButtons){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Map<String, String> buttons = Map.of(
                "Мероприятия", "show",
                "Помощь", "help"
        );
        rowList.add(createButton(buttons, typeButtons));
        return rowList;
    }

    private List<List<InlineKeyboardButton>> date(String typeButtons){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Map<String, String> buttons = Map.of(
                "Сегодня", "today",
                "Завтра", "tomorrow"
        );
        rowList.add(createButton(buttons, typeButtons));
        buttons = Map.of(
                "На этой неделе", "thisWeek",
                "На следующей неделе", "nextWeek"
        );
        rowList.add(createButton(buttons, typeButtons));
        buttons = Map.of(
                "В этом месяце", "thisMonth",
                "В следующем месяце", "nextMonth"
        );
        rowList.add(createButton(buttons, typeButtons));
        return rowList;
    }

    private List<List<InlineKeyboardButton>> category(String typeButtons) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Map<String, String> buttons = Map.of(
        "Театр", "theatre",
        "Кино", "movie"
        );
        rowList.add(createButton(buttons, typeButtons));
        buttons = Map.of(
        "Концерт", "concert",
        "Все мероприятия", "allEvents"
        );
        rowList.add(createButton(buttons, typeButtons));
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
