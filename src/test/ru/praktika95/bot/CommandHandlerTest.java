package ru.praktika95.bot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandHandlerTest {
    private CommandHandler commandHandler;

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
    }

    @Test
    void testCorrectHelpMessage() {
        String correctAnswer = "Доступные команды:\n/help - узнать список доступных команд.\n/hello - получить приветственное сообщение.\n/show - узнать ближайшие мероприятия.\n/choose \"номер мероприятия\" - выбрать мероприятие.";
        BotResponse botResponse = commandHandler.commandHandler("/help 0");
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    @Test
    void testCorrectHelloMessage() {
        String correctAnswer = "Привет!\nЯ бот, которые может показать ближайшие мероприятия. Вы можете подписаться на их уведомление и вы точно про него не забудете.";
        BotResponse botResponse = commandHandler.commandHandler("/hello 0");
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    @Test
    void testCorrectShowMessage() {
        String correctAnswer = "1. Первое мероприятие.\n2. Второе мероприятие.\n3. Третье мероприятие.";
        BotResponse botResponse = commandHandler.commandHandler("/show 0");
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    @Test
    void testCorrectChooseMessage() {
        String correctAnswer = "Информация второго мероприятия.";
        BotResponse botResponse = commandHandler.commandHandler("/choose 2");
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    @Test
    void testNotExistsNumberEventChooseMessage() {
        String correctAnswer = "Такого мероприятия не существует.";
        BotResponse botResponse = commandHandler.commandHandler("/choose 0");
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    @Test
    void testIncorrectNumberEventMessage() {
        String correctAnswer = "Вы ввели некорректный номер мероприятия.";
        BotResponse botResponse = commandHandler.commandHandler("/choose -1");
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }

    @Test
    void testIncorrectMessage() {
        String correctAnswer = "Введённой команды не существует, вы можете выполнить команду /help, чтобы узнать список доступных команд.";
        BotResponse botResponse = commandHandler.commandHandler("/read 0");
        assertEquals(correctAnswer, botResponse.getSendMessage().getText());
    }
}
