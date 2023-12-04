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
 * @version 2.12.2023
 */

public class DataBaseConnection {
    /**
     * The URL of the database.
     */
    protected static String dbUrl = "jdbc:mysql://sql11.freesqldatabase.com:3306/sql11665772";

    /**
     * The username used to connect to the database.
     */
    protected static String user = "sql11665772";

    /**
     * The password used to connect to the database.
     */
    protected static String password = "m5kZbc2aMA";

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
        Connection connection = getConnection(); 
        return connection.prepareStatement(sql);
    }
}
