package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBaseConnection {
    protected static String dbUrl = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11665772";
    protected static String user = "sql11665772";
    protected static String password = "m5kZbc2aMA";

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
