package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Command {
    public String name;
    public List<InlineKeyboardButton> buttons;
    public String text;
}
