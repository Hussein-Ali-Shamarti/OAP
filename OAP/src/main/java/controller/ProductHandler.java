/**
 * File: ProductHandler.java
 * Description: This class is responsible for handling CRUD operations related to products.
 * It provides methods for adding, searching, updating, and deleting products in the database.
 * The class interacts with the model (Products) and the database to manage product-related operations.
 * @author Ole
 * @version 08.11.2023
 */

package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.DataBaseConnection;
import model.Products;


public class ProductHandler {

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

    /**
     * Deletes a product from the database based on the product code.
     * @param productCode The product code of the product to be deleted.
     * @return True if the product is deleted successfully, false otherwise.
     */
    public boolean deleteProduct(String productCode) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM products WHERE productCode = ?")) {

            preparedStatement.setString(1, productCode);

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0;
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
}