package ru.praktika95.bot.responseTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.praktika95.bot.bot.BotRequest;
import ru.praktika95.bot.handle.CommandHandler;
import ru.praktika95.bot.handle.response.Response;
import ru.praktika95.bot.handle.services.chService.CommandHandlerConstants;
import ru.praktika95.bot.handle.services.timeService.TimeConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringResponseTest {
    private CommandHandler commandHandler;
    private Response response;
    private BotRequest botRequest;

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        response = new Response();
    }

    private void compareBasicCommandWork(String basicCommand, String correctAnswer){
        commandHandler.commandHandler(basicCommand, response);
        assertEquals(correctAnswer,response.getText());
    }

    @Test
    void testCorrectHelpMessage() {
        compareBasicCommandWork("help", CommandHandlerConstants.helpMessage);
    }

    @Test
    void testCorrectStartMessage() {
        compareBasicCommandWork("start", CommandHandlerConstants.helloMessage);
    }

    @Test
    void testIncorrectMessage() {
        compareBasicCommandWork("read", CommandHandlerConstants.otherCommand);
    }

    private void setBotRequest(String typeButtons, String botCommand) {
        Update update = createTestUpdate();
        botRequest = new BotRequest(update);
        botRequest.setTypeButtons(typeButtons);
        botRequest.setBotCommand(botCommand);
    }

    private Message createTestMessage() {
        Message message = new Message();
        message.setMessageId(689);
        Chat chat = new Chat(454652745L, "private", null, "UserFirstName", "UserLastName", "userName", null, null, null, null, null, null, null, null, null, null, null, null, null);
        message.setChat(chat);
        return message;
    }

    private Update createTestUpdate() {
        Message message = createTestMessage();
        return new Update(55632457, message, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    private void compareStringBotResponse(String correctAnswer) {
        commandHandler.commandHandler(botRequest, response);
        assertEquals(correctAnswer, response.getText());
    }

    @Test
    void testCorrectShowCommand() {
        setBotRequest("main", "show");
        compareStringBotResponse(TimeConstants.chooseDate);
    }

    @Test
    void testCorrectHelpCommand() {
        setBotRequest("main", "help");
        String correctAns = CommandHandlerConstants.helpMessage;
        compareStringBotResponse(correctAns);
    }

    @Test
    void testCorrectTodayCommand() {
        setBotRequest("date", "today");
        compareStringBotResponse(CommandHandlerConstants.dateText + TimeConstants.today);
    }

    @Test
    void testCorrectTomorrowCommand() {
        setBotRequest("date", "tomorrow");
        compareStringBotResponse( CommandHandlerConstants.dateText + TimeConstants.tomorrow);
    }

    @Test
    void testCorrectThisWeekCommand() {
        setBotRequest("date", "thisWeek");
        compareStringBotResponse(CommandHandlerConstants.dateText + TimeConstants.thisWeek);
    }

    @Test
    void testCorrectNextWeekCommand() {
        setBotRequest("date", "nextWeek");
        compareStringBotResponse(CommandHandlerConstants.dateText + TimeConstants.nextWeek);
    }

    @Test
    void testCorrectThisMonthCommand() {
        setBotRequest("date", "thisMonth");
        compareStringBotResponse(CommandHandlerConstants.dateText + TimeConstants.thisMonth);
    }

    @Test
    void testCorrectSubscribeCommand() {
        setBotRequest("event", "subscribe");
        compareStringBotResponse(CommandHandlerConstants.choosePeriod);
    }

    @Test
    void testCorrectBuyCommand() {
        setBotRequest("event", "buy");
        compareStringBotResponse(CommandHandlerConstants.catPolice);
    }
}
