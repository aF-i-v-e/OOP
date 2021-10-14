package ru.praktika95.bot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

public class App {

    public static void main(String[] args) throws TelegramApiException {
        try {
            FileInputStream config = new FileInputStream("src/main/resources/config.properties");
            Properties property = new Properties();
            property.load(config);
            String name = property.getProperty("bot.name");
            String token = property.getProperty("bot.token");
            Bot bot = new Bot(name, token);
            bot.botConnect();
//            String urlBase = property.getProperty("db.host");
//            String userBase = property.getProperty("db.login");
//            String passBase = property.getProperty("db.password");
//            DataBase dataBase = new DataBase();
//            dataBase.connect(urlBase, userBase, passBase);
//            dataBase.createTable();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}