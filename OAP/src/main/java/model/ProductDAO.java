/**
 * File: ProductHandler.java
 * Description: This class is responsible for handling CRUD operations related to products.
 * It provides methods for adding, searching, updating, and deleting products in the database.
 * The class interacts with the model (Products) and the database to manage product-related operations.
 * @author Ole
 * @version 08.11.2023
 */

package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.DataBaseConnection;


public class ProductDAO {

    /**
     * Adds a new product to the database.
     * @param product The product to be added.
     * @return True if the product is added successfully, false otherwise.
     */
    public boolean addProduct(Products product) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO products (productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            preparedStatement.setString(1, product.getProductCode());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getProductLine());
            preparedStatement.setString(4, product.getProductScale());
            preparedStatement.setString(5, product.getProductVendor());
            preparedStatement.setString(6, product.getProductDescription());
            preparedStatement.setInt(7, product.getQuantityInStock());
            preparedStatement.setDouble(8, product.getBuyPrice());
            preparedStatement.setDouble(9, product.getMsrp());

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Searches for products in the database based on the provided search criteria.
     * @param searchCriteria The criteria to search for products.
     * @return A list of Products objects matching the search criteria.
     */
    public List<Products> searchProducts(String searchCriteria) {
        List<Products> searchResults = new ArrayList<>();

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM products WHERE " +
                             "productCode LIKE ? OR " +
                             "productName LIKE ? OR " +
                             "productLine LIKE ? OR " +
                             "productScale LIKE ? OR " +
                             "productVendor LIKE ? OR " +
                             "quantityInStock LIKE ? OR " +
                             "buyPrice LIKE ? OR " +
                             "MSRP LIKE ? OR " +
                             "productDescription LIKE ?")) {

            for (int i = 1; i <= 9; i++) {
                preparedStatement.setString(i, "%" + searchCriteria + "%");
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Products products = mapResultSetToProduct(resultSet);
                    searchResults.add(products);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }
    
    
    
    public List<String[]> fetchProducts() {
        List<String[]> products = new ArrayList<>();

        try (Connection conn = DataBaseConnection.getConnection();
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
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return products;
    }


    /**
     * Updates an existing product's information in the database.
     * @param product The updated product information.
     * @return True if the product is updated successfully, false otherwise.
     */
    public boolean updateProduct(Products product) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE products SET productName = ?, productLine = ?, productScale = ?, productVendor = ?, " +
                             "productDescription = ?, quantityInStock = ?, buyPrice = ?, MSRP = ? WHERE productCode = ?")) {

            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getProductLine());
            preparedStatement.setString(3, product.getProductScale());
            preparedStatement.setString(4, product.getProductVendor());
            preparedStatement.setString(5, product.getProductDescription());
            preparedStatement.setInt(6, product.getQuantityInStock());
            preparedStatement.setDouble(7, product.getBuyPrice());
            preparedStatement.setDouble(8, product.getMsrp());
            preparedStatement.setString(9, product.getProductCode());

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Products fetchProductFromDatabase(String productCode) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM products WHERE productCode = ?")) {

            preparedStatement.setString(1, productCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToProduct(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Deletes a product from the database based on the product code.
     * @param productCode The product code of the product to be deleted.
     * @return True if the product is deleted successfully, false otherwise.
     */
 // ProductDAO
    public boolean deleteProducts(List<String> productCodes) {
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM products WHERE productCode = ?")) {

            for (String productCode : productCodes) {
                pstmt.setString(1, productCode);
                pstmt.addBatch();
            }

            int[] deleteCounts = pstmt.executeBatch();
            for (int count : deleteCounts) {
                if (count != 1) {
                    return false;
                }
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Retrieves the product code for a given product name.
     * 
     * @param productName The name of the product.
     * @return The product code corresponding to the given product name, or null if not found.
     */
    public String getProductCodeByName(String productName) {
        String query = "SELECT productCode FROM products WHERE productName = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("productCode");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if product name is not found or if an exception occurs
    }
    
    public String getProductNameByCode(String productCode) {
        String query = "SELECT productName FROM products WHERE productCode = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("productName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if product code is not found or if an exception occurs
    }

    /**
     * Maps a ResultSet to a Products object.
     * @param resultSet The ResultSet containing product information.
     * @return The Products object created from the ResultSet.
     * @throws SQLException If a SQL exception occurs.
     */
    private Products mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        return new Products(
                resultSet.getString("productCode"),
                resultSet.getString("productName"),
                resultSet.getString("productLine"),
                resultSet.getString("productScale"),
                resultSet.getString("productVendor"),
                resultSet.getString("productDescription"),
                resultSet.getInt("quantityInStock"),
                resultSet.getDouble("buyPrice"),
                resultSet.getDouble("msrp")
        );
    }
    /**
     * Retrieves a mapping of product names to product codes.
     *
     * @return A map where the key is the product name and the value is the product code.
     */
    public Map<String, String> getProducts() {
        Map<String, String> products = new HashMap<>();
        String query = "SELECT productName, productCode FROM products";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String productName = rs.getString("productName");
                String productCode = rs.getString("productCode");
                products.put(productName, productCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    /**
     * Retrieves detailed information for a given product name.
     *
     * @param productName The name of the product.
     * @return A map containing the details of the product including quantityInStock, buyPrice, and MSRP.
     */
    public Map<String, Object> getProductDetailsByName(String productName) {
        Map<String, Object> productDetails = new HashMap<>();
        String query = "SELECT quantityInStock, buyPrice, MSRP FROM products WHERE productName = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    productDetails.put("quantityInStock", rs.getInt("quantityInStock"));
                    productDetails.put("buyPrice", rs.getDouble("buyPrice"));
                    productDetails.put("MSRP", rs.getDouble("MSRP"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productDetails; // This map will be empty if the product is not found or if an exception occurs
    }
    
    
    /**
     * Check if the entered product line exists in the database.
     *
     * @param productLine The product line to check.
     * @return true if the product line exists; false otherwise.
     */
    public boolean isProductLineExists(String productLine) {
        try {
            // create a PreparedStatement
            try (PreparedStatement preparedStatement = DataBaseConnection.prepareStatement("SELECT COUNT(*) FROM productlines WHERE productLine = ?")) {
                preparedStatement.setString(1, productLine);

                // Execute the query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Check if a row with the product line exists
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return true; // Product line exists
                    }
                }
            }
        } catch (SQLException ex) {
            // Log the exception or handle it based on your application's requirements
            ex.printStackTrace();
            
            // Provide a user-friendly error message
            String errorMessage = "Error checking product line existence. Please try again later or contact support.";
            // Rethrow the exception as a runtime exception with the user-friendly message
            throw new RuntimeException(errorMessage, ex);
        }

        return false; // Product line doesn't exist or an error occurred
    }
    /**
     * Retrieves all product names from the database.
     * @return A list of all product names.
     */
    public List<String> getAllProductNames() {
        List<String> productNames = new ArrayList<>();
        String query = "SELECT productName FROM products";

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                productNames.add(resultSet.getString("productName"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
        }

        return productNames;
    }

    public Map<String, String> getAllProductDetails() {
        Map<String, String> productDetails = new HashMap<>();

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT productName, productCode FROM products");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String productName = resultSet.getString("productName"); // Adjust the column name based on your database
                String productCode = resultSet.getString("productCode"); // Adjust the column name based on your database
                productDetails.put(productName, productCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception (Consider logging this or throwing a custom exception)
        }

        return productDetails;
    }


    public Map<String, Object> getProductDetailsByCode(String productCode) {
        Map<String, Object> productDetails = new HashMap<>();
        Connection connection = null; // Initialize connection

        try {
            connection = DataBaseConnection.getConnection(); // Get the database connection

            String query = "SELECT * FROM products WHERE productCode = ?"; // Adjust this query to fit your database schema
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, productCode);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Assuming you have columns like 'quantityInStock', 'buyPrice', 'MSRP', etc.
                        // Adjust the column names based on your database schema
                        productDetails.put("quantityInStock", resultSet.getInt("quantityInStock"));
                        productDetails.put("buyPrice", resultSet.getDouble("buyPrice"));
                        productDetails.put("MSRP", resultSet.getDouble("MSRP"));
                        // Add more columns as needed
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        } finally {
            // Close the connection in the finally block
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately
                }
            }
        }

        return productDetails;
    }
    
    public Map<String, String> getProductNamesToCodes() {
        Map<String, String> productNamesToCodes = new HashMap<>();
        String query = "SELECT productName, productCode FROM products";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String productName = rs.getString("productName");
                String productCode = rs.getString("productCode");
                productNamesToCodes.put(productName, productCode);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
        }

        return productNamesToCodes;
    }


}
