package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class Button {
    public InlineKeyboardButton button;
    public Button(String text,String callBackData)
    {
        button=new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callBackData);
    }
    public InlineKeyboardButton getInlineKeyboardButton()
    {
        return button;
    }

}
