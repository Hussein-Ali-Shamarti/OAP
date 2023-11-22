package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MenuBar {

    protected JMenuBar menuBar;
    protected JMenu fileMenu;
    protected JMenuItem exitMenuItem;
    protected JMenu aboutMenu;
    protected JMenuItem aboutMenuItem;
    protected JMenu helpMenu;
    protected JMenuItem helpMenuItem;

    public MenuBar() {
        // Initialize the menu bar
        menuBar = new JMenuBar();

        // Create a File menu
        fileMenu = new JMenu("File");
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));
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
}