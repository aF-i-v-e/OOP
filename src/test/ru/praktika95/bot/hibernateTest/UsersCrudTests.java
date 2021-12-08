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
            testUserCrud.removeAllInstances();
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
        testUserCrud.removeAllInstances();
        testUserCrud.save(testUser);
        List<TestUser> usersList = testUserCrud.getEqualsUsersFromDB(testUser);
        for (TestUser user : usersList) {
            assertEquals(testUser.getChatId(), user.getChatId());
            assertEquals(testUser.getEventDateNotice(),user.getEventDateNotice());
            assertEquals(testUser.getEventUrl(), user.getEventUrl());
        }
        testUserCrud.delete(testUser);
    }

    @Test
    void testGetNotesWithEqualDateNotice() {
        testUserCrud.removeAllInstances();
        testUserCrud.save(testUser);
        String date = TimeService.getCurrentTime();
        List<TestUser> usersList = testUserCrud.getUsersByDate(date);
        for (TestUser user : usersList) {
            assertEquals(date, user.getEventDateNotice());
        }
        testUserCrud.delete(testUser);
    }
    @Test
    void testExistUser() {
        testUserCrud.removeAllInstances();
        testUserCrud.save(testUser);
        assertTrue(testUserCrud.existNote(testUser));
        testUserCrud.delete(testUser);
    }

    @Test
    void testNotExistUser() {
        TestUser user = createUser("1234", "07.12.2021", "url-1");
        assertFalse(testUserCrud.existNote(user));
    }

    @Test
    void testGetByChatId() {
        testUserCrud.removeAllInstances();
        testUserCrud.save(testUser);
        List<TestUser> users = testUserCrud.getByChatId(testUser.getChatId());
        assertTrue(users.size() == 1);
        testUserCrud.delete(testUser);
    }

    @Test
    void testCheckLastId() {
        testUserCrud.removeAllInstances();
        testUserCrud.save(testUser);
        Integer id = testUserCrud.getLastId();
        assertTrue(id == 1);
        testUserCrud.delete(testUser);
    }

    @Test
    void getByID() {
        testUserCrud.removeAllInstances();
        testUserCrud.save(testUser);
        TestUser user = testUserCrud.getById(1);
        assertEquals(testUser.getChatId(), user.getChatId());
        assertEquals(testUser.getEventDateNotice(), user.getEventDateNotice());
        assertEquals(testUser.getEventUrl(), user.getEventUrl());
        testUserCrud.delete(testUser);
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
