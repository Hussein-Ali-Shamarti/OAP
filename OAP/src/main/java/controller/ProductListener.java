package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import view.ProductView;

/**
 * ActionListener for handling events related to products.
 * Opens the {@link ProductView} when the "Products" button is pressed.
 * Uses {@link SwingUtilities#invokeLater(Runnable)} to ensure the GUI is updated on the Event Dispatch Thread.
 */
public class ProductListener implements ActionListener {

    /**
     * Invoked when an action occurs, in this case, opens the {@link ProductView}.
     *
     * @param e The event representing the user's action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Open ProductView when "Products" button is pressed
        SwingUtilities.invokeLater(() -> {
            // Create ProductView and set it visible
            ProductView productView = new ProductView();
            productView.setVisible(true);
        });
    }
}