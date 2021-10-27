package ru.praktika95.bot;

import java.sql.*;

public class DataBase {
    private Statement statement;
    private Connection connection;

    public DataBase() {}

    public void connect(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
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
                "photo_event varchar(30) not null," +
                "name_event varchar(30) not null," +
                "date_event varchar(30) not null," +
                "place_event varchar(30) not null," +
                "price_event varchar(30) not null," +
                "url_event varchar(30) not null)");
    }

    public void deleteTable() throws SQLException {
        statement.executeUpdate("DROP TABLE users");
    }

    public void create(int chat_id, String date_notice, String photo_event, String name_event, String date_event,
            String place_event, String price_event, String url_event) throws SQLException {
        PreparedStatement ps = connection.prepareStatement( "INSERT INTO users (" +
                "chat_id, date_notice, photo_event, name_event," +
                "date_event, place_event, price_event, url_event)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)" );
        ps.setInt(1, chat_id);
        ps.setString(2, date_notice);
        ps.setString(3, photo_event);
        ps.setString(4, name_event);
        ps.setString(5, date_event);
        ps.setString(6, place_event);
        ps.setString(7, price_event);
        ps.setString(8, url_event);
        ps.executeUpdate();
        ps.close();
    }

    public void read(int id) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE id=?");
            ps.setInt( 1, id );
            ps.executeUpdate();
            ps.close();
        } catch( SQLException e ) {
            e.printStackTrace();
        }
    }

    public void update(String date_notice, int chat_id) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE users SET date_notice=? WHERE chat_id=?");
            ps.setString( 1, date_notice );
            ps.setInt( 2, chat_id );
            ps.executeUpdate();
            ps.close();
        } catch( SQLException e ) {
            e.printStackTrace();
        }
    }

    public void delete(int chat_id, String name_event) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE chat_id=? AND name_event=?");
            ps.setInt( 1, chat_id );
            ps.setString( 2, name_event);
            ps.executeUpdate();
            ps.close();
        } catch( SQLException e ) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM users WHERE id=?");
            ps.setInt( 1, id );
            ps.executeUpdate();
            ps.close();
        } catch( SQLException e ) {
            e.printStackTrace();
        }
    }
}
