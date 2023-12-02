package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import model.OrderDAO;
import view.OrderView;

public class DeleteOrderButtonListener implements ActionListener {
    private OrderView orderView;
    private OrderDAO orderDAO;

    public DeleteOrderButtonListener(OrderView orderView, OrderDAO orderDAO) {
        this.orderView = orderView;
        this.orderDAO = orderDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Integer orderNumberToDelete = orderView.gatherUserInputForDeleteOrder();

        if (orderNumberToDelete != null) {
            boolean success = orderDAO.deleteOrder(orderNumberToDelete);

            if (success) {
                JOptionPane.showMessageDialog(orderView, "Order deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(orderView, "Failed to delete order.");
            }
        }
    }
}