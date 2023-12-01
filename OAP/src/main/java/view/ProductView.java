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
import controller.DeleteProductButtonListener;
import controller.SearchProductsButtonListener;
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

        JButton searchButton = createButton("Search",new SearchProductsButtonListener(this, this.productDAO));
        JButton addButton = createButton("Add", new AddProductButtonListener(this, this.productDAO));
        JButton editButton = createButton("Edit", new UpdateProductButtonListener(this,this.productDAO));
        JButton deleteButton = createButton("Delete", new DeleteProductButtonListener(this,this.productDAO));
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
    
    public Products gatherUserInputForAddProduct() {
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

                return new Products(productCode, productName, productLine, productScale, productVendor,
                        productDescription, quantityInStock, buyPrice, msrp);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input format.");
            }
        }

        return null; // Return null if the user cancels or an error occurs
    }
    
    public Products gatherUserInputForUpdate(Products existingProduct) {
        JTextField productCodeField = new JTextField(existingProduct.getProductCode(), 10);
        JTextField productNameField = new JTextField(existingProduct.getProductName(), 20);
        JTextField productLineField = new JTextField(existingProduct.getProductLine(), 20);
        JTextField productScaleField = new JTextField(existingProduct.getProductScale(), 10);
        JTextField productVendorField = new JTextField(existingProduct.getProductVendor(), 20);
        JTextField productDescriptionField = new JTextField(existingProduct.getProductDescription(), 20);
        JTextField quantityInStockField = new JTextField(String.valueOf(existingProduct.getQuantityInStock()), 5);
        JTextField buyPriceField = new JTextField(String.valueOf(existingProduct.getBuyPrice()), 10);
        JTextField msrpField = new JTextField(String.valueOf(existingProduct.getMsrp()), 10);

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

        int result = JOptionPane.showConfirmDialog(null, panel, "Update Product Details", JOptionPane.OK_CANCEL_OPTION);
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

                return new Products(productCode, productName, productLine, productScale, productVendor,
                        productDescription, quantityInStock, buyPrice, msrp);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input format.");
            }
        }

        return null; // Return null if the user cancels or an error occurs
    }

    public List<String[]> fetchAndDisplayProducts() {
        List<String[]> products = productDAO.fetchProducts(); // Fetch data using DAO
        tableModel.setRowCount(0); // Clear existing rows

        for (String[] product : products) {
            tableModel.addRow(product); // Add rows to the table model
        }

        return products; // If you need to use the products elsewhere in your class
    }



 
        // Implement the logic to perform the search based on user input
        public List<Products> performSearch(String searchCriteria) {
            // Placeholder logic: You should implement the actual database search here
            // This code assumes you have a ProductHandler class to handle database operations
            ProductDAO productHandler = new ProductDAO();
            List<Products> searchResults = productHandler.searchProducts(searchCriteria);

            return searchResults;
        }

        // Update the table with the search results
        public void updateTableWithSearchResults(List<Products> searchResults) {
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