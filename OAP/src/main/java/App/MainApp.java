package App;

import javax.swing.SwingUtilities;

import view.MainView;



public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}

               