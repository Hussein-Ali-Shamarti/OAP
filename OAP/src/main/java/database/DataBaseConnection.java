package database;

import java.sql.Connection;
import java.sql.DriverManager;
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

<<<<<<< HEAD
	@Override
	public void open() throws SQLException {
		try {
			// Establish connection
			conn = DriverManager.getConnection(dbUrl, user, password);
			// Create statement that will be used for executing SQL queries
			stmt = conn.createStatement();
		} catch (SQLException ex) {
			ex.printStackTrace();// More elegant solutions for catching errors exist but they are out of the
									// scope for this course
		}
	}

	@Override
	public void close() throws SQLException {
		try {
			stmt.close();
			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void test() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public static Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}
=======
    public static String getPassword() {
        return password;
    }
>>>>>>> 5058866d668a794f8f88eb5a5431194758f8bb55
}

