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






public class MainView extends JFrame {

    private static final long serialVersionUID = 1L;
    protected JMenuBar menuBar;
    
    

    public MainView() {
        super("Model Perfect");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set application icon
        ImageIcon appIcon = new ImageIcon(getClass().getResource("/images/Model Perfect.png"));
        setIconImage(appIcon.getImage());

        getContentPane().setBackground(Color.WHITE);

        // Initialize the Menubar
        MenuBar menuBar = new MenuBar(rootPaneCheckingEnabled); // Create an instance of MenuBar

        // Set the menu bar for the frame
        setJMenuBar(menuBar.getMenuBar()); // Use the instance to get the JMenuBar
        
        
        // Create panel for company name and logo
        
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        // Sub-panel for the logo
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);

        // Load the image using getClass().getResource()
        ImageIcon companyLogo = new ImageIcon(getClass().getResource("/images/Model Perfect.png"));
        JLabel logoLabel = new JLabel(companyLogo);
        logoPanel.add(logoLabel);

        // Sub-panel for the company name
        JPanel companyNamePanel = new JPanel();
        companyNamePanel.setBackground(Color.WHITE);

        // Create a label for the company name with a cooler font
        Font coolFont = new Font("Segoe UI", Font.BOLD, 24);
        JLabel companyNameLabel = new JLabel("Model Perfect Inc.");
        companyNameLabel.setFont(coolFont);
        companyNameLabel.setForeground(Color.BLACK);
        companyNamePanel.add(companyNameLabel);

        // Add sub-panels to the main panel
        mainPanel.add(logoPanel);
        mainPanel.add(companyNamePanel);

        // Add the main panel to the center of the window
        add(mainPanel, BorderLayout.CENTER);

        // Center the window on the screen
        setLocationRelativeTo(null);
        
        JButton employeesButton = new JButton("Employees");
        
        

        // Create ProductsListener for "Products" button in MainMenu
        ProductListener productsListener = new ProductListener();
        
        
        // Create OrderListener for "Orders" button in MainMenu
        OrderListener orderListener = new OrderListener();

        // Create CustomersListener for "Customers" button in MainMenu
        CustomerListener customersListener = new CustomerListener();

        // Create EmployeesListener for "Employees" button in MainMenu
        EmployeeListener employeeHandler = new EmployeeListener ();
        employeesButton.addActionListener(employeeHandler);

        // Add MainMenu panel at the top
        MainMenu mainMenu = new MainMenu(productsListener, orderListener, customersListener, employeeHandler);
        add(mainMenu, BorderLayout.SOUTH);

        // Center the window on the screen
        setLocationRelativeTo(null);
    }

    public class ProductListener implements ActionListener {

        /**
         * Invoked when an action occurs, in this case, opens the {@link ProductView}.
         *
         * @param e The event representing the user's action.
         */
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
    
    public class EmployeeListener implements ActionListener {

        /**
         * Invoked when an action occurs, in this case, opens the {@link EmployeeView}.
         *
         * @param e The event representing the user's action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open EmployeeView when "Employees" button is pressed
            SwingUtilities.invokeLater(() -> {
                EmployeeView employeeView = new EmployeeView();
                employeeView.setVisible(true);
            });
        }
    }
    
    public class CustomerListener implements ActionListener {

        /**
         * Invoked when the "Customers" button is pressed.
         * Opens the CustomerView.
         *
         * @param e The ActionEvent representing the button press.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open CustomerView when "Customers" button is pressed
            SwingUtilities.invokeLater(() -> {
                CustomerView customerView = new CustomerView();
                customerView.setVisible(true);
            });
        }
    }
    
    public class OrderListener implements ActionListener {

        /**
         * Invoked when an action occurs, in this case, opens the {@link OrderView}.
         *
         * @param e The event representing the user's action.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open OrderView when "Orders" button is pressed
            SwingUtilities.invokeLater(() -> {
                OrderView orderView = new OrderView();
                orderView.setVisible(true);
            });
        }
    }

}
	

    
    
  


