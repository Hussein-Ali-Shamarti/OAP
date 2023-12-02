/**
 * Represents an order entity with information such as order number, dates, status, comments,
 * customer number, and associated order date.
 * 
 * <p>Orders may also contain order details, which are not implemented in this version.</p>
 * 
 * @author Hussein
 * @version 07.11.2023
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import view.OrderView;

/**
 * ActionListener for handling events related to orders.
 * Opens the {@link OrderView} when the "Orders" button is pressed.
 * Uses {@link SwingUtilities#invokeLater(Runnable)} to ensure the GUI is updated on the Event Dispatch Thread.
 */
public class OrderListener implements ActionListener {

    /**
     * Invoked when an action occurs, in this case, opens the {@link OrderView}.
     *
     * @param e The event representing the user's action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Open OrderView when "Orders" button is pressed
        SwingUtilities.invokeLater(() -> {
            OrderView orderView = new OrderView();
            orderView.setVisible(true);
        });
    }
}