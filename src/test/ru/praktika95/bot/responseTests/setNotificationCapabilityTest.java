package ru.praktika95.bot.responseTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.praktika95.bot.handle.CommandHandler;
import ru.praktika95.bot.handle.response.Response;

class setNotificationCapabilityTest {
    @BeforeEach
    public void setUp() {
        CommandHandler commandHandler = new CommandHandler();
        Response response = new Response();
    }

    @Test
    void testCanSetNotificationCapability() {

    }

    @Test
    void testCanNotSetNotificationCapabilityBecauseExist() {

    }

    @Test
    void testCanNotSetNotificationCapabilityBecauseWrongPeriodDay() {

    }

    @Test
    void testCanNotSetNotificationCapabilityBecauseWrongPeriodWeek() {

    }
}
