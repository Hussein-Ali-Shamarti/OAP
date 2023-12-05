package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Provides methods for establishing a connection to the database and preparing SQL statements.
 * This class manages the database connection details and offers utility methods for database operations.
 * 
 * @author 7094
 */

public class DataBaseConnection {
    /**
     * The URL of the database.
     */
    protected static String dbUrl = "jdbc:mysql://127.0.0.1:3306/classicmodels";

    /**
     * The username used to connect to the database.
     */
    protected static String user = "student";

    /**
     * The password used to connect to the database.
     */
    protected static String password = "student";

    /**
     * Establishes and returns a connection to the database.
     *
     * @return A Connection object to the database.
     * @throws SQLException If a database access error occurs or the url is null.
     */
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, user, password);
    }

    /**
     * Retrieves the database URL.
     *
     * @return The URL of the database.
     */
    
    public static String getDbUrl() {
        return dbUrl;
    }

    /**
     * Retrieves the database user name.
     *
     * @return The user name for the database.
     */
    
    public static String getUser() {
        return user;
    }

    /**
     * Gets the database connection password.
     *
     * @return The password used to connect to the database.
     */
    
    public static String getPassword() {
        return password;
    }

    /**
     * Prepares and returns a SQL statement for the given SQL query.
     *
     * @param sql The SQL query to prepare a statement for.
     * @return A PreparedStatement object for executing the SQL query.
     * @throws SQLException If a database access error occurs or the SQL statement is invalid.
     */
    
    public static PreparedStatement prepareStatement(String sql) throws SQLException {
        Connection connection = getConnection(); // Implement this method to obtain the database connection
        return connection.prepareStatement(sql);
    }
}
