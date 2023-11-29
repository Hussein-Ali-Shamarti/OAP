package view;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.AddProductButtonListener;
import database.DataBaseConnection;
import model.ProductDAO;
import model.Products;


public class ProductView extends MainView {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField textField; // Assuming you have a JTextField for search

    public ProductView() {
        super();

        setLayout(new BorderLayout());
        initializeUI();
        fetchAndDisplayProducts();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
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
        String[] columnNames = {"Product Code", "Product Name","product Line", "Product Scale", "Product Vendor",
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
        JButton addButton = createButton("Add", new AddProductButtonListener(null));
        JButton editButton = createButton("Edit", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete", new DeleteButtonListener());
        JButton saveProductButton = createButton("Save to File", new SaveProductButtonListener());
		

        controlPanel.add(searchButton);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(saveProductButton);

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

    List<String[]> fetchAndDisplayProducts() {
        List<String[]> products = new ArrayList<>();
        tableModel.setRowCount(0); // Clear the existing rows

        try (Connection conn = database.DataBaseConnection.getConnection();
             Statement statement = conn.createStatement()) {
            String sql = "SELECT productCode, productName, productLine, productScale, productVendor, " +
                         "productDescription, quantityInStock, buyPrice, msrp FROM products";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String[] product = {
                    resultSet.getString("productCode"),
                    resultSet.getString("productName"),
                    resultSet.getString("productLine"),
                    resultSet.getString("productScale"),
                    resultSet.getString("productVendor"),
                    resultSet.getString("productDescription"),
                    String.valueOf(resultSet.getInt("quantityInStock")),
                    String.valueOf(resultSet.getDouble("buyPrice")),
                    String.valueOf(resultSet.getDouble("msrp"))
                };
                tableModel.addRow(product); // Add row to the table model
                products.add(product); // Add to the list
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching product data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return products;
    }


 // Action listener for "Add" button
   



    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get the selected row from the table
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ProductView.this, "Please select a product to update.");
                return;
            }

            String productCode = table.getValueAt(selectedRow, 0).toString();
            Products productToUpdate = fetchProductFromDatabase(productCode);

            if (productToUpdate == null) {
                JOptionPane.showMessageDialog(ProductView.this, "Product not found in the database.");
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
                        JOptionPane.showMessageDialog(ProductView.this, "Product updated successfully!");
                        // Add your method to refresh the product list if you have one
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
                    String productLine = resultSet.getString("productLine");
                    String productScale = resultSet.getString("productScale");
                    String productVendor = resultSet.getString("productVendor");
                    String productDescription = resultSet.getString("productDescription");
                    int quantityInStock = resultSet.getInt("quantityInStock");
                    double buyPrice = resultSet.getDouble("buyPrice");
                    double msrp = resultSet.getDouble("msrp");
                    
                    Products product = new Products(productCode, productName, productLine, productScale, productVendor,
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

 // Action listener for "Search" button
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a dialog to input search criteria
            JTextField searchField = new JTextField(20);
            JPanel panel = new JPanel();
            panel.add(new JLabel("Search Products:"));
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
            ProductDAO productHandler = new ProductDAO();
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
                    product.getProductLine(),
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
    
    private void saveProductsToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a CSV file to save");
        fileChooser.setSelectedFile(new File("Products.csv")); // Set default file name

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                List<String[]> products = fetchAndDisplayProducts(); // Fetch product data

                // Write header row (optional)
                writer.write("Product Code, Product Name, Product Line, Product Scale, Product Vendor, " +
                             "Product Description, Quantity In Stock, Buy Price, MSRP");
                writer.newLine();

                // Write data rows
                for (String[] product : products) {
                    String line = String.join(",", product); // Comma as delimiter
                    writer.write(line);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(null, "CSV file saved successfully at " + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private class SaveProductButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveProductsToFile();
        }
    }

}