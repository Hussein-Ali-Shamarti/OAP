package app;

import javax.swing.SwingUtilities;
import view.MainView;

/**
 * The main entry point for the application.
 */
public class MainApp {
    /**
     * The main method that starts the application.
     * Instance of the mainview
     * @param args The command-line arguments (not used in our application).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}


               
