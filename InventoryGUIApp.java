package inventoryapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Interface for inventory operations
interface InventoryOperations<T extends Product> {
    void addProduct(T product);
    void removeProduct(int id);
    void displayProducts(JTable table);
    void searchProduct(String name, JTextArea outputArea);
    void saveToFile(String filename) throws IOException;
    void loadFromFile(String filename) throws IOException, ClassNotFoundException;
}

// Abstract product class
abstract class Product implements Serializable {
    protected int id;
    protected String name;
    protected String category;
    protected int quantity;
    protected double price;

    public Product(int id, String name, String category, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public abstract String getProductType();
}

// Concrete class for an electronic product
class ElectronicProduct extends Product {
    public ElectronicProduct(int id, String name, String category, int quantity, double price) {
        super(id, name, category, quantity, price);
    }

    @Override
    public String getProductType() {
        return "Electronic";
    }
}

// Inventory with GUI connection
class InventoryManagementSystem<T extends Product> implements InventoryOperations<T> {
    private List<T> inventory;

    public InventoryManagementSystem() {
        inventory = new ArrayList<>();
    }

    @Override
    public void addProduct(T product) {
        inventory.add(product);
    }

    @Override
    public void removeProduct(int id) {
        try {
            boolean removed = inventory.removeIf(product -> product.getId() == id);
            if (!removed) throw new Exception("Product not found with ID: " + id);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void displayProducts(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        inventory.forEach(product -> model.addRow(new Object[] {
                product.getId(), product.getName(), product.getCategory(),
                product.getQuantity(), product.getPrice()
        }));
    }

    @Override
    public void searchProduct(String name, JTextArea outputArea) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            outputArea.setText("");
            boolean found = false;
            for (Product product : inventory) {
                if (product.getName().equalsIgnoreCase(name)) {
                    outputArea.append("ID: " + product.getId() +
                            "\nName: " + product.getName() +
                            "\nCategory: " + product.getCategory() +
                            "\nQuantity: " + product.getQuantity() +
                            "\nPrice: " + product.getPrice() + "\n\n");
                    found = true;
                }
            }
            if (!found) {
                outputArea.setText("Product not found.");
            }
        });
        executor.shutdown();
    }

    @Override
    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(inventory);
        }
    }

    @Override
    public void loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            inventory = (List<T>) ois.readObject();
        }
    }

    // Static nested class
    static class Stats {
        static <T extends Product> double calculateAveragePrice(List<T> products) {
            return products.stream().mapToDouble(Product::getPrice).average().orElse(0);
        }
    }
}

// Main GUI class
public class InventoryGUIApp extends JFrame {
    private InventoryManagementSystem<Product> ims = new InventoryManagementSystem<>();
    private JTable table;
    private JTextArea searchOutputArea;

    public InventoryGUIApp() {
        setTitle("Inventory Management System");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Name", "Category", "Quantity", "Price"};
        table = new JTable(new DefaultTableModel(columnNames, 0));
        table.setBackground(new Color(240, 240, 255));
        table.setGridColor(Color.GRAY);
        table.setSelectionBackground(new Color(100, 149, 237));
        JScrollPane tableScrollPane = new JScrollPane(table);

        searchOutputArea = new JTextArea(10, 30);
        searchOutputArea.setBackground(new Color(255, 255, 204));
        searchOutputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        searchOutputArea.setForeground(Color.BLACK);
        JScrollPane textAreaScrollPane = new JScrollPane(searchOutputArea);

        JButton searchButton = createButton("Search", () -> ims.searchProduct(searchField.getText(), searchOutputArea));
        JButton addButton = createButton("Add Product", this::addSingleProduct);
        JButton removeButton = createButton("Remove Product", () -> {
            try {
                String input = JOptionPane.showInputDialog("Enter Product ID to remove:");
                ims.removeProduct(Integer.parseInt(input));
                ims.displayProducts(table);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid ID.");
            }
        });

        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(new Color(255, 228, 196));
        panel.add(new JLabel("Search Product by Name:"));
        searchField = new JTextField(15);
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(addButton);
        panel.add(removeButton);

        add(panel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(textAreaScrollPane, BorderLayout.SOUTH);

        addSampleProducts();
        ims.displayProducts(table);

        setVisible(true);
    }

    private JTextField searchField;

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(switch (text) {
            case "Search" -> new Color(100, 200, 150);
            case "Add Product" -> new Color(100, 149, 237);
            case "Remove Product" -> new Color(255, 99, 71);
            default -> new Color(128, 128, 128);
        });
        button.addActionListener(e -> action.run());
        return button;
    }

    private void addSampleProducts() {
        String[] names = {
                "Smartphone", "Laptop", "Headphones", "Monitor", "Keyboard",
                "Mouse", "Smartwatch", "Tablet", "Webcam", "Printer",
                "Speakers", "Router", "Hard Drive", "SSD", "Flash Drive",
                "Microphone", "Drone", "Camera", "TV", "VR Headset"
        };
        String[] categories = {
                "Mobile", "Computers", "Audio", "Display", "Accessories",
                "Accessories", "Wearable", "Mobile", "Accessories", "Office",
                "Audio", "Networking", "Storage", "Storage", "Storage",
                "Audio", "Gadgets", "Photography", "Home Entertainment", "Gadgets"
        };
        int[] quantities = {10, 5, 15, 7, 20, 18, 8, 6, 14, 9, 10, 12, 16, 11, 25, 4, 3, 5, 6, 2};
        double[] prices = {
                29999, 54999, 1999, 10999, 1499,
                999, 12999, 18999, 2599, 8999,
                3499, 1799, 4299, 3599, 799,
                4999, 49999, 15999, 39999, 69999
        };
        for (int i = 0; i < 20; i++) {
            ims.addProduct(new ElectronicProduct(i + 1, names[i], categories[i], quantities[i], prices[i]));
        }
        ims.displayProducts(table);
    }

    private void addSingleProduct() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID:"));
            String name = JOptionPane.showInputDialog("Enter Product Name:");
            String category = JOptionPane.showInputDialog("Enter Category:");
            int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter Quantity:"));
            double price = Double.parseDouble(JOptionPane.showInputDialog("Enter Price:"));

            Product newProduct = new ElectronicProduct(id, name, category, quantity, price);
            ims.addProduct(newProduct);
            ims.displayProducts(table);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please try again.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryGUIApp::new);
    }
}
