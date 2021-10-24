package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

public enum ReplyMarkup {
    MAIN("main") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd, String url) {
            String[] stringButtons = new String[] {
                    "Мероприятия", "show", "Помощь", "help"
            };
            return createRowList(typeButtons, stringButtons, TwoColumns, url);
        }
    },
    DATE("date") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd, String url) {
            String[] stringButtons = new String[] {
                    "Сегодня", "today", "Завтра", "tomorrow",
                    "На этой неделе", "thisWeek", "На следующей неделе", "nextWeek",
                    "В этом месяце", "thisMonth", "В следующем месяце", "nextMonth"
            };
            return createRowList(typeButtons, stringButtons, TwoColumns, url);
        }
    },
    CATEGORY("category") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd, String url) {
            String[] stringButtons = new String[] {
                    "Театр", "theatre",
                    "Музеи", "museum",
                    "Концерт", "concert",
                    "Все мероприятия", "allEvents"
            };
            return createRowList(typeButtons, stringButtons, TwoColumns, url);
        }
    },
    EVENTS("events") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd, String url) {
            String[] stringButtons;
            if (isEnd)
                stringButtons = new String[] {
                        "Просмотреть мероприятие", "event" + " " + number,
                        "Показать ещё", "next"
                };
            else
                stringButtons = new String[] { "Просмотреть мероприятие", "event" + " " + number };
            return createRowList(typeButtons, stringButtons, OneColumn, url);
        }
    },
    EVENT("event") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd, String url) {
            String[] stringButtons = new String[] {
                    "Перейти на сайт", "",
                    "Подписаться на мероприятие", "subscribe",
                    "Купить фальшивый QR-код", "buy"
            };
            return createRowList(typeButtons, stringButtons, OneColumn, url);
        }
    };

    final int TwoColumns = 2;

    final int OneColumn = 1;

    private final String type;

    ReplyMarkup(String type) {
        this.type = type;
    }


    public abstract List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd, String url);

    public static ReplyMarkup findButtons(String typeButtons) {
        for (ReplyMarkup e : values()) {
            if (e.type.equals(typeButtons)) {
                return e;
            }
        }
        return null;
    }

    List<List<InlineKeyboardButton>> createRowList(String typeButtons, String[] stringButtons, int countColumns, String url){
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        int twoCountColumns = 2 * countColumns;
        for(int i = 0; i < stringButtons.length; i += twoCountColumns) {
            LinkedHashMap<String, String> columns = new LinkedHashMap<>();
            for (int j = 0; j < twoCountColumns; j += 2)
                columns.put(stringButtons[i + j], stringButtons[i + j + 1]);
            rowList.add(createColumnList(columns, typeButtons, url));
            url = null;
        }
        return rowList;
    }

    List<InlineKeyboardButton> createColumnList(Map<String, String> columns, String typeButtons, String url) {
        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        for (Map.Entry<String, String> entry : columns.entrySet()) {
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
