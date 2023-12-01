package controller;

import model.ProductDAO;
import model.Products;
import view.ProductView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateProductButtonListener implements ActionListener {
    private ProductView productView;
    private ProductDAO productDAO;

    public UpdateProductButtonListener(ProductView productView, ProductDAO productDAO) {
        this.productView = productView;
        this.productDAO = productDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String productCodeToUpdate = JOptionPane.showInputDialog(productView, "Enter Product Code to update:");

        if (productCodeToUpdate != null && !productCodeToUpdate.isEmpty()) {
            Products existingProduct = productDAO.fetchProductFromDatabase(productCodeToUpdate);

            if (existingProduct != null) {
                Products updatedProduct = productView.gatherUserInputForUpdate(existingProduct);

                if (updatedProduct != null) {
                    boolean success = productDAO.updateProduct(updatedProduct);

                    if (success) {
                        JOptionPane.showMessageDialog(productView, "Product updated successfully!");
                        productView.fetchAndDisplayProducts(); // Update the table with the new data
                    } else {
                        JOptionPane.showMessageDialog(productView, "Failed to update product.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(productView, "Product not found.");
            }
        }
    }
}