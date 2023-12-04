package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.OrderHandler;
import database.DataBaseConnection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * The MenuBar class represents the menu bar for the main application window.
 * It provides various menu items and actions for interacting with the application.
 * 
 * @author 7120
 */

public class MenuBar {

	/**
	 * Represents the main menu bar of the application's user interface.
	 */
	protected JMenuBar menuBar;

	/**
	 * Represents the "File" menu in the application's user interface.
	 */
	protected JMenu fileMenu;

	/**
	 * Represents the "Test Database Connection" menu item in the "File" menu.
	 */
	protected JMenuItem testDatabaseConnectionItem;

	/**
	 * Represents the "SQL Query" menu item in the "File" menu.
	 */
	protected JMenuItem sqlQueryItem;

	/**
	 * Represents the "Exit" menu item in the "File" menu.
	 */
	protected JMenuItem exitMenuItem;

	/**
	 * Represents the "About" menu in the application's user interface.
	 */
	protected JMenu aboutMenu;

	/**
	 * Represents the "About" menu item in the "About" menu.
	 */
	protected JMenuItem aboutMenuItem;

	/**
	 * Represents the "Help" menu in the application's user interface.
	 */
	protected JMenu helpMenu;

	/**
	 * Represents the "Help" menu item in the "Help" menu.
	 */
	protected JMenuItem helpMenuItem;

	/**
	 * A boolean flag indicating whether to include extended menu items.
	 */
	protected boolean includeExtendedMenuItems;

    
    /**
     * Constructs a MenuBar with the specified inclusion of extended menu items.
     *
     * @param includeExtendedMenuItems True if extended menu items should be included; otherwise, false.
     */
    
    public MenuBar(boolean includeExtendedMenuItems) {
        this.includeExtendedMenuItems = includeExtendedMenuItems;
     
        menuBar = new JMenuBar();

       
        fileMenu = new JMenu("file");

  
        testDatabaseConnectionItem = new JMenuItem("Test Database Connection");
        testDatabaseConnectionItem.addActionListener(new TestConnectionListener());
        sqlQueryItem = new JMenuItem("SQL Query");
        sqlQueryItem.addActionListener(new SQLQueryListener()); 
        exitMenuItem = new JMenuItem("Exit");
       

        if (includeExtendedMenuItems) {
            addExtendedMenuItems();
        }

        fileMenu.add(testDatabaseConnectionItem);
        fileMenu.add(sqlQueryItem);
        fileMenu.add(exitMenuItem);

        aboutMenu = new JMenu("About");
        aboutMenuItem = createMenuItem("About", "About.txt");
        aboutMenu.add(aboutMenuItem);

        helpMenu = new JMenu("Help");
        helpMenuItem = createMenuItem("Help", "Help.txt");
        helpMenu.add(helpMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        menuBar.add(helpMenu);
        
    }

    /**
     * Gets the menu bar associated with this MenuBar.
     *
     * @return The JMenuBar instance.
     */
    
    public JMenuBar getMenuBar() {
        return menuBar;
    }
    
    /**
     * Adds extended menu items, if included, to the File menu.
     */

    private void addExtendedMenuItems() {
        
        JMenuItem uploadCsv = new JMenuItem("Upload Csv");
       
       uploadCsv.addActionListener(new UploadCsvListener());
        
        fileMenu.add(uploadCsv);
    }
    
    /**
     * Creates a menu item with the specified text and associated file content.
     *
     * @param text     The text to display on the menu item.
     * @param filePath The path to the file containing the content.
     * @return The created JMenuItem.
     */

    private JMenuItem createMenuItem(String text, String filePath) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String content = readTextFromFile(filePath);
                    JOptionPane.showMessageDialog(null, content, text, JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return menuItem;
    }

    /**
     * Reads the content of a text file located at the specified filePath.
     *
     * @param filePath The path to the text file.
     * @return The content of the text file as a String.
     * @throws IOException If an error occurs while reading the file.
     */
    
    private String readTextFromFile(String filePath) throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/documents/" + filePath)) {
            if (inputStream != null) {
                byte[] bytes = inputStream.readAllBytes();
                return new String(bytes);
            } else {
                throw new IOException("File not found: " + filePath);
            }
        }
    }

    /**
     * ActionListener for the "Upload CSV" menu item.
     * Allows users to select a CSV file for bulk import and notifies about the import result.
     */
   
    private class UploadCsvListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a CSV file for bulk import");
            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                try {
                    OrderHandler orderHandler = new OrderHandler(null, null);
                    boolean success = orderHandler.importOrders(selectedFile);

                    if (success) {
                        JOptionPane.showMessageDialog(null, "Orders imported successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to import orders.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "An error occurred during the import: " + ex.getMessage());
                }
            }
        }
    }
    
    /**
     * ActionListener for the "Test Database Connection" menu item.
     * Attempts to establish a database connection and displays a message box indicating the result.
     */
    
    private class TestConnectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Connection conn = DataBaseConnection.getConnection();
                JOptionPane.showMessageDialog(null, "Database connection successful!");
                conn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Failed to connect to the database: " + ex.getMessage());
            }
        }
    }
    
    /**
     * ActionListener for the "SQL Query" menu item.
     * Displays a dialog for entering an SQL query and executes it. Displays the results or errors in a message box.
     */
    
    private class SQLQueryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextArea queryInput = new JTextArea(10, 40);
            queryInput.setWrapStyleWord(true);
            queryInput.setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(queryInput);

            int option = JOptionPane.showConfirmDialog(null, scrollPane, "Enter SQL Query",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (option == JOptionPane.OK_OPTION) {
                String sql = queryInput.getText();
                executeSqlQuery(sql);
            }
        }

        /**
         * Executes an SQL query and displays the results or errors in a message box.
         *
         * @param sql The SQL query to execute.
         */
        
        private void executeSqlQuery(String sql) {
            try (Connection conn = DataBaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                 
                if (sql.trim().toUpperCase().startsWith("SELECT")) {
                    executeSelectQuery(stmt, sql);
                } else {
                    executeActionQuery(stmt, sql);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "SQL Error: " + ex.getMessage());
            }
        }
        
        /**
         * Executes a SELECT SQL query and displays the results in a table.
         *
         * @param stmt The Statement object to execute the query.
         * @param sql  The SELECT SQL query.
         * @throws SQLException If a database error occurs.
         */

        private void executeSelectQuery(Statement stmt, String sql) throws SQLException {
            ResultSet rs = stmt.executeQuery(sql);
            JTable table = new JTable(buildTableModel(rs));
            JOptionPane.showMessageDialog(null, new JScrollPane(table));
        }
        
        /**
         * Executes a non-SELECT SQL query and displays the number of affected rows.
         *
         * @param stmt The Statement object to execute the query.
         * @param sql  The non-SELECT SQL query.
         * @throws SQLException If a database error occurs.
         */


        private void executeActionQuery(Statement stmt, String sql) throws SQLException {
            int affectedRows = stmt.executeUpdate(sql);
            JOptionPane.showMessageDialog(null, affectedRows + " rows affected.");
        }

        /**
         * Builds a DefaultTableModel from a ResultSet, allowing the data to be displayed in a JTable.
         *
         * @param rs The ResultSet containing query results.
         * @return A DefaultTableModel representing the data from the ResultSet.
         * @throws SQLException If a database error occurs while processing the ResultSet.
         */
        private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
            ResultSetMetaData metaData = rs.getMetaData();
            Vector<String> columnNames = new Vector<>();
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

            return new DefaultTableModel(data, columnNames);
        }

    }
}