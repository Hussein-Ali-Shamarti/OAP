package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * The MainView class represents the main view of the application.
 * It provides the user interface for the application's main menu and layout.
 * @author Ole
 */


public class MainView extends JFrame {

    private static final long serialVersionUID = 1L;
    protected JMenuBar menuBar;
    
    /**
     * Constructs the MainView.
     */

    public MainView() {
        super("Model Perfect");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon appIcon = new ImageIcon(getClass().getResource("/images/Model Perfect.png"));
        setIconImage(appIcon.getImage());

        getContentPane().setBackground(Color.WHITE);

        MenuBar menuBar = new MenuBar(rootPaneCheckingEnabled); 

        setJMenuBar(menuBar.getMenuBar()); 
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);

        ImageIcon companyLogo = new ImageIcon(getClass().getResource("/images/Model Perfect.png"));
        JLabel logoLabel = new JLabel(companyLogo);
        logoPanel.add(logoLabel);

        JPanel companyNamePanel = new JPanel();
        companyNamePanel.setBackground(Color.WHITE);

        Font coolFont = new Font("Segoe UI", Font.BOLD, 24);
        JLabel companyNameLabel = new JLabel("Model Perfect Inc.");
        companyNameLabel.setFont(coolFont);
        companyNameLabel.setForeground(Color.BLACK);
        companyNamePanel.add(companyNameLabel);

        mainPanel.add(logoPanel);
        mainPanel.add(companyNamePanel);

        add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        
        JButton employeesButton = new JButton("Employees");
        

        ProductListener productsListener = new ProductListener();
        

        OrderListener orderListener = new OrderListener();


        CustomerListener customersListener = new CustomerListener();


        EmployeeListener employeeHandler = new EmployeeListener ();
        employeesButton.addActionListener(employeeHandler);


        MainMenu mainMenu = new MainMenu(productsListener, orderListener, customersListener, employeeHandler);
        add(mainMenu, BorderLayout.SOUTH);


        setLocationRelativeTo(null);
    }

    /**
     * An ActionListener implementation for handling product-related events.
     * This class defines the behavior when specific product-related actions occur.
     */
    
    public class ProductListener implements ActionListener {

        /**
         * Invoked when an action occurs, in this case, opens the {@link ProductView}.
         *
         * @param e The event representing the user's action.
         */
    	
        @Override
        public void actionPerformed(ActionEvent e) {         
            SwingUtilities.invokeLater(() -> {
                ProductView productView = new ProductView();
                productView.setVisible(true);
            });
        }
}
    
    /**
     * An ActionListener implementation for handling employee-related events.
     * This class defines the behavior when specific employee-related actions occur.
     */
    
    public class EmployeeListener implements ActionListener {

        /**
         * Invoked when an action occurs, in this case, opens the {@link EmployeeView}.
         *
         * @param e The event representing the user's action.
         */
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                EmployeeView employeeView = new EmployeeView();
                employeeView.setVisible(true);
            });
        }
    }

    /**
     * An ActionListener implementation for handling customer-related events.
     * This class defines the behavior when specific customer-related actions occur.
     */
    
    public class CustomerListener implements ActionListener {

        /**
         * Invoked when the "Customers" button is pressed.
         * Opens the CustomerView.
         *
         * @param e The ActionEvent representing the button press.
         */
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                CustomerView customerView = new CustomerView();
                customerView.setVisible(true);
            });
        }
    }

    /**
     * An ActionListener implementation for handling order-related events.
     * This class defines the behavior when specific order-related actions occur.
     */
    
    public class OrderListener implements ActionListener {

        /**
         * Invoked when an action occurs, in this case, opens the {@link OrderView}.
         *
         * @param e The event representing the user's action.
         */
    	
        @Override
        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(() -> {
                OrderView orderView = new OrderView();
                orderView.setVisible(true);
            });
        }
    }

}


	

    
    
  


