package database;



/**
 * Class that provides details for the DB connectivity
 * It assumes that you have proper MySQL JDBC driver:
 * 
 * How to use this class?
 * 
 * 1. Create new object
 *  DBHelper db = new DBHelper();
 * 2. Open connection
 *  db.open();
 * 3. Call corresponding method
 *  db.test();
 * 4. Close connection
 *  db.close;
  
 * jdbc driver can be found at: http://www.java2s.com/Code/Jar/c/Downloadcommysqljdbc515jar.htm
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataBaseConnection implements ConnectionInterface {
	
	 protected static String dbUrl = "jdbc:mysql://127.0.0.1:3306/classicmodels";
	    
	 
	 protected static String user = "student";
	 protected static String password = "student";



	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement prepStmt = null;
	private ResultSet rSet = null;

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
}

