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
        String correctAnswer = "Доступные команды:\nhelp - узнать список доступных команд.\nhello - получить приветственное сообщение.\nshow - узнать ближайшие мероприятия.\nchoose \"номер мероприятия\" - выбрать мероприятие.";
        String botAnswer = commandHandler.commandHandler(new String[] {"help", "0"});
        assertEquals(correctAnswer, botAnswer);
    }

    @Test
    void testCorrectHelloMessage() {
        String correctAnswer = "Привет!\nЯ бот, которые может показать ближайшие мероприятия. Вы можете подписаться на их уведомление и вы точно про него не забудете.";
        String botAnswer = commandHandler.commandHandler(new String[] {"hello", "0"});
        assertEquals(correctAnswer, botAnswer);
    }

    @Test
    void testCorrectShowMessage() {
        String correctAnswer = "1. Первое мероприятие.\n2. Второе мероприятие.\n3. Третье мероприятие.";
        String botAnswer = commandHandler.commandHandler(new String[] {"show", "0"});
        assertEquals(correctAnswer, botAnswer);
    }

    @Test
    void testCorrectChooseMessage() {
        String correctAnswer = "Информация второго мероприятия.";
        String botAnswer = commandHandler.commandHandler(new String[] {"choose", "0"});
        assertEquals(correctAnswer, botAnswer);
    }

    @Test
    void testNotExistsNumberEventChooseMessage() {
        String correctAnswer = "Такого мероприятия не существует.";
        String botAnswer = commandHandler.commandHandler(new String[] {"choose", "0"});
        assertEquals(correctAnswer, botAnswer);
    }

    @Test
    void testIncorrectNumberEventMessage() {
        String correctAnswer = "Вы ввели некорректный номер мероприятия.";
        String botAnswer = commandHandler.commandHandler(new String[] {"choose", "0"});
        assertEquals(correctAnswer, botAnswer);
    }

    @Test
    void testIncorrectMessage() {
        String correctAnswer = "Введённой команды не существует, вы можете выполнить команду help, чтобы узнать список доступных команд.";
        String botAnswer = commandHandler.commandHandler(new String[] {"read", "0"});
        assertEquals(correctAnswer, botAnswer);
    }
}
