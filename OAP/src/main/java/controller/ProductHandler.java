package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DataBaseConnection;
import model.Products;

public class ProductHandler {

    private static final String INSERT_PRODUCT_SQL = "INSERT INTO products (productCode, productName, productLine, productScale, productVendor, productDescription, quantityInStock, buyPrice, MSRP) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SEARCH_PRODUCTS_SQL = "SELECT * FROM products WHERE productCode LIKE ? OR productName LIKE ? OR productLine LIKE ? OR productScale LIKE ? OR productVendor LIKE ? OR quantityInStock LIKE ? OR buyPrice LIKE ? OR MSRP LIKE ? OR productDescription LIKE ?";
    private static final String UPDATE_PRODUCT_SQL = "UPDATE products SET productName = ?, productLine = ?, productScale = ?, productVendor = ?, productDescription = ?, quantityInStock = ?, buyPrice = ?, MSRP = ? WHERE productCode = ?";
    private static final String DELETE_PRODUCT_SQL = "DELETE FROM products WHERE productCode = ?";

    public boolean addProduct(Products product) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_SQL)) {

            setProductParameters(preparedStatement, product);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Products> searchProducts(String searchCriteria) {
        List<Products> searchResults = new ArrayList<>();

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_PRODUCTS_SQL)) {

            for (int i = 1; i <= 9; i++) {
                preparedStatement.setString(i, "%" + searchCriteria + "%");
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Products product = mapResultSetToProduct(resultSet);
                    searchResults.add(product);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }

    public boolean updateProduct(Products product) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PRODUCT_SQL)) {

            setProductParameters(preparedStatement, product);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteProduct(String productCode) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT_SQL)) {

            preparedStatement.setString(1, productCode);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setProductParameters(PreparedStatement preparedStatement, Products product) throws SQLException {
        preparedStatement.setString(1, product.getProductCode());
        preparedStatement.setString(2, product.getProductName());
        preparedStatement.setString(3, product.getProductLine());
        preparedStatement.setString(4, product.getProductScale());
        preparedStatement.setString(5, product.getProductVendor());
        preparedStatement.setString(6, product.getProductDescription());
        preparedStatement.setInt(7, product.getQuantityInStock());
        preparedStatement.setDouble(8, product.getBuyPrice());
        preparedStatement.setDouble(9, product.getMsrp());
    }

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
}