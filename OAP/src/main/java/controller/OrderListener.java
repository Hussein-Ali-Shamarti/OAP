package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import view.OrderView;

public class OrderListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Open OrderView when "Orders" button is pressed
        SwingUtilities.invokeLater(() -> {
            OrderView orderView = new OrderView();
            orderView.setVisible(true);
           
        });
    }
}