package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public enum ReplyMarkup {
    MAIN("main") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd) {
            String[] stringButtons = new String[] {
                    "Мероприятия", "show", "Помощь", "help"
            };
            return createRowList(typeButtons,stringButtons);
        }
    },
    DATE("date") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd){
            String[] stringButtons = new String[] {
                    "Сегодня", "today", "Завтра", "tomorrow",
                    "На этой неделе", "thisWeek", "На следующей неделе", "nextWeek",
                    "В этом месяце", "thisMonth", "В следующем месяце", "nextMonth"
            };
            return createRowList(typeButtons,stringButtons);
        }
    },
    CATEGORY("category") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd){
            String[] stringButtons = new String[] {
                    "Театр", "theatre",
                    "Музеи", "museum",
                    "Концерт", "concert",
                    "Все мероприятия", "allEvents"
            };
            return createRowList(typeButtons,stringButtons);
        }
    },
    EVENTS("events") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd){
            String[] stringButtons = new String[] {
                    "Просмотреть мероприятие", "event" + " " + number
            };
            if (isEnd)
                stringButtons = new String[] { "Показать ещё", "next" };
            return createRowList(typeButtons, stringButtons);
        }
    };

    private final String type;

    ReplyMarkup(String type) {
        this.type = type;
    }

    public abstract List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd);

    public static ReplyMarkup findButtons(String typeButtons) {
        for (ReplyMarkup e : values()) {
            if (e.type.equals(typeButtons)) {
                return e;
            }
        }
        return null;
    }

    List<List<InlineKeyboardButton>> createRowList(String typeButtons, String[] stringButtons){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for(int i = 0; i < stringButtons.length; i+=4) {
            LinkedHashMap<String, String> buttons = new LinkedHashMap<>();
            buttons.put(stringButtons[i], stringButtons[i+1]);
            buttons.put(stringButtons[i+2], stringButtons[i+3]);
            rowList.add(createButton(buttons, typeButtons, null));
        }
        return rowList;
    }

    List<InlineKeyboardButton> createButton(Map<String, String> buttons, String typeButtons, String url) {
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
