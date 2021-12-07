package ru.praktika95.bot.bot;

import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.praktika95.bot.hibernate.DataBaseSettings;
import ru.praktika95.bot.quartz.QuartzJobScheduler;

import java.io.IOException;
import java.util.Properties;

public class App {

    private static Bot bot;
    public static void main(String[] args) throws TelegramApiException, SchedulerException {
        try {
            Properties property = BotConfig.getProperties();
            assert property != null;
            String name = property.getProperty("bot.name");
            String token = property.getProperty("bot.token");
            bot = new Bot(name, token);
            bot.botConnect();
            QuartzJobScheduler.mainQuartzApp();
            DataBaseSettings.setDataBaseSettings(property);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static Bot getBot() {
        return bot;
    }
}