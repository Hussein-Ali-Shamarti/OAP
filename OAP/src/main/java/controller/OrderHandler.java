package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.DataBaseConnection;
import model.Order;

public class OrderHandler {

    private static final String INSERT_ORDER_SQL = "INSERT INTO orders (orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SEARCH_ORDERS_SQL = "SELECT * FROM orders WHERE orderNumber LIKE ? OR orderDate LIKE ? OR requiredDate LIKE ? OR shippedDate LIKE ? OR status LIKE ? OR comments LIKE ? OR customerNumber LIKE ?";
    private static final String UPDATE_ORDER_SQL = "UPDATE orders SET orderDate = ?, requiredDate = ?, shippedDate = ?, status = ?, comments = ?, customerNumber = ? WHERE orderNumber = ?";
    private static final String DELETE_ORDER_SQL = "DELETE FROM orders WHERE orderNumber = ?";

    public boolean addOrder(Order order) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER_SQL)) {

            setOrderParameters(preparedStatement, order);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> searchOrders(String searchCriteria) {
        List<Order> searchResults = new ArrayList<>();

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_ORDERS_SQL)) {

            for (int i = 1; i <= 7; i++) {
                preparedStatement.setString(i, "%" + searchCriteria + "%");
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = mapResultSetToOrder(resultSet);
                    searchResults.add(order);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }

    public boolean updateOrder(Order order) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_SQL)) {

            setOrderParameters(preparedStatement, order);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrder(String orderNumber) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ORDER_SQL)) {

            preparedStatement.setString(1, orderNumber);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setOrderParameters(PreparedStatement preparedStatement, Order order) throws SQLException {
        preparedStatement.setInt(1, order.getOrderNumber());
        preparedStatement.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
        preparedStatement.setDate(3, new java.sql.Date(order.getRequiredDate().getTime()));
        preparedStatement.setDate(4, new java.sql.Date(order.getShippedDate().getTime()));
        preparedStatement.setString(5, order.getStatus());
        preparedStatement.setString(6, order.getComments());
        preparedStatement.setInt(7, order.getCustomerNumber());
    }

    private Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        int orderNumber = resultSet.getInt("orderNumber");
        Date orderDate = resultSet.getDate("orderDate");
        Date requiredDate = resultSet.getDate("requiredDate");
        Date shippedDate = resultSet.getDate("shippedDate");
        String status = resultSet.getString("status");
        String comments = resultSet.getString("comments");
        int customerNumber = resultSet.getInt("customerNumber");

        // Convert null values to actual dates
        orderDate = (orderDate != null) ? new Date(orderDate.getTime()) : null;
        requiredDate = (requiredDate != null) ? new Date(requiredDate.getTime()) : null;
        shippedDate = (shippedDate != null) ? new Date(shippedDate.getTime()) : null;

        return new Order(orderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber);
    }
}