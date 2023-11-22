/**
 * File: ProductHandler.java
 * Description: This class serves as the controller for managing products in the application. It provides methods for creating, updating, and deleting products in the database, as well as searching for products and adding them to orders.
 * The class interacts with the model (Product) and the database to handle product-related operations.
 * @author Ole
 * @version 08.11.2023
 */

package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import view.ProductView;
import database.DataBaseConnection;
import model.Products;
public class ProductHandler {

    // CRUD Methods

    public boolean addProduct(Products product) {
        try (Connection connection = DataBaseConnection.getConnection()) {
            String sql = "INSERT INTO products (productCode, productName, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, product.getProductCode());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getProductScale());
            preparedStatement.setString(4, product.getProductVendor());
            preparedStatement.setString(5, product.getProductDescription());
            preparedStatement.setInt(6, product.getQuantityInStock());
            preparedStatement.setDouble(7, product.getBuyPrice());
            preparedStatement.setDouble(8, product.getMsrp());
            int affectedRows = preparedStatement.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Products> searchProducts(String searchCriteria) {
        List<Products> searchResults = new ArrayList<>();

        try (Connection connection = DataBaseConnection.getConnection()) {
            // Define your SQL query to search for products based on the search criteria
            String sql = "SELECT * FROM products WHERE productName LIKE ?"; // Example: Searching by product name

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set the search criteria as a parameter in the SQL query
                preparedStatement.setString(1, "%" + searchCriteria + "%");

                // Execute the query and retrieve the result set
                ResultSet resultSet = preparedStatement.executeQuery();

                // Iterate through the result set and add matching products to the list
                while (resultSet.next()) {
                    Products product = new Products(
                        resultSet.getString("productCode"),
                        resultSet.getString("productName"),
                        resultSet.getString("productScale"),
                        resultSet.getString("productVendor"),
                        resultSet.getString("productDescription"),
                        resultSet.getInt("quantityInStock"),
                        resultSet.getDouble("buyPrice"),
                        resultSet.getDouble("msrp")
                    );
                    searchResults.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }

    public boolean updateProduct(Products product) {
        try (Connection connection = database.DataBaseConnection.getConnection()) {
            String sql = "UPDATE products SET productName = ?, productScale = ?, productVendor = ?, productDescription = ?, quantityInStock = ?, buyPrice = ?, MSRP = ? WHERE productCode = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getProductScale());
            preparedStatement.setString(3, product.getProductVendor());
            preparedStatement.setString(4, product.getProductDescription());
            preparedStatement.setInt(5, product.getQuantityInStock());
            preparedStatement.setDouble(6, product.getBuyPrice());
            preparedStatement.setDouble(7, product.getMsrp());
            preparedStatement.setString(8, product.getProductCode());
            int affectedRows = preparedStatement.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(String productCode) {
        try (Connection connection = database.DataBaseConnection.getConnection()) {
            String sql = "DELETE FROM products WHERE productCode = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, productCode);
            int affectedRows = preparedStatement.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
