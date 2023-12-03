package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import model.Order;
import model.OrderDAO;
import model.OrderDetails;
import model.OrderInput;
import view.OrderView;

public class AddOrderButtonListener implements ActionListener {
    private OrderView orderView;
    private OrderDAO orderDAO;

    public AddOrderButtonListener(OrderView orderView, OrderDAO orderDAO) {
        this.orderView = orderView;
        this.orderDAO = orderDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OrderInput orderAndDetails = orderView.gatherUserInputForAddOrder();

        if (orderAndDetails != null) {
            Order order = orderAndDetails.getOrder();
            List<OrderDetails> orderDetailsList = orderAndDetails.getOrderDetailsList();

            boolean success = orderDAO.addOrder(order, orderDetailsList);
            if (success) {
                JOptionPane.showMessageDialog(orderView, "New order added successfully!");
                // Optionally, update the UI to reflect the new order
            } else {
                JOptionPane.showMessageDialog(orderView, "Failed to add new order.");
            }
        }
    }
}
