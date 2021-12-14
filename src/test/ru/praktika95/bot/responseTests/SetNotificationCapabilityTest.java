package ru.praktika95.bot.responseTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.praktika95.bot.bot.BotConfig;
import ru.praktika95.bot.handle.CommandHandler;
import ru.praktika95.bot.handle.response.Event;
import ru.praktika95.bot.handle.response.Response;
import ru.praktika95.bot.handle.services.chService.CommandHandlerConstants;
import ru.praktika95.bot.handle.services.chService.StringFormatService;
import ru.praktika95.bot.handle.services.timeService.TimeConstants;
import ru.praktika95.bot.handle.services.timeService.TimeService;
import ru.praktika95.bot.hibernate.DataBaseSettings;
import ru.praktika95.bot.hibernate.User;
import ru.praktika95.bot.hibernate.UsersCRUD;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class setNotificationCapabilityTest {
    private CommandHandler commandHandler;
    private Response response;
    private Event testEvent;
    private UsersCRUD usersCRUD;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        commandHandler = new CommandHandler();
        response = new Response();
        usersCRUD = new UsersCRUD();
        testEvent = new Event("https://cdn.kassir.ru/ekb/poster/ad/ad775642fb783dd5d9e212caa504c0e5.jpeg",
                "GAYAZOVS BROTHERS В ЕКАТЕРИНБУРГЕ","2022-04-23", "19:00", "TELE-CLUB - ТЕЛЕ-КЛУБ", " 1 800 — 4 500",
                "https://ekb.kassir.ru/koncert/gayazov-brothers#916376");
        Properties property = BotConfig.getProperties();
        assert property != null;
        DataBaseSettings.setDataBaseSettings(property);
    }

    @Test
    void test1CanSetNotificationCapabilityDay() {
        SetUpTestData.setBotRequest("period", "day");
        response.setSelectedEvent(testEvent);
        response.setChatId(SetUpTestData.getBotRequest().getChatId());
        commandHandler.setNotification(response, TimeConstants.day);
        assertEquals(testEvent.getEventNotification(TimeConstants.day), response.getText());
        assertTrue("841.jpg".equals(response.getPhotoFile().getMediaName())
                || "842.jpg".equals(response.getPhotoFile().getMediaName()));
        Response correctResponse = new Response();
        correctResponse.createButtons(CommandHandlerConstants.cancelButtons,
                response.map.get(CommandHandlerConstants.cancelButtons).toString(), false, null);
        assertEquals(correctResponse.getKeyboard(), response.getKeyboard());
    }

    @Test
    void test2CanSetNotificationCapabilityWeek() {
        SetUpTestData.setBotRequest("period", "week");
        response.setSelectedEvent(testEvent);
        response.setChatId(SetUpTestData.getBotRequest().getChatId());
        commandHandler.setNotification(response, TimeConstants.week);
        assertEquals(testEvent.getEventNotification(TimeConstants.week), response.getText());
        assertTrue("841.jpg".equals(response.getPhotoFile().getMediaName())
                || "842.jpg".equals(response.getPhotoFile().getMediaName()));
        Response correctResponse = new Response();
        correctResponse.createButtons(CommandHandlerConstants.cancelButtons,
                response.map.get(CommandHandlerConstants.cancelButtons).toString(), false, null);
        assertEquals(correctResponse.getKeyboard(), response.getKeyboard());
    }

    @Test
    void test3CanNotSetNotificationCapabilityBecauseExistDay() {
        SetUpTestData.setBotRequest("period", "day");
        response.setSelectedEvent(testEvent);
        response.setChatId(SetUpTestData.getBotRequest().getChatId());
        commandHandler.setNotification(response, TimeConstants.day);
        assertEquals(CommandHandlerConstants.YouShallNotPassNoticeImage + ".jpg", response.getPhotoFile().getMediaName());
        assertEquals(CommandHandlerConstants.existNotification, response.getText());
        rollBack("22.4.2022", SetUpTestData.getBotRequest().getChatId());
    }

    private void rollBack(String dateNotice, String chatId) {
        User user = new User(chatId, testEvent);
        user.setEventDateNotice(dateNotice);
        List<User> users = usersCRUD.getEqualsUsersFromDB(user);
        for (User u : users)
            usersCRUD.delete(u);
    }

    @Test
    void test4CanNotSetNotificationCapabilityBecauseExistWeek() {
        SetUpTestData.setBotRequest("period", "week");
        response.setSelectedEvent(testEvent);
        response.setChatId(SetUpTestData.getBotRequest().getChatId());
        commandHandler.setNotification(response, TimeConstants.week);
        assertEquals(CommandHandlerConstants.YouShallNotPassNoticeImage+".jpg", response.getPhotoFile().getMediaName());
        assertEquals(CommandHandlerConstants.existNotification, response.getText());
        rollBack("16.4.2022", SetUpTestData.getBotRequest().getChatId());
    }

    @Test
    void test5CanNotSetNotificationCapabilityBecauseWrongPeriodDay() {
        SetUpTestData.setBotRequest("period", "day");
        Event event = new Event("https://cdn.kassir.ru/ekb/poster/ad/ad775642fb783dd5d9e212caa504c0e5.jpeg",
                "GAYAZOVS BROTHERS В ЕКАТЕРИНБУРГЕ", TimeService.getCurrentTimePatternWithDash(), "19:00", "TELE-CLUB - ТЕЛЕ-КЛУБ", " 1 800 — 4 500",
                "https://ekb.kassir.ru/koncert/gayazov-brothers#916376");
        response.setSelectedEvent(event);
        response.setChatId(SetUpTestData.getBotRequest().getChatId());
        commandHandler.setNotification(response, TimeConstants.day);
        assertEquals(CommandHandlerConstants.YouShallNotPassNoticeImage+".jpg", response.getPhotoFile().getMediaName());
        assertEquals(StringFormatService.getString(CommandHandlerConstants.youCannotSetNotification,
               "0", TimeConstants.day), response.getText());
    }

    @Test
    void test6CanNotSetNotificationCapabilityBecauseWrongPeriodWeek() {
        int delta = 6;
        SetUpTestData.setBotRequest("period", "week");
        Event event = new Event("https://cdn.kassir.ru/ekb/poster/ad/ad775642fb783dd5d9e212caa504c0e5.jpeg",
                "GAYAZOVS BROTHERS В ЕКАТЕРИНБУРГЕ",TimeService.getTimeWithDeltaInDays(delta), "19:00", "TELE-CLUB - ТЕЛЕ-КЛУБ", " 1 800 — 4 500",
                "https://ekb.kassir.ru/koncert/gayazov-brothers#916376");
        response.setSelectedEvent(event);
        response.setChatId(SetUpTestData.getBotRequest().getChatId());
        commandHandler.setNotification(response, TimeConstants.week);
        assertEquals(CommandHandlerConstants.YouShallNotPassNoticeImage+".jpg", response.getPhotoFile().getMediaName());
        assertEquals(StringFormatService.getString(CommandHandlerConstants.youCannotSetNotification,
                Integer.toString(delta), TimeConstants.week), response.getText());
    }
}
