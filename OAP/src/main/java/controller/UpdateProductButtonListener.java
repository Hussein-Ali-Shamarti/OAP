package controller;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import database.DataBaseConnection;
import model.Products;
import view.ProductView;

public class UpdateProductButtonListener implements ActionListener {
	private ProductView productView;

	public UpdateProductButtonListener(ProductView productView) {
        this.productView = productView;
    }
	
    @Override
    public void actionPerformed(ActionEvent e) {
        // Get the selected row from the table
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(productView, "Please select a product to update.");
            return;
        }
   
        String productCode = table.getValueAt(selectedRow, 0).toString();
        Products productToUpdate = fetchProductFromDatabase(productCode);

        if (productToUpdate == null) {
            JOptionPane.showMessageDialog(productView, "Product not found in the database.");
            return;
        }

        JTextField productNameField = new JTextField(productToUpdate.getProductName());
        JTextField productLineField = new JTextField(productToUpdate.getProductLine());
        JTextField productScaleField = new JTextField(productToUpdate.getProductScale());
        JTextField productVendorField = new JTextField(productToUpdate.getProductVendor());
        JTextArea productDescriptionArea = new JTextArea(productToUpdate.getProductDescription(), 5, 20);
        productDescriptionArea.setLineWrap(true);
        productDescriptionArea.setWrapStyleWord(true);
        JScrollPane productDescriptionScrollPane = new JScrollPane(productDescriptionArea);
        JTextField quantityInStockField = new JTextField(String.valueOf(productToUpdate.getQuantityInStock()));
        JTextField buyPriceField = new JTextField(String.valueOf(productToUpdate.getBuyPrice()));
        JTextField msrpField = new JTextField(String.valueOf(productToUpdate.getMsrp()));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        panel.add(new JLabel("Product Name:"), gbc);
        panel.add(productNameField, gbc);
        panel.add(new JLabel("Product Line:"), gbc);
        panel.add(productLineField, gbc);
        panel.add(new JLabel("Product Scale:"), gbc);
        panel.add(productScaleField, gbc);
        panel.add(new JLabel("Product Vendor:"), gbc);
        panel.add(productVendorField, gbc);
        panel.add(new JLabel("Product Description:"), gbc);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        panel.add(productDescriptionScrollPane, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        panel.add(new JLabel("Quantity in Stock:"), gbc);
        panel.add(quantityInStockField, gbc);
        panel.add(new JLabel("Buy Price:"), gbc);
        panel.add(buyPriceField, gbc);
        panel.add(new JLabel("MSRP:"), gbc);
        panel.add(msrpField, gbc);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Product Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                productToUpdate.setProductName(productNameField.getText());
                productToUpdate.setProductLine(productLineField.getText());
                productToUpdate.setProductScale(productScaleField.getText());
                productToUpdate.setProductVendor(productVendorField.getText());
                productToUpdate.setProductDescription(productDescriptionArea.getText());
                productToUpdate.setQuantityInStock(Integer.parseInt(quantityInStockField.getText()));
                productToUpdate.setBuyPrice(Double.parseDouble(buyPriceField.getText()));
                productToUpdate.setMsrp(Double.parseDouble(msrpField.getText()));

                boolean success = updateProductInDatabase(productToUpdate);

                if (success) {
                    JOptionPane.showMessageDialog(productView, "Product updated successfully!");
                    // Add your method to refresh the product list if you have one
                } else {
                    JOptionPane.showMessageDialog(productView, "Failed to update product.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(productView, "Invalid input format.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(productView, "Error: " + ex.getMessage());
            }
        }
    }
    
 // Update product details in the database
    private boolean updateProductInDatabase(Products product) {
        try {
            // Assuming you have a connection to the database
            Connection conn = DataBaseConnection.getConnection();
            
            // Define your SQL update query to update the product details
            String sql = "UPDATE products SET productName = ?, productLine = ?, productScale = ?, productVendor = ?, " +
                    "productDescription = ?, quantityInStock = ?, buyPrice = ?, msrp = ? WHERE productCode = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set the updated product details as parameters in the SQL query
                pstmt.setString(1, product.getProductName());
                pstmt.setString(2, product.getProductLine());
                pstmt.setString(3, product.getProductScale());
                pstmt.setString(4, product.getProductVendor());
                pstmt.setString(5, product.getProductDescription());
                pstmt.setInt(6, product.getQuantityInStock());
                pstmt.setDouble(7, product.getBuyPrice());
                pstmt.setDouble(8, product.getMsrp());
                pstmt.setString(9, product.getProductCode());
                
                // Execute the update query
                int affectedRows = pstmt.executeUpdate();
                
                // Check if the update was successful
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Return false if the update failed or an error occurred
        return false;
    }
}