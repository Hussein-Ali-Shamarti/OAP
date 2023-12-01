package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import model.ProductDAO;
import view.ProductView;

// Action listener for "Delete" button
public class DeleteProductButtonListener implements ActionListener {

    private ProductView productView;
    private ProductDAO productDAO;

    public DeleteProductButtonListener(ProductView productView, ProductDAO productDAO) {
        this.productView = productView;
        this.productDAO = productDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        List<String> productCodesToDelete = productView.gatherUserInputForDelete();

        if (productCodesToDelete != null && !productCodesToDelete.isEmpty()) {
            boolean success = productDAO.deleteProducts(productCodesToDelete);

            if (success) {
                JOptionPane.showMessageDialog(productView, "Product(s) deleted successfully.");
                productView.fetchAndDisplayProducts();
            } else {
                JOptionPane.showMessageDialog(productView, "Failed to delete product(s).", "Delete Product", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}