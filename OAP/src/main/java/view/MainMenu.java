package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The MainMenu class represents a panel containing buttons for navigating the main menu of an application.
 * This panel is typically used as a main menu screen with options like "Products," "Orders," "Customers," and "Employees."
 * 
 * @author 7120
 */
public class MainMenu extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a MainMenu panel with specified action listeners for its buttons.
     *
     * @param productsListener   The action listener for the "Products" button.
     * @param orderListener     The action listener for the "Orders" button.
     * @param customersListener The action listener for the "Customers" button.
     * @param employeesListener The action listener for the "Employees" button.
     */
    public MainMenu(ActionListener productsListener, ActionListener orderListener, ActionListener customersListener, ActionListener employeesListener) {
        setBackground(Color.WHITE);

        JButton productsButton = createButton("Products", productsListener);
        JButton ordersButton = createButton("Orders", orderListener);
        JButton customersButton = createButton("Customers", customersListener);
        JButton employeesButton = createButton("Employees", employeesListener);

        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        add(productsButton);
        add(ordersButton);
        add(customersButton);
        add(employeesButton);
    }

    /**
     * Creates a customized JButton with the specified text and action listener.
     *
     * @param text     The text to display on the button.
     * @param listener The action listener to attach to the button.
     * @return A customized JButton.
     */
    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        button.setPreferredSize(new Dimension(120, 50));
        return button;
    }
}
