package ru.praktika95.bot.hibernate;

public class DataBaseSettings {

    private static String driver;
    private static String url;
    private static String user;
    private static String pass;
    private static String dialect;

    public static void setDataBaseSettings (String driverBase, String urlBase, String userBase, String passBase, String dialectBase) {
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
