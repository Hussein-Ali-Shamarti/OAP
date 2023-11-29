package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import view.ProductView;

// Static inner class for "Products" button in MainMenu
public class ProductListener implements ActionListener {
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