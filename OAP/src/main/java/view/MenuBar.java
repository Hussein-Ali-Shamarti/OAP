package view;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar {

    protected JMenuBar menuBar;
    protected JMenu fileMenu;
    protected JMenuItem exitMenuItem;

    public MenuBar() {
        // Initialize the menu bar
        menuBar = new JMenuBar();

        // Create a File menu
        fileMenu = new JMenu("File");

        // Create an Exit menu item
        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(e -> System.exit(0));

        // Add the Exit menu item to the File menu
        fileMenu.add(exitMenuItem);

        // Add the File menu to the menu bar
        menuBar.add(fileMenu);
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}