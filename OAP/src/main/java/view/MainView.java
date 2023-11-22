package view;
import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import view.OrderView;
import view.CustomerView;
import view.ProductView;
public class MainView extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainView() {
        // Set title anxd size
        super("Model Perfect");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set background color
        getContentPane().setBackground(Color.WHITE);

        // Create panel for company name and logo
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        // Add logo (replace with your own implementation)
        ImageIcon companyLogo = new ImageIcon("images/Model Perfect.png");
        JLabel logoLabel = new JLabel(companyLogo);
        mainPanel.add(logoLabel);

        // Add panel to the center of the window
        add(mainPanel, BorderLayout.CENTER);

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Create ProductsListener for "Products" button in MainMenu
        ProductsListener productsListener = new ProductsListener();

        // Create OrderListener for "Orders" button in MainMenu
        OrderListener orderListener = new OrderListener();

        // Create CustomersListener for "Customers" button in MainMenu
        CustomersListener customersListener = new CustomersListener();

        // Create EmployeesListener for "Employees" button in MainMenu
        EmployeesListener employeesListener = new EmployeesListener();

        // Add MainMenu panel at the top
        MainMenu mainMenu = new MainMenu(productsListener, orderListener, customersListener, employeesListener);
        add(mainMenu, BorderLayout.SOUTH);

        // Center the window on the screen
        setLocationRelativeTo(null);
    }

	// Static inner class for "Products" button in MainMenu
	private static class ProductsListener implements ActionListener {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        // Open ProductView when "Products" button is pressed
	        SwingUtilities.invokeLater(() -> {
	            // Create ProductView and set it visible
	            ProductView productView = new ProductView();
	            productView.setVisible(true);
	            
	        });
	    }
	}
	// Static inner class for "Orders" button in MainMenu
    private static class OrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open OrderView when "Orders" button is pressed
            SwingUtilities.invokeLater(() -> {
                OrderView orderView = new OrderView();
                orderView.setVisible(true);
               
            });
        }
    }

    // Static inner class for "Customers" button in MainMenu
    private static class CustomersListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open CustomerView when "Customers" button is pressed
            SwingUtilities.invokeLater(() -> {
                CustomerView customerView = new CustomerView();
                customerView.setVisible(true);
               
            });
        }
    }

// Static inner class for "Employees" button in MainMenu
private static class EmployeesListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Open EmployeeView when "Employees" button is pressed
    	 SwingUtilities.invokeLater(() -> {
             EmployeeView employeeView = new EmployeeView();
             employeeView.setVisible(true);
    	 });
    	 
    }
    
  }

}
