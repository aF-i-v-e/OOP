package ru.praktika95.bot.hibernate;

import java.util.Properties;

public class DataBaseSettings {

    public static void setDataBaseSettings(Properties properties) {
        String driverBase = properties.getProperty("db.driver");
        String urlBase = properties.getProperty("db.host");
        String userBase = properties.getProperty("db.login");
        String passBase = properties.getProperty("db.password");
        String dialectBase = properties.getProperty("db.dialect");
        DataBaseSettings.setDataBaseSettings(driverBase, urlBase, userBase, passBase, dialectBase);
    }

    private static String driver;
    private static String url;
    private static String user;
    private static String pass;
    private static String dialect;

    private static void setDataBaseSettings (String driverBase, String urlBase, String userBase, String passBase, String dialectBase) {
        driver = driverBase;
        url = urlBase;
        user = userBase;
        pass = passBase;
        dialect = dialectBase;
    }

    public static String getDriver() {
        return driver;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUser() {
        return user;
    }

    public static String getPass() {
        return pass;
    }

    public static String getDialect() {
        return dialect;
    }

}
