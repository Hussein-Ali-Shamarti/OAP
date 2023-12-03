/**
 * ProductView class represents the graphical user interface for managing products.
 * It extends the MainView class and provides functionality for displaying, adding, updating, and deleting products in a table format.
 * The class interacts with the controller (ProductHandler) and the model (ProductDAO and Products) to facilitate user actions
 * and maintain the consistency of product data.
 * 
 * @author Ole
 * @version 1.0
 */


package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
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
import model.ProductDAO;
import model.Products;


public class ProductView extends MainView {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private ProductDAO productDAO;
    private ProductHandler productHandler;

    /**
     * Gets the JTable component.
     *
     * @return The JTable component.
     */
    public JTable getTable() {
        return table;
    }

    /**
     * Constructor for the ProductView class.
     * Initializes the UI components, sets up the table, and fetches and displays the products.
     */
    public ProductView() {
        super();
        this.productDAO = new ProductDAO();
        this.productHandler = new ProductHandler(this, this.productDAO);

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

    /**
     * Initializes the UI components, including the title panel, table, and control panel.
     */
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

    /**
     * Sets up the table with column names and customization options.
     */
    private void setupTable() {
        String[] columnNames = {"Product Code", "Product Name", "Product Line", "Product Scale", "Product Vendor",
                "Product Description", "Quantity In Stock", "Buy Price", "MSRP"};
        tableModel = new DefaultTableModel(null, columnNames) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
    }

    /**
     * Sets up the control panel with buttons for search, add, edit, delete, and save operations.
     */
    public void setupControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        controlPanel.setBackground(new Color(90, 23, 139));

        JButton searchButton = createButton("Search", productHandler.getSearchProductsButtonListener());
        JButton addButton = createButton("Add", productHandler.getAddProductButtonListener());
        JButton editButton = createButton("Edit", productHandler.getUpdateProductButtonListener());
        JButton deleteButton = createButton("Delete", productHandler.getDeleteProductButtonListener());
        JButton saveProductButton = createButton("Save to File", productHandler.getSaveProductsButtonListener());

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

    /**
     * Creates a JButton with specified text, foreground color, background color, and an ActionListener.
     *
     * @param text     The text to be displayed on the button.
     * @param listener The ActionListener for the button.
     * @return The created JButton.
     */
    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    /**
     * Gathers user input for adding a new product through a dialog.
     *
     * @return The Products object created from the user's input.
     */
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

    /**
     * Gathers user input for updating an existing product through a dialog.
     *
     * @param existingProduct The existing Products object to be updated.
     * @return The updated Products object based on the user's input.
     */
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

    /**
     * Gathers user input for deleting selected products through confirmation dialogs.
     *
     * @return The list of product codes to be deleted, or null if the user cancels the operation.
     */
    public List<String> gatherUserInputForDelete() {
        JTable table = getTable();
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.", "Delete Product", JOptionPane.WARNING_MESSAGE);
            return null;
        } else {
            int confirmResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete selected product(s)?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (confirmResult == JOptionPane.YES_OPTION) {
                return getProductCodesFromRows(selectedRows);
            }
        }

        return null;
    }

    /**
     * Retrieves product codes from selected rows in the table.
     *
     * @param selectedRows The array of selected row indices.
     * @return The list of product codes corresponding to the selected rows.
     */
    private List<String> getProductCodesFromRows(int[] selectedRows) {
        JTable table = getTable();
        List<String> productCodes = new ArrayList<>();

        for (int rowIndex : selectedRows) {
            String productCode = (String) table.getValueAt(rowIndex, 0);
            productCodes.add(productCode);
        }

        return productCodes;
    }

    /**
     * Gathers user input for searching products through a dialog.
     *
     * @return The search criteria entered by the user, or null if the user cancels the operation.
     */
    public String gatherInputForSearch() {
        JTextField searchField = new JTextField(20);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Search Products:"));
        panel.add(searchField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Search Products", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            return searchField.getText().trim();
        } else {
            return null; // User canceled the operation
        }
    }

    /**
     * Performs a search based on user input and updates the table with the search results.
     *
     * @param searchCriteria The search criteria entered by the user.
     */
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

    /**
     * Updates the table with the provided list of product details.
     *
     * @param products The list of product details to update the table.
     * @return The list of product details if needed elsewhere in the class.
     */
    public List<String[]> updateTableWithProducts(List<String[]> products) {
        tableModel.setRowCount(0); // Clear existing rows from the table

        // Populate the table with the product details
        for (String[] product : products) {
            tableModel.addRow(product);
        }

        return products; // If you need to use the products elsewhere in your class
    }

    /**
     * Fetches and displays products in the table.
     *
     * @return The list of product details fetched from the database.
     */
    public List<String[]> fetchAndDisplayProducts() {
        List<String[]> products = productDAO.fetchProducts(); // Fetch data using DAO
        updateTableWithProducts(products);

        return products; // If you need to use the products elsewhere in your class
    }
}

