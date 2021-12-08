package ru.praktika95.bot.responseTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.praktika95.bot.handle.CommandHandler;
import ru.praktika95.bot.handle.response.Response;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReplyMarkupResponseTest {

    private CommandHandler commandHandler;
    private Response response;

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
        response = new Response();
    }

    private Response createCorrectBotResponse(String typeButtons){
        Response correctBotResponse = new Response();
        int status = correctBotResponse.map.get(typeButtons);
        correctBotResponse.createButtons(getKey(++status, response.map), null, false, null);
        return correctBotResponse;
    }

    private String getKey(int status, Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == status) {
                return entry.getKey();
            }
        }
        return null;
    }


    private void compareReplyMarkupBotResponse(String typeButtons){
        commandHandler.commandHandler(SetUpTestData.getBotRequest(), response);
        Response correctBotResponse = createCorrectBotResponse(typeButtons);
        assertEquals(correctBotResponse.getKeyboard(), response.getKeyboard());
    }

    @Test
    void testCorrectShowCommand() {
        SetUpTestData.setBotRequest("main", "show");
        compareReplyMarkupBotResponse("main");
    }

    @Test
    void testCorrectTodayCommand() {
        SetUpTestData.setBotRequest("date", "today");
        compareReplyMarkupBotResponse("date");
    }

    @Test
    void testCorrectTomorrowCommand() {
        SetUpTestData.setBotRequest("date", "tomorrow");
        compareReplyMarkupBotResponse("date");
    }

    @Test
    void testCorrectThisWeekCommand() {
        SetUpTestData.setBotRequest("date", "thisWeek");
        compareReplyMarkupBotResponse("date");
    }
    @Test
    void testCorrectNextWeekCommand() {
        SetUpTestData.setBotRequest("date", "nextWeek");
        compareReplyMarkupBotResponse("date");
    }

    @Test
    void testCorrectThisMonthCommand() {
        SetUpTestData.setBotRequest("date", "thisMonth");
        compareReplyMarkupBotResponse("date");
    }

    @Test
    void testCorrectNextMonthCommand() {
        SetUpTestData.setBotRequest("date", "nextMonth");
        compareReplyMarkupBotResponse("date");
    }

    @Test
    void testCorrectEventCommand() {
        SetUpTestData.setBotRequest("event", "subscribe");
        compareReplyMarkupBotResponse("event");
    }
}
