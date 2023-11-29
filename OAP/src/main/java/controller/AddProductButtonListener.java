package controller;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import database.DataBaseConnection;
import model.ProductDAO;
import model.Products;
import view.ProductView;


public class AddProductButtonListener implements ActionListener {
	private final ProductView productView;

    public AddProductButtonListener(ProductView productView) {
        this.productView = productView;
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // Create an instance of ProductHandler
        ProductDAO productHandler = new ProductDAO();

        // Implement the logic for the "Add" button here
        // For example, you can open a dialog to input product details
        // and then call the addProduct method on the productHandler instance
        // Here's a simple example using JOptionPane for input:
        JTextField productCodeField = new JTextField(10);
        JTextField productNameField = new JTextField(20);
        JTextField productLineField = new JTextField(20);
        JTextField productScaleField = new JTextField(10);
        JTextField productVendorField = new JTextField(20);
        JTextField productDescriptionField = new JTextField(20);
        JTextField quantityInStockField = new JTextField(5);
        JTextField buyPriceField = new JTextField(10);
        JTextField msrpField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Product Code:"));
        panel.add(productCodeField);
        panel.add(new JLabel("Product Name:"));
        panel.add(productNameField);
        panel.add(new JLabel("Product Line:"));
        panel.add(productLineField);
        panel.add(new JLabel("Product Scale:"));
        panel.add(productScaleField);
        panel.add(new JLabel("Product Vendor:"));
        panel.add(productVendorField);
        panel.add(new JLabel("Product Description:"));
        panel.add(productDescriptionField);
        panel.add(new JLabel("Quantity in Stock:"));
        panel.add(quantityInStockField);
        panel.add(new JLabel("Buy Price:"));
        panel.add(buyPriceField);
        panel.add(new JLabel("MSRP:"));
        panel.add(msrpField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Enter Product Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String productCode = productCodeField.getText();
                String productName = productNameField.getText();
                String productLine = productLineField.getText();
                String productScale = productScaleField.getText();
                String productVendor = productVendorField.getText();
                String productDescription = productDescriptionField.getText();
                int quantityInStock = Integer.parseInt(quantityInStockField.getText());
                double buyPrice = Double.parseDouble(buyPriceField.getText());
                double msrp = Double.parseDouble(msrpField.getText());

                // Check if the entered product line exists in the database
                if (!isProductLineExists(productLine)) {
                    JOptionPane.showMessageDialog(productView, "Product line doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit without adding the product
                }

                // Create a Products object with the entered details
                Products product = new Products(productCode, productName, productLine, productScale, productVendor,
                        productDescription, quantityInStock, buyPrice, msrp);

                // Call the addProduct method on the productHandler instance
                boolean success = productHandler.addProduct(product);

                if (success) {
                    JOptionPane.showMessageDialog(productView, "Product added successfully!");
                    // Refresh the product list or take any other necessary action
                } else {
                    JOptionPane.showMessageDialog(productView, "Failed to add product.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(productView, "Invalid input format.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(productView, "Error: " + ex.getMessage());
            }
        }
    }

    // Check if the entered product line exists in the database
    private boolean isProductLineExists(String productLine) {
        try {
            // Assuming you have a database connection established, create a PreparedStatement
            PreparedStatement preparedStatement = DataBaseConnection.prepareStatement("SELECT COUNT(*) FROM productlines WHERE productLine = ?");
            preparedStatement.setString(1, productLine);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if a row with the product line exists
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true; // Product line exists
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Handle the exception properly in your code
        }

        return false; // Product line doesn't exist or an error occurred
    }
}

