package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public enum ReplyMarkup {
    MAIN("main") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd) {
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            Map<String, String> buttons = Map.of(
                    "Мероприятия", "show",
                    "Помощь", "help"
            );
            rowList.add(createButton(buttons, typeButtons, null));
            return rowList;
        }
    },
    DATE("date") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd){
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
    },
    CATEGORY("category") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd){
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
    },
    EVENTS("events") {
        public List<List<InlineKeyboardButton>> handler(String typeButtons, String number, boolean isEnd){
            List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
            Map<String, String> buttons = Map.of("Просмотреть мероприятие", "event" + number);
            rowList.add(createButton(buttons, typeButtons, null));
            if (isEnd){
                buttons = Map.of("Показать ещё", "next");
                rowList.add(createButton(buttons, typeButtons, null));
            }
            return rowList;
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
