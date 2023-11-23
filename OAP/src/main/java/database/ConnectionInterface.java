
/**
 * Declaration of the methods for establishing database connection the interface
 * provides all necessary methods that should be implemented to achieve
 * successful connection to a database
 */

package database;

import java.sql.SQLException;
import java.util.List;




public interface ConnectionInterface {
	
	

		/**
		 * Open connection
		 * 
		 * @throws SQLException
		 */
		void open() throws SQLException;

		/**
		 * Close connection
		 * 
		 * @throws SQLException
		 */
		void close() throws SQLException;

		/**
		 * Test connection
		 * 
		 * @throws SQLException
		 */
		void test() throws SQLException;

}
