package view;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Font;

import java.awt.GridLayout;

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

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.AddProductButtonListener;
import controller.UpdateProductButtonListener;
import model.ProductDAO;
import model.Products;


public class ProductView extends MainView {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private ProductDAO productDAO;


    public JTable getTable() {
        return table;
    }

 
 
    public ProductView() {
        super();
        this.productDAO = new ProductDAO();
        table = new JTable(tableModel);
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
        JButton editButton = createButton("Edit", new UpdateProductButtonListener(this,this.productDAO));
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

    private List<String[]> fetchAndDisplayProducts() {
        List<String[]> products = productDAO.fetchProducts(); // Fetch data using DAO
        tableModel.setRowCount(0); // Clear existing rows

        for (String[] product : products) {
            tableModel.addRow(product); // Add rows to the table model
        }

        return products; // If you need to use the products elsewhere in your class
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