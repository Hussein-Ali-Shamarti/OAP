package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    public MenuBar(boolean includeExtendedMenuItems) {
        this.includeExtendedMenuItems = includeExtendedMenuItems;
        // Initialize the menu bar
        menuBar = new JMenuBar();

        // Create a File menu
        fileMenu = new JMenu("File");

        // Add items to the File menu
        testDatabaseConnectionItem = new JMenuItem("Test Database Connection");
        sqlQueryItem = new JMenuItem("SQL Query");
        exitMenuItem = new JMenuItem("Exit");

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
        saveItem.addActionListener(new SaveListener());
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

    // Action listener for saving data
    private class SaveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Add your logic to save data here
            JOptionPane.showMessageDialog(null, "Data saved successfully!");
        }
    }

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
}