package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.Order;
import model.OrderDAO;
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
        Order newOrder = orderView.gatherUserInputForAddOrder();
        if (newOrder != null) {
            boolean success = orderDAO.addOrder(newOrder, null);
            if (success) {
                JOptionPane.showMessageDialog(orderView, "New order added successfully!");
                // Optionally, update the UI to reflect the new order
            } else {
                JOptionPane.showMessageDialog(orderView, "Failed to add new order.");
            }
        }
    }
}
