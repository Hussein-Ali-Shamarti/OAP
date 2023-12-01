package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;


import model.ProductDAO;
import view.ProductView;



//Action listener for "Delete" button
public class DeleteProductButtonListener implements ActionListener {

    private ProductView productView;
    private ProductDAO productDAO;

    public DeleteProductButtonListener(ProductView productView, ProductDAO productDAO) {
        this.productView = productView;
        this.productDAO = productDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTable table = productView.getTable();
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(productView, "Please select a product to delete.", "Delete Product", JOptionPane.WARNING_MESSAGE);
        } else {
            int confirmResult = JOptionPane.showConfirmDialog(productView, "Are you sure you want to delete selected product(s)?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirmResult == JOptionPane.YES_OPTION) {
                List<String> productCodes = getProductCodesFromRows(selectedRows);

                boolean success = productDAO.deleteProducts(productCodes);

                if (success) {
                    JOptionPane.showMessageDialog(productView, "Product(s) deleted successfully.");
                    productView.fetchAndDisplayProducts();
                } else {
                    JOptionPane.showMessageDialog(productView, "Failed to delete product(s).", "Delete Product", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private List<String> getProductCodesFromRows(int[] selectedRows) {
        JTable table = productView.getTable();
        List<String> productCodes = new ArrayList<>();

        for (int rowIndex : selectedRows) {
            String productCode = (String) table.getValueAt(rowIndex, 0);
            productCodes.add(productCode);
        }

        return productCodes;
    }
}