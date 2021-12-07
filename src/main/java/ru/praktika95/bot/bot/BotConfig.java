package ru.praktika95.bot.bot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class BotConfig {
    public static Properties getProperties() throws FileNotFoundException {
        try {
            FileInputStream config = new FileInputStream("src/main/resources/config.properties");
            Properties property = new Properties();
            property.load(config);
            return property;
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }
}
