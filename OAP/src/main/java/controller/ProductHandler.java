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

/**
 * The {@code ProductHandler} class is responsible for handling user actions in the {@link ProductView}.
 * It provides action listeners for adding, updating, deleting, saving, and searching products.
 *
 * This class allows users to interact with product data, perform CRUD operations, and save product information to a CSV file.
 *
 * @author Ole
 * @version 1.0
 */

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
     * Handles the action event for adding a product. It gathers the product information from
     * the ProductView and attempts to add it to the database using ProductDAO. It then 
     * displays a message based on the success of the operation.
     *
     * @param e The action event triggering the addition of a new product.
     */
    
    public void addProduct(ActionEvent e) {
        Products product = productView.gatherUserInputForAddProduct();

        if (product != null) {
            boolean success = productDAO.addProduct(product);

            if (success) {
                JOptionPane.showMessageDialog(productView, "Product added successfully!");
                productView.fetchAndDisplayProducts(); 
            } else {
                JOptionPane.showMessageDialog(productView, "Failed to add product.");
            }
        }
    }

    /**
     * Handles the action event for updating a product. It prompts the user for a product code,
     * fetches the corresponding product, and allows the user to update its details. The update
     * is processed through ProductDAO.
     *
     * @param e The action event triggering the update operation.
     */
    public void updateProduct(ActionEvent e) {
        String productCodeToUpdate = JOptionPane.showInputDialog(productView, "Enter Product Code to update:");

        if (productCodeToUpdate != null && !productCodeToUpdate.isEmpty()) {
            Products existingProduct = productDAO.fetchProductFromDatabase(productCodeToUpdate);

            if (existingProduct != null) {
                Products updatedProduct = productView.gatherUserInputForUpdate(existingProduct);

                if (updatedProduct != null) {
                    boolean success = productDAO.updateProduct(updatedProduct);

                    if (success) {
                        JOptionPane.showMessageDialog(productView, "Product updated successfully!");
                        productView.fetchAndDisplayProducts(); 
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
     * Handles the action event for deleting a product. It prompts the user to specify which
     * product(s) to delete and processes the deletion through ProductDAO.
     *
     * @param e The action event triggering the deletion operation.
     */
    
    public void deleteProduct(ActionEvent e) {
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
     * Handles the action event for searching products based on user-input criteria. Retrieves 
     * and displays the search results in the ProductView.
     *
     * @param e The action event triggering the search operation.
     */
    public void searchProducts(ActionEvent e) {
        String searchCriteria = productView.gatherInputForSearch();

        if (searchCriteria != null) {
            
            List<Products> searchResults = productDAO.searchProducts(searchCriteria);

            
            productView.updateTableWithSearchResults(searchResults);
        }
    }

    /**
     * Handles the action event for saving product data to a file. Prompts the user to choose a 
     * file location and writes the product data in CSV format. Reports any file I/O errors encountered.
     *
     * @param e The action event triggering the save operation.
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    
    public void saveProductsToFile(ActionEvent e) {
        saveProductsToFile();
    }

    /**
     * Saves the product data to a CSV file chosen by the user. It writes details such as product
     * code, name, line, etc. Alerts the user upon successful save or error.
     *
     * @throws IOException If an I/O error occurs while writing to the file.
     */
    
    public void saveProductsToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a CSV file to save");
        fileChooser.setSelectedFile(new File("Products.csv"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                List<String[]> products = productView.fetchAndDisplayProducts(); 

                
                writer.write("Product Code, Product Name, Product Line, Product Scale, Product Vendor, " +
                             "Product Description, Quantity In Stock, Buy Price, MSRP");
                writer.newLine();

               
                for (String[] product : products) {
                    String line = String.join(",", product); 
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