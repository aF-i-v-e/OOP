package ru.praktika95.bot;

import java.sql.*;

public class DataBase {
    private Statement statement;

    public void connect(String url, String user, String password) {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
//        System.out.println("База Подключена!");
    }

    public void createTable() throws SQLException {
        statement.executeUpdate("CREATE TABLE users (" +
                "id int auto_increment primary key," +
                "chat_id int(30) not null," +
                "date_notice varchar(30) not null," +
                "photo varchar(30) not null," +
                "name_event varchar(30) not null," +
                "date_time varchar(30) not null," +
                "place varchar(30) not null," +
                "price varchar(30) not null)");
    }

    public void deleteTable() throws SQLException {
        statement.executeUpdate("DROP TABLE users-notice");
    }

    public void create() throws SQLException {
        statement.executeUpdate("INSERT");
    }

    public void read() throws SQLException {
        ResultSet record = statement.executeQuery("INSERT");
    }

    public void update() throws SQLException {
        statement.executeUpdate("INSERT");
    }

    public void delete() throws SQLException {
        statement.executeUpdate("INSERT");
    }
}
