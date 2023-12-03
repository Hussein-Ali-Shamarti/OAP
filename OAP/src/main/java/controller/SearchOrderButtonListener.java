/**
 * Represents an order entity with information such as order number, dates, status, comments,

 * customer number, and associated order date.
 * 
 * <p>Orders may also contain order details, which are not implemented in this version.</p>
 * 
 * @author 7094
 * @version 07.11.2023
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Customer;
import model.Order;
import model.OrderDAO;
import view.OrderView;
import java.util.List;

public class SearchOrderButtonListener implements ActionListener {
    private OrderView orderView;
    private OrderDAO orderDAO;

    public SearchOrderButtonListener(OrderView orderView, OrderDAO orderDAO) {
        this.orderView = orderView;
        this.orderDAO = orderDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
String searchCriteria = orderView.gatherUserInputForSearch();    	// Show the search dialog from the view
    
if (searchCriteria !=null && !searchCriteria.isEmpty()){
	List<Order> searchResults=orderDAO.searchOrder(searchCriteria);
	orderView.gatherUserInputForSearchOrder(searchResults);
    }
  }
    
}

