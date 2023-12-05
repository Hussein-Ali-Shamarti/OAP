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

/**
 * This class serves as the Data Access Object (DAO) for managing CRUD (Create, Read, Update, Delete) operations
 * related to products in the database. It acts as an intermediary between the application's model (Products) and the database,
 * providing methods to add, search, update, and delete products. The class encapsulates the database interactions and ensures
 * proper handling of database connections and SQL queries.
 * The methods in this class are designed to handle various aspects of product management, such as adding new products,
 * searching for products based on criteria, updating product information, and deleting products. Additionally, it provides
 * methods to retrieve product details, check the existence of a product line, and obtain mappings between product names and codes.
 * 
 * @author 7120
 */

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
     * @param productCodes The product code of the product to be deleted.
     * @return True if the product is deleted successfully, false otherwise.
     */
    
    
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
     * Fetches a single product's details from the database based on its product code.
     * This method executes a SQL query to retrieve all attributes of a product with the specified code.
     * If found, it maps the result set to a Products object.
     *
     * @param productCode The unique code of the product to be retrieved.
     * @return A Products object containing the details of the product, or null if the product is not found.
     */
    
    
    
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
     * Fetches all product details from the database.
     * This method retrieves various attributes of each product, including code, name, line, scale, vendor, 
     * description, quantity in stock, buy price, and MSRP. The data for each product is stored in a string array.
     *
     * @return A list of string arrays, where each array represents a product and its details.
     */
    
    
    
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
            e.printStackTrace(); 
        }

        return products;
    }
    
    
    
    /**
     * Retrieves all product details from the database.
     * This method returns a map where each key is a product name and the corresponding value is the product code.
     *
     * @return A map containing product names as keys and product codes as values.
     */
    
    
    public Map<String, String> getAllProductDetails() {
        Map<String, String> productDetails = new HashMap<>();

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT productName, productCode FROM products");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String productName = resultSet.getString("productName"); 
                String productCode = resultSet.getString("productCode"); 
                productDetails.put(productName, productCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productDetails;
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
            e.printStackTrace(); 
        }

        return productNames;
    }

    
   
    /**
     * Retrieves the product code for a given product name.
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

        return null; 
    }
    
    /**
     * Retrieves the product name corresponding to a given product code.
     * This method queries the database for the name of the product associated with the specified product code.
     *
     * @param productCode The unique code of the product for which the name is being retrieved.
     * @return The name of the product if found, or null if there is no product with the given code.
     */
    
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
        return null; 
    }

   
    /**
     * Retrieves detailed information for a given product name.
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

        return productDetails;
    }

   
    
    /**
     * Retrieves detailed information for a product identified by its product code.
     * This method returns a map containing various details of the product, such as quantity in stock, buy price, and MSRP.
     *
     * @param productCode The code of the product for which details are being retrieved.
     * @return A map containing key-value pairs of product details.
     */
    
    

    public Map<String, Object> getProductDetailsByCode(String productCode) {
        Map<String, Object> productDetails = new HashMap<>();
        Connection connection = null; 

        try {
            connection = DataBaseConnection.getConnection();

            String query = "SELECT * FROM products WHERE productCode = ?"; 
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, productCode);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        productDetails.put("quantityInStock", resultSet.getInt("quantityInStock"));
                        productDetails.put("buyPrice", resultSet.getDouble("buyPrice"));
                        productDetails.put("MSRP", resultSet.getDouble("MSRP"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return productDetails;
    }
    

    /**
     * Retrieves a mapping of product names to their corresponding codes.
     * This method queries the database and constructs a map where the key is the product name and the value is the product code.
     *
     * @return A map containing product names as keys and their corresponding product codes as values.
     */
    
    
    
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
            e.printStackTrace();
        }

        return productNamesToCodes;
    }
    
    /**
     * Check if the entered product line exists in the database.
     * @param productLine The product line to check.
     * @return true if the product line exists; false otherwise.
     */
   
    
    public boolean isProductLineExists(String productLine) {
        try {
            try (PreparedStatement preparedStatement = DataBaseConnection.prepareStatement("SELECT COUNT(*) FROM productlines WHERE productLine = ?")) {
                preparedStatement.setString(1, productLine);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {               
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        return true; 
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

            String errorMessage = "Error checking product line existence. Please try again later or contact support.";
            throw new RuntimeException(errorMessage, ex);
        }

        return false; 
    }
    
    
    /**
     * Checks if the specified product is available in stock in the required quantity.
     *
     * @param productCode     The product code to check availability for.
     * @param quantityOrdered The quantity of the product ordered.
     * @return True if the product is available in the required quantity, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    
    public boolean isStockAvailable(String productCode, int quantityOrdered) throws SQLException {
        String sql = "SELECT quantityInStock FROM products WHERE productCode = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int quantityInStock = rs.getInt("quantityInStock");
                    return quantityOrdered <= quantityInStock;
                }
            }
        }
        return false;
    }
    
    
    /**
     * Updates the stock quantity for a given product.
     *
     * @param productCode The code of the product.
     * @param quantityChange The amount to adjust the stock by. This can be negative or positive.
     * @return true if the update was successful, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public boolean updateProductStock(String productCode, int quantityChange) throws SQLException {
        String sql = "UPDATE products SET quantityInStock = quantityInStock + ? WHERE productCode = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantityChange);
            pstmt.setString(2, productCode);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
 }