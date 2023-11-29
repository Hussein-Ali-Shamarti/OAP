package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import view.CustomerView;

/**
 * ActionListener for handling the "Customers" button action.
 * Opens the CustomerView when the button is pressed.
 */
public class CustomerListener implements ActionListener {

    /**
     * Invoked when the "Customers" button is pressed.
     * Opens the CustomerView.
     *
     * @param e The ActionEvent representing the button press.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Open CustomerView when "Customers" button is pressed
        SwingUtilities.invokeLater(() -> {
            CustomerView customerView = new CustomerView();
            customerView.setVisible(true);
        });
    }
}