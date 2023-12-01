/**
 * The {@code ProductHandler} class is responsible for handling user actions in the {@link ProductView}.
 * It provides action listeners for adding, updating, deleting, saving, and searching products.
 *
 * This class allows users to interact with product data, perform CRUD operations, and save product information to a CSV file.
 *
 * @author Ole
 * @version 1.0
 */


package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.ProductDAO;
import model.Products;

import view.ProductView;


public class ProductHandler {

    /**
     * The associated {@link ProductView} instance for which actions are handled.
     */
    private final ProductView productView;

    /**
     * The data access object for managing products in the model.
     */
    private final ProductDAO productDAO;

    /**
     * Constructs a new {@code ProductHandler} with the given {@link ProductView} and {@link ProductDAO}.
     *
     * @param productView The associated product view.
     * @param productDAO  The data access object for managing products.
     */
    public ProductHandler(ProductView productView, ProductDAO productDAO) {
        this.productView = productView;
        this.productDAO = productDAO;
    }

    /**
     * Returns the action listener for the "Add Product" button.
     *
     * @return The action listener for adding a product.
     */
    public ActionListener getAddProductButtonListener() {
        return this::addProduct;
    }

    /**
     * Returns the action listener for the "Update Product" button.
     *
     * @return The action listener for updating a product.
     */
    public ActionListener getUpdateProductButtonListener() {
        return this::updateProduct;
    }

    /**
     * Returns the action listener for the "Delete Product" button.
     *
     * @return The action listener for deleting a product.
     */
    public ActionListener getDeleteProductButtonListener() {
        return this::deleteProduct;
    }

    /**
     * Returns the action listener for the "Save Products to File" button.
     *
     * @return The action listener for saving products to a file.
     */
    public ActionListener getSaveProductsButtonListener() {
        return this::saveProductsToFile;
    }

    /**
     * Returns the action listener for the "Search Products" button.
     *
     * @return The action listener for searching products.
     */
    public ActionListener getSearchProductsButtonListener() {
        return this::searchProducts;
    }

    /**
     * Handles the action event for adding a product.
     *
     * @param e The action event.
     */
    private void addProduct(ActionEvent e) {
        Products product = productView.gatherUserInputForAddProduct();

        if (product != null) {
            boolean success = productDAO.addProduct(product);

            if (success) {
                JOptionPane.showMessageDialog(productView, "Product added successfully!");
                productView.fetchAndDisplayProducts(); // Update the table with the new data
            } else {
                JOptionPane.showMessageDialog(productView, "Failed to add product.");
            }
        }
    }

    /**
     * Handles the action event for updating a product.
     *
     * @param e The action event.
     */
    private void updateProduct(ActionEvent e) {
        String productCodeToUpdate = JOptionPane.showInputDialog(productView, "Enter Product Code to update:");

        if (productCodeToUpdate != null && !productCodeToUpdate.isEmpty()) {
            Products existingProduct = productDAO.fetchProductFromDatabase(productCodeToUpdate);

            if (existingProduct != null) {
                Products updatedProduct = productView.gatherUserInputForUpdate(existingProduct);

                if (updatedProduct != null) {
                    boolean success = productDAO.updateProduct(updatedProduct);

                    if (success) {
                        JOptionPane.showMessageDialog(productView, "Product updated successfully!");
                        productView.fetchAndDisplayProducts(); // Update the table with the new data
                    } else {
                        JOptionPane.showMessageDialog(productView, "Failed to update product.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(productView, "Product not found.");
            }
        }
    }

    /**
     * Handles the action event for deleting a product.
     *
     * @param e The action event.
     */
    private void deleteProduct(ActionEvent e) {
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

    /**
     * Handles the action event for searching products.
     *
     * @param e The action event.
     */
    private void searchProducts(ActionEvent e) {
        String searchCriteria = productView.gatherInputForSearch();

        if (searchCriteria != null) {
            // Perform the search based on the user's input
            List<Products> searchResults = productDAO.searchProducts(searchCriteria);

            // Update the view with the search results
            productView.updateTableWithSearchResults(searchResults);
        }
    }

    /**
     * Handles the action event for saving products to a file.
     *
     * @param e The action event.
     */
    private void saveProductsToFile(ActionEvent e) {
        saveProductsToFile();
    }

    /**
     * Saves the product data to a CSV file chosen by the user.
     */
    public void saveProductsToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a CSV file to save");
        fileChooser.setSelectedFile(new File("Products.csv")); // Set default file name

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                List<String[]> products = productView.fetchAndDisplayProducts(); // Fetch product data

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
}