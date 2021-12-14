package ru.praktika95.bot.serviceTest;

import org.junit.jupiter.api.Test;
import ru.praktika95.bot.handle.services.timeService.TimeService;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeServiceTest {
    @Test
    void test1() {
        String date = "08.12.2021";
        String actual = TimeService.getTimeInDBFormat(date);
        String expected = "8.12.2021";
        assertEquals(expected, actual);
    }

    @Test
    void test2() {
        String date = "17.04.2021";
        String actual = TimeService.getTimeInDBFormat(date);
        String expected = "17.4.2021";
        assertEquals(expected, actual);
    }

    @Test
    void test3() {
        String date = "03.05.2021";
        String actual = TimeService.getTimeInDBFormat(date);
        String expected = "3.5.2021";
        assertEquals(expected, actual);
    }

    @Test
    void test4() {
        String date = "13.11.2021";
        String actual = TimeService.getTimeInDBFormat(date);
        String expected = "13.11.2021";
        assertEquals(expected, actual);
    }

    @Test
    void test5() {
        String date = "08-12-2021";
        String actual = TimeService.getTimeInDBFormat(date);
        String expected = "8-12-2021";
        assertEquals(expected, actual);
    }

    @Test
    void test6() {
        String date = "17-04-2021";
        String actual = TimeService.getTimeInDBFormat(date);
        String expected = "17-4-2021";
        assertEquals(expected, actual);
    }

    @Test
    void test7() {
        String date = "03-05-2021";
        String actual = TimeService.getTimeInDBFormat(date);
        String expected = "3-5-2021";
        assertEquals(expected, actual);
    }

    @Test
    void test8() {
        String date = "13-11-2021";
        String actual = TimeService.getTimeInDBFormat(date);
        String expected = "13-11-2021";
        assertEquals(expected, actual);
    }
}
