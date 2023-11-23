package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.DataBaseConnection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;



public class MenuBar {

    protected JMenuBar menuBar;
    protected JMenu fileMenu;
    protected JMenuItem testDatabaseConnectionItem;
    protected JMenuItem sqlQueryItem;
    protected JMenuItem exitMenuItem;
    protected JMenu aboutMenu;
    protected JMenuItem aboutMenuItem;
    protected JMenu helpMenu;
    protected JMenuItem helpMenuItem;
    protected boolean includeExtendedMenuItems; // Flag to include extended menu items
    private JFrame parentFrame; //dont remove it!!

    public MenuBar(JFrame parentFrame, boolean includeExtendedMenuItems) {
        this.setParentFrame(parentFrame);
        this.includeExtendedMenuItems = includeExtendedMenuItems;
        // Initialize the menu bar
        menuBar = new JMenuBar();

        // Create a File menu
        fileMenu = new JMenu("File");

        // Add items to the File menu
        testDatabaseConnectionItem = new JMenuItem("Test Database Connection");
        testDatabaseConnectionItem.addActionListener(new TestConnectionListener());
        sqlQueryItem = new JMenuItem("SQL Query");
        sqlQueryItem.addActionListener(new SQLQueryListener());

        exitMenuItem = new JMenuItem("Exit");
        
        // Modify the ActionListener for exitMenuItem
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (parentFrame != null) {
                    parentFrame.dispose(); // Use the parentFrame here
                }
            }
        });


        // Add extended menu items if the flag is true
        if (includeExtendedMenuItems) {
            addExtendedMenuItems();
        }

        // Add the rest of the items to the File menu
        fileMenu.add(testDatabaseConnectionItem);
        fileMenu.add(sqlQueryItem);
        fileMenu.add(exitMenuItem);
        

        // Create an About menu
        aboutMenu = new JMenu("About");
        aboutMenuItem = createMenuItem("About", "About.txt");
        aboutMenu.add(aboutMenuItem);

        // Create a Help menu
        helpMenu = new JMenu("Help");
        helpMenuItem = createMenuItem("Help", "Help.txt");
        helpMenu.add(helpMenuItem);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        menuBar.add(helpMenu);
        
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }

    private void addExtendedMenuItems() {
        // Add extended menu items
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveToFileItem = new JMenuItem("Save to File");

        // Add action listeners
//        saveItem.addActionListener(new SaveListener()); 
        saveToFileItem.addActionListener(new SaveToFileListener());

        // Add items to the File menu
        fileMenu.add(saveItem);
        fileMenu.add(saveToFileItem);
    }

    private JMenuItem createMenuItem(String text, String filePath) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Read text from file and display it in a JOptionPane
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

    public JFrame getParentFrame() {
		return parentFrame;
	}

	public void setParentFrame(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

	// Action listener for saving data
//    private class SaveListener implements ActionListener {
//        
//       
//    }

    // Action listener for saving to a file
    private class SaveToFileListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Add your logic to save data to a file here
            // For demonstration purposes, let's write a sample file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
                writer.write("This is a sample file content.");
                JOptionPane.showMessageDialog(null, "File saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private class TestConnectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Try to get a connection and show a message depending on whether it was successful
            try {
                Connection conn = DataBaseConnection.getConnection();
                // If no exception is thrown, the connection is successful
                JOptionPane.showMessageDialog(null, "Database connection successful!");
                // Don't forget to close the connection when done
                conn.close();
            } catch (SQLException ex) {
                // If there's an exception, the connection failed
                JOptionPane.showMessageDialog(null, "Failed to connect to the database: " + ex.getMessage());
            }
        }
    }
    
 // ActionListener for sqlQueryItem
    private class SQLQueryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a text area for user input
            JTextArea queryInput = new JTextArea(10, 40);
            queryInput.setWrapStyleWord(true);
            queryInput.setLineWrap(true);
            JScrollPane scrollPane = new JScrollPane(queryInput);

            // Show dialog with text area
            int option = JOptionPane.showConfirmDialog(null, scrollPane, "Enter SQL Query",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // If user clicks OK, process the query
            if (option == JOptionPane.OK_OPTION) {
                String sql = queryInput.getText();
                executeSqlQuery(sql);
            }
        }
        
        

        private void executeSqlQuery(String sql) {
            try (Connection conn = DataBaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                 
                // Check if the query is a SELECT or an action query (INSERT/UPDATE/DELETE)
                if (sql.trim().toUpperCase().startsWith("SELECT")) {
                    executeSelectQuery(stmt, sql);
                } else {
                    executeActionQuery(stmt, sql);
                }
            } catch (SQLException ex) {
                // Handle any SQL errors
                JOptionPane.showMessageDialog(null, "SQL Error: " + ex.getMessage());
            }
        }

        private void executeSelectQuery(Statement stmt, String sql) throws SQLException {
            ResultSet rs = stmt.executeQuery(sql);
            // Convert ResultSet to a more friendly format, like a JTable
            JTable table = new JTable(buildTableModel(rs));
            JOptionPane.showMessageDialog(null, new JScrollPane(table));
        }

        private void executeActionQuery(Statement stmt, String sql) throws SQLException {
            int affectedRows = stmt.executeUpdate(sql);
            // Inform the user of the result
            JOptionPane.showMessageDialog(null, affectedRows + " rows affected.");
        }

        private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
            ResultSetMetaData metaData = rs.getMetaData();
            // Names of columns
            Vector<String> columnNames = new Vector<>();
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            // Data of the table
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