package ru.praktika95.bot;

import org.junit.jupiter.api.Test;
import ru.praktika95.bot.handle.SeparatorsConst;
import ru.praktika95.bot.handle.helpers.setDataForResponse;
import ru.praktika95.bot.handle.response.Event;
import ru.praktika95.bot.handle.services.timeService.TimeConstants;

import java.util.Calendar;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CheckNotificationCapabilityTest {

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
