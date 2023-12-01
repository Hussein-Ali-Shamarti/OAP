package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import model.ProductDAO;
import model.Products;
import view.ProductView;

public class AddProductButtonListener implements ActionListener {
    private final ProductView productView;
    private final ProductDAO productDAO;

    public AddProductButtonListener(ProductView productView, ProductDAO productDAO) {
        this.productView = productView;
        this.productDAO = productDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Products product = productView.gatherUserInputForProduct();

        if (product != null) {
            boolean success = productDAO.addProduct(product);

            if (success) {
                JOptionPane.showMessageDialog(productView, "Product added successfully!");
                // Refresh the product list or take any other necessary action
            } else {
                JOptionPane.showMessageDialog(productView, "Failed to add product.");
            }
        }
    }
}