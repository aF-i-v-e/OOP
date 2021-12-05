package ru.praktika95.bot;

import org.quartz.SchedulerException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.praktika95.bot.hibernate.DataBaseSettings;
import ru.praktika95.bot.quartz.QuartzJobScheduler;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class App {

    private static Bot bot;
    public static void main(String[] args) throws TelegramApiException, SchedulerException {
        try {
            FileInputStream config = new FileInputStream("src/main/resources/config.properties");
            Properties property = new Properties();
            property.load(config);
            String name = property.getProperty("bot.name");
            String token = property.getProperty("bot.token");
            bot = new Bot(name, token);
            bot.botConnect();
            //QuartzJobScheduler.mainQuartzApp();
            String driverBase = property.getProperty("db.driver");
            String urlBase = property.getProperty("db.host");
            String userBase = property.getProperty("db.login");
            String passBase = property.getProperty("db.password");
            String dialectBase = property.getProperty("db.dialect");
            DataBaseSettings.setDataBaseSettings(driverBase, urlBase, userBase, passBase, dialectBase);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static Bot getBot() {
        return bot;
    }
}