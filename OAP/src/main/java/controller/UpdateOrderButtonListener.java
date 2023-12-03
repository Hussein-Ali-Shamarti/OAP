package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.Order;
import model.OrderDAO;
import model.ProductDAO;
import view.OrderView;

public class UpdateOrderButtonListener implements ActionListener {
    private OrderView orderView;
    private OrderDAO orderDAO;

    public UpdateOrderButtonListener(OrderView orderView, OrderDAO orderDAO, ProductDAO productDAO) {
        this.orderView = orderView;
        this.orderDAO = orderDAO;
    }

  

	@Override
    public void actionPerformed(ActionEvent e) {
        String orderNumberString = JOptionPane.showInputDialog("Enter Order Number to update:");
        if (orderNumberString != null && !orderNumberString.isEmpty()) {
            try {
                int orderNumber = Integer.parseInt(orderNumberString);
                Order existingOrder = orderDAO.getOrder(orderNumber);
                if (existingOrder != null) {
                    Order updatedOrder = orderView.gatherUserInputForUpdateOrder(existingOrder);
                    if (updatedOrder != null) {
                        boolean success = orderDAO.editOrder(updatedOrder, orderNumber);
                        if (success) {
                            JOptionPane.showMessageDialog(orderView, "Order updated successfully!");
                        } else {
                            JOptionPane.showMessageDialog(orderView, "Failed to update order.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(orderView, "Order not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(orderView, "Invalid order number format.");
            }
        }
    }
}
