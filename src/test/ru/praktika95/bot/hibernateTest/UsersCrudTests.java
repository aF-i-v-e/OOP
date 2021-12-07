package ru.praktika95.bot.hibernateTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.praktika95.bot.bot.BotConfig;
import ru.praktika95.bot.handle.services.chService.RandomService;
import ru.praktika95.bot.handle.services.timeService.TimeService;
import ru.praktika95.bot.hibernate.DataBaseSettings;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class UsersCrudTests {

    private TestUserCrud testUserCrud;
    private TestUser testUser;

    @BeforeEach
    public void setUp() {
        try {
            testUserCrud = new TestUserCrud();
            testUser = generateTestUser();
            Properties property = BotConfig.getProperties();
            assert property != null;
            DataBaseSettings.setDataBaseSettings(property);
        }catch (IOException e) {
            System.err.println(e);
        }
    }

    @Test
    void testSaveNote() {
        testUserCrud.save(testUser);
    }

    @Test
    void testDeleteNote() {
        testUserCrud.delete(testUser);
    }

    @Test
    void testGetEqualsNote() {
        List<TestUser> usersList = testUserCrud.getEqualsUsersFromDB(testUser);
        for (TestUser user : usersList) {
            assertEquals(testUser.getChatId(), user.getChatId());
            assertEquals(testUser.getEventDateNotice(),user.getEventDateNotice());
            assertEquals(testUser.getEventUrl(), user.getEventUrl());
        }
    }

    @Test
    void testGetNotesWithEqualDateNotice() {
        String date = "07.12.2021";
        List<TestUser> usersList = testUserCrud.getUsersByDate(date);
        for (TestUser user : usersList) {
            assertEquals(date, user.getEventDateNotice());
        }
    }
    @Test
    void testExistUser() {
        TestUser user = createUser("1234", "07.12.2021", "url1");
        assertTrue(testUserCrud.existNote(user));
    }

    @Test
    void testNotExistUser() {
        TestUser user = createUser("1234", "07.12.2021", "url-1");
        assertFalse(testUserCrud.existNote(user));
    }

    @Test
    void testGetByChatId() {
        TestUser user = createUser("1234", "08.12.2021", "url2");
        testUserCrud.save(user);
        List<TestUser> users = testUserCrud.getByChatId(user.getChatId());
        assertTrue(users.size() - 2 >= 0);
    }

    @Test
    void testCheckLastId() {
        TestUser user = generateTestUser();
        testUserCrud.save(user);
        Integer id = testUserCrud.getLastId();
        List<TestUser> users = testUserCrud.getAll();
        assertEquals(users.size(),id);
    }

    @Test
    void getByID() {
        TestUser user = testUserCrud.getById(1);
        assertEquals("1234", user.getChatId());
        assertEquals("07.12.2021", user.getEventDateNotice());
        assertEquals("url1", user.getEventUrl());
    }

    private TestUser generateTestUser() {
        String chatId ="" + RandomService.getRandomIntegerBetweenRange(1000, 10000);
        String date = TimeService.getCurrentTime();
        String url = "url" + RandomService.getRandomIntegerBetweenRange(0, 2048);
        return new TestUser(chatId, date, url);
    }

    private TestUser createUser(String chatId, String date, String url) {
        return new TestUser(chatId, date, url);
    }
}
