package App;

import javax.swing.SwingUtilities;

import view.MainView;

public class MainApp {

public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainView();
            }
        });
    }

}