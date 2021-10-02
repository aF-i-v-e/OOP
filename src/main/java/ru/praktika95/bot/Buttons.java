package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Buttons {
    public InlineKeyboardMarkup setInline() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        var but=new InlineKeyboardButton();
        but.setText("Hello");
        but.setCallbackData("call back data");
        buttons1.add(but);
        buttons.add(buttons1);

        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
        markupKeyboard.setKeyboard(buttons);
        return  markupKeyboard;
//        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
//        List < List < InlineKeyboardButton >> rowsInline = new ArrayList < > ();
//        List < InlineKeyboardButton > rowInline = new ArrayList < > ();
//        rowInline.add(new InlineKeyboardButton().setText("Open Browser").setUrl("https://www.google.com/"));
//        rowsInline.add(rowInline);
//        markupInline.setKeyboard(rowsInline);
//        message.setReplyMarkup(markupInline);
    }
}
