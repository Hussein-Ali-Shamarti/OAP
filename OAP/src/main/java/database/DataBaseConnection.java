package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseConnection {
    protected static String dbUrl = "jdbc:mysql://127.0.0.1:3306/classicmodels";
    protected static String user = "student";
    protected static String password = "student";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, user, password);
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static String getUser() {
        return user;
    }


    public static String getPassword() {
        return password;
    }

    // Create a PreparedStatement
    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        Connection connection = getConnection(); // Implement this method to obtain the database connection
        return connection.prepareStatement(sql);
    }
}
