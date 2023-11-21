import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {

    public MainView() {
        // Set title and size
        super("Model Perfect");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set background color
        getContentPane().setBackground(Color.WHITE);

        // Create panel for company name and logo
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);

        // Add logo (replace with your own implementation)
        ImageIcon companyLogo = new ImageIcon("Model Perfect.png");
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
                new ProductView().setVisible(true);
            });
        }
    }

    // Static inner class for "Orders" button in MainMenu
    private static class OrderListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open OrderView when "Orders" button is pressed
            SwingUtilities.invokeLater(() -> {
                new OrderView().setVisible(true);
            });
        }
    }

    // Static inner class for "Customers" button in MainMenu
    private static class CustomersListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open CustomerView when "Customers" button is pressed
            SwingUtilities.invokeLater(() -> {
                new CustomerView(null).setVisible(true);
            });
        }
    }

// Static inner class for "Employees" button in MainMenu
private static class EmployeesListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Open EmployeeView when "Employees" button is pressed
        new EmployeeView().setVisible(true);
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}