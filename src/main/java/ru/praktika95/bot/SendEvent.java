package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public class SendEvent {
    private SendMessage sendMessage;
    private SendPhoto sendPhoto;

    public SendEvent (BotResponse botResponse, String text) {
        sendMessage = new SendMessage();
        createSendMessage(botResponse, text);
    }

    public SendEvent(BotResponse botResponse, String caption, String photoPath) {
        sendPhoto = new SendPhoto();
        createSendPhoto(botResponse, caption, photoPath);
    }

    public SendMessage getSendMessage() {
        return sendMessage;
    }

    public SendPhoto getSendPhoto() {
        return sendPhoto;
    }

    private void createSendMessage(BotResponse botResponse, String text) {
        String chatId = botResponse.getSendMessage().getChatId();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
    }

    private void createSendPhoto(BotResponse botResponse, String caption, String photoPath) {
        String chatId = botResponse.getSendMessage().getChatId();
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption(caption);
        sendPhoto.setPhoto(new InputFile().setMedia(photoPath));
    }

    public void createButtons(BotResponse botResponse) {
        ReplyKeyboard replyKeyboard = botResponse.getSendPhoto().getReplyMarkup();
        sendPhoto.setReplyMarkup(replyKeyboard);
    }
}
