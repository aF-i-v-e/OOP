package ru.praktika95.bot;

import static org.junit.jupiter.api.Assertions.*;

import ru.praktika95.bot.handle.SeparatorsConst;
import ru.praktika95.bot.handle.helpers.setDataForResponse;
import ru.praktika95.bot.handle.response.Event;
import ru.praktika95.bot.handle.services.chService.CommandHandlerConstants;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.praktika95.bot.bot.BotRequest;
import ru.praktika95.bot.handle.CommandHandler;
import ru.praktika95.bot.handle.response.Response;
import ru.praktika95.bot.handle.services.timeService.TimeConstants;

import java.util.Calendar;
import java.util.Objects;

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
        Chat chat = new Chat(454652745l, "private", null, "UserFirstName", "UserLastName", "userName", null, null, null, null, null, null, null, null, null, null, null, null, null);
        message.setChat(chat);
        return message;
    }

    private Update createTestUpdate() {
        Message message = createTestMessage();
        Update update = new Update(55632457, message, null, null, null, null, null, null, null, null, null, null, null, null);
        return update;
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

    @Test
    void testCheckNotificationCapabilityIncorrect() {
        Event event = new Event(
                null, null, "19 Дек.", null, null, null, null);
        assertNull(setDataForResponse.checkNotificationCapability(event, ""));
    }

    @Test
    void testCheckNotificationCapabilityCorrectDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        Event event = new Event(
                null, null, year + SeparatorsConst.dash + month + SeparatorsConst.dash + day,
                null, null, null, null);
        String[] answer = Objects.requireNonNull(setDataForResponse.checkNotificationCapability(event, TimeConstants.day));
        assertEquals("0 дней", answer[0]);
        assertEquals(TimeConstants.day, answer[1]);
    }

    @Test
    void testCheckNotificationCapabilityCorrectWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        Event event = new Event(
                null, null, year + SeparatorsConst.dash + month + SeparatorsConst.dash + day,
                null, null, null, null);
        String[] answer = Objects.requireNonNull(setDataForResponse.checkNotificationCapability(event, TimeConstants.week));
        assertEquals("0 дней", answer[0]);
        assertEquals(TimeConstants.week, answer[1]);
    }
}
