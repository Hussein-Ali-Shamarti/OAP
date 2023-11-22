package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.ProductHandler;
import database.DataBaseConnection;
import model.Products;

public class ProductView extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField textField; // Assuming you have a JTextField for search

    public ProductView() {
        super("Product Management");

        setLayout(new BorderLayout());
        initializeUI();
        fetchAndDisplayProducts();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        pack(); // Adjusts the frame to fit the components
        setVisible(true); // Make sure the frame is visible
    }

    private void initializeUI() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131));
        JLabel titleLabel = new JLabel("Product Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        setupTable();
        setupControlPanel();

        add(titlePanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Set frame properties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center on screen
    }

    private void setupTable() {
        String[] columnNames = {"Product Code", "Product Name", "Product Scale", "Product Vendor",
                "Product Description", "Quantity In Stock", "Buy Price", "MSRP"};
        tableModel = new DefaultTableModel(null, columnNames) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        //customizeTableAppearance();//
    }

    private void setupControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        controlPanel.setBackground(new Color(90, 23, 139));

        JButton searchButton = createButton("Search",new SearchButtonListener());
        JButton addButton = createButton("Add", new AddButtonListener());
        JButton editButton = createButton("Edit", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete", new DeleteButtonListener());

        controlPanel.add(searchButton);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);

        JPanel buttonPanelHolder = new JPanel(new BorderLayout());
        buttonPanelHolder.add(controlPanel, BorderLayout.NORTH);
        buttonPanelHolder.add(Box.createVerticalStrut(10), BorderLayout.CENTER); // Add space
        this.add(buttonPanelHolder, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    void fetchAndDisplayProducts() {
        tableModel.setRowCount(0);
        try (Connection conn = database.DataBaseConnection.getConnection();
             Statement statement = conn.createStatement()) {
            String sql = "SELECT productCode, productName, productScale, productVendor, " +
                    "productDescription, quantityInStock, buyPrice, msrp FROM products";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getString("productCode"),
                        resultSet.getString("productName"),
                        resultSet.getString("productScale"),
                        resultSet.getString("productVendor"),
                        resultSet.getString("productDescription"),
                        resultSet.getInt("quantityInStock"),
                        resultSet.getDouble("buyPrice"),
                        resultSet.getDouble("msrp")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching product data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

 // Action listener for "Add" button
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create an instance of ProductHandler
            ProductHandler productHandler = new ProductHandler();

            // Implement the logic for the "Add" button here
            // For example, you can open a dialog to input product details
            // and then call the addProduct method on the productHandler instance
            // Here's a simple example using JOptionPane for input:
            JTextField productCodeField = new JTextField(10);
            JTextField productNameField = new JTextField(20);
            JTextField productScaleField = new JTextField(10);
            JTextField productVendorField = new JTextField(20);
            JTextField productDescriptionField = new JTextField(50);
            JTextField quantityInStockField = new JTextField(5);
            JTextField buyPriceField = new JTextField(10);
            JTextField msrpField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("Product Code:"));
            panel.add(productCodeField);
            panel.add(new JLabel("Product Name:"));
            panel.add(productNameField);
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
                    String productScale = productScaleField.getText();
                    String productVendor = productVendorField.getText();
                    String productDescription = productDescriptionField.getText();
                    int quantityInStock = Integer.parseInt(quantityInStockField.getText());
                    double buyPrice = Double.parseDouble(buyPriceField.getText());
                    double msrp = Double.parseDouble(msrpField.getText());

                    // Create a Products object with the entered details
                    Products product = new Products(productCode, productName, productScale, productVendor,
                            productDescription, quantityInStock, buyPrice, msrp);

                    // Call the addProduct method on the productHandler instance
                    boolean success = productHandler.addProduct(product);

                    if (success) {
                        JOptionPane.showMessageDialog(ProductView.this, "Product added successfully!");
                        // Refresh the product list or take any other necessary action
                    } else {
                        JOptionPane.showMessageDialog(ProductView.this, "Failed to add product.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductView.this, "Invalid input format.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProductView.this, "Error: " + ex.getMessage());
                }
            }
        }
    }



 // Action listener for "Update" button
    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the selected row from the table
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ProductView.this, "Please select a product to update.");
                return;
            }

            // Assuming that the first column (index 0) contains the product code
            String productCode = table.getValueAt(selectedRow, 0).toString();

            // You can fetch the product details from the database based on the product code
            Products productToUpdate = fetchProductFromDatabase(productCode);

            if (productToUpdate == null) {
                JOptionPane.showMessageDialog(ProductView.this, "Product not found in the database.");
                return;
            }

            // Now, you can open a dialog to allow the user to edit the product details
            JTextField productNameField = new JTextField(productToUpdate.getProductName());
            JTextField productScaleField = new JTextField(productToUpdate.getProductScale());
            JTextField productVendorField = new JTextField(productToUpdate.getProductVendor());
            JTextField productDescriptionField = new JTextField(productToUpdate.getProductDescription());
            JTextField quantityInStockField = new JTextField(String.valueOf(productToUpdate.getQuantityInStock()));
            JTextField buyPriceField = new JTextField(String.valueOf(productToUpdate.getBuyPrice()));
            JTextField msrpField = new JTextField(String.valueOf(productToUpdate.getMsrp()));

            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("Product Name:"));
            panel.add(productNameField);
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

            int result = JOptionPane.showConfirmDialog(null, panel, "Edit Product Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    // Update the product details based on user input
                    productToUpdate.setProductName(productNameField.getText());
                    productToUpdate.setProductScale(productScaleField.getText());
                    productToUpdate.setProductVendor(productVendorField.getText());
                    productToUpdate.setProductDescription(productDescriptionField.getText());
                    productToUpdate.setQuantityInStock(Integer.parseInt(quantityInStockField.getText()));
                    productToUpdate.setBuyPrice(Double.parseDouble(buyPriceField.getText()));
                    productToUpdate.setMsrp(Double.parseDouble(msrpField.getText()));

                    // Call the updateProductInDatabase method to update the product in the database
                    boolean success = updateProductInDatabase(productToUpdate);

                    if (success) {
                        JOptionPane.showMessageDialog(ProductView.this, "Product updated successfully!");
                        // Refresh the product list or take any other necessary action
                        fetchAndDisplayProducts(); // Refresh the product list
                    } else {
                        JOptionPane.showMessageDialog(ProductView.this, "Failed to update product.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ProductView.this, "Invalid input format.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(ProductView.this, "Error: " + ex.getMessage());
                }
            }
        }
    }

 // Fetch product details from the database based on the product code
    private Products fetchProductFromDatabase(String productCode) {
        try {
            // Assuming you have a connection to the database
            Connection conn = DataBaseConnection.getConnection();
            
            // Define your SQL query to fetch the product details based on the product code
            String sql = "SELECT * FROM products WHERE productCode = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set the product code as a parameter in the SQL query
                pstmt.setString(1, productCode);
                
                // Execute the query and retrieve the result set
                ResultSet resultSet = pstmt.executeQuery();
                
                // Check if a record was found
                if (resultSet.next()) {
                    // Retrieve the product details from the result set and create a Products object
                    String productName = resultSet.getString("productName");
                    String productScale = resultSet.getString("productScale");
                    String productVendor = resultSet.getString("productVendor");
                    String productDescription = resultSet.getString("productDescription");
                    int quantityInStock = resultSet.getInt("quantityInStock");
                    double buyPrice = resultSet.getDouble("buyPrice");
                    double msrp = resultSet.getDouble("msrp");
                    
                    Products product = new Products(productCode, productName, productScale, productVendor,
                            productDescription, quantityInStock, buyPrice, msrp);
                    
                    return product;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Return null if the product was not found or an error occurred
        return null;
    }

 // Update product details in the database
    private boolean updateProductInDatabase(Products product) {
        try {
            // Assuming you have a connection to the database
            Connection conn = DataBaseConnection.getConnection();
            
            // Define your SQL update query to update the product details
            String sql = "UPDATE products SET productName = ?, productScale = ?, productVendor = ?, " +
                    "productDescription = ?, quantityInStock = ?, buyPrice = ?, msrp = ? WHERE productCode = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                // Set the updated product details as parameters in the SQL query
                pstmt.setString(1, product.getProductName());
                pstmt.setString(2, product.getProductScale());
                pstmt.setString(3, product.getProductVendor());
                pstmt.setString(4, product.getProductDescription());
                pstmt.setInt(5, product.getQuantityInStock());
                pstmt.setDouble(6, product.getBuyPrice());
                pstmt.setDouble(7, product.getMsrp());
                pstmt.setString(8, product.getProductCode());
                
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

 // Action listener for "Search" button
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a dialog to input search criteria
            JTextField searchField = new JTextField(20);
            JPanel panel = new JPanel();
            panel.add(new JLabel("Search Products Code:"));
            panel.add(searchField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Search Products", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String searchCriteria = searchField.getText().trim();

                // Perform the search based on the user's input
                List<Products> searchResults = performSearch(searchCriteria);

                // Update the table with the search results
                updateTableWithSearchResults(searchResults);
            }
        }

        // Implement the logic to perform the search based on user input
        private List<Products> performSearch(String searchCriteria) {
            // Placeholder logic: You should implement the actual database search here
            // This code assumes you have a ProductHandler class to handle database operations
            ProductHandler productHandler = new ProductHandler();
            List<Products> searchResults = productHandler.searchProducts(searchCriteria);

            return searchResults;
        }

        // Update the table with the search results
        private void updateTableWithSearchResults(List<Products> searchResults) {
            tableModel.setRowCount(0); // Clear existing rows from the table

            // Populate the table with the search results
            for (Products product : searchResults) {
                Object[] row = {
                    product.getProductCode(),
                    product.getProductName(),
                    product.getProductScale(),
                    product.getProductVendor(),
                    product.getProductDescription(),
                    product.getQuantityInStock(),
                    product.getBuyPrice(),
                    product.getMsrp()
                };
                tableModel.addRow(row);
            }
        }
    }

    

 // Action listener for "Delete" button
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the selected row(s) in the table
            int[] selectedRows = table.getSelectedRows();

            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(ProductView.this, "Please select a product to delete.", "Delete Product", JOptionPane.WARNING_MESSAGE);
            } else {
                int confirmResult = JOptionPane.showConfirmDialog(ProductView.this, "Are you sure you want to delete selected product(s)?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirmResult == JOptionPane.YES_OPTION) {
                    // Delete the selected product(s) from the database
                    boolean success = deleteSelectedProducts(selectedRows);

                    if (success) {
                        JOptionPane.showMessageDialog(ProductView.this, "Product(s) deleted successfully.");
                        // Refresh the product list or take any other necessary action
                        fetchAndDisplayProducts(); // Refresh the product list after deletion
                    } else {
                        JOptionPane.showMessageDialog(ProductView.this, "Failed to delete product(s).", "Delete Product", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        // Implement the logic to delete selected products from the database
        private boolean deleteSelectedProducts(int[] selectedRows) {
            try {
                Connection conn = database.DataBaseConnection.getConnection();
                String deleteSQL = "DELETE FROM products WHERE productCode = ?";
                PreparedStatement pstmt = conn.prepareStatement(deleteSQL);

                for (int rowIndex : selectedRows) {
                    String productCode = (String) table.getValueAt(rowIndex, 0);
                    pstmt.setString(1, productCode);
                    pstmt.addBatch();
                }

                int[] deleteCounts = pstmt.executeBatch();
                for (int count : deleteCounts) {
                    if (count != 1) {
                        return false; // If any deletion count is not 1, consider it a failure
                    }
                }

                return true; // All deletions were successful
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}