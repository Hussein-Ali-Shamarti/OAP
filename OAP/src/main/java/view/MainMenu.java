import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {

    public MainMenu(ActionListener productsListener, ActionListener orderListener, ActionListener customersListener, ActionListener employeesListener) {
        setBackground(Color.WHITE); // White background

        // Create buttons with listeners
        JButton productsButton = createButton("Products", productsListener);
        JButton ordersButton = createButton("Orders", orderListener);
        JButton customersButton = createButton("Customers", customersListener);
        JButton employeesButton = createButton("Employees", employeesListener);

        // Add buttons in a horizontal row
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        add(productsButton);
        add(ordersButton);
        add(customersButton);
        add(employeesButton);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE); // White text color
        button.setBackground(new Color(84, 11, 131)); // RGB(84, 11, 131) background color
        button.setFocusPainted(false); // Remove focus highlighting for better appearance
        button.addActionListener(listener); // Add the listener
        button.setPreferredSize(new Dimension(120, 50)); // Smaller size
        return button;
    }
}