package ru.praktika95.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public class BotResponse {
    private SendMessage sendMessage;
    private SendPhoto sendPhoto;

    public BotResponse(Response response) {
        formSendMessage(response);
        if (response.getPhotoFile() != null)
            formSendPhoto(response);
    }

    public SendPhoto getSendPhoto() {
        return this.sendPhoto;
    }

    public SendMessage getSendMessage() {
        return this.sendMessage;
    }

    private void formSendMessage(Response response) {
        this.sendMessage = new SendMessage();
        this.sendMessage.setChatId(response.getChatId());
        this.sendMessage.setText(response.getText());
        this.sendMessage.setReplyMarkup(response.getKeyboard());
    }

    private void formSendPhoto(Response response) {
        this.sendPhoto = new SendPhoto();
        this.sendPhoto.setPhoto(response.getPhotoFile());
        this.sendPhoto.setChatId(response.getChatId());
        this.sendPhoto.setCaption(response.getText());
        this.sendPhoto.setReplyMarkup(response.getKeyboard());
    }
}
