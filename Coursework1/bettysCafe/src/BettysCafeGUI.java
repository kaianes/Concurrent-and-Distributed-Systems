import javax.swing.*;
import java.awt.*;
import java.io.File;

public class BettysCafeGUI {
    public static void main(String[] args) {
        // Main window setup
        JFrame frame = new JFrame("Betty's Cafe Control");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 650);
        frame.setLayout(new BorderLayout(10, 10));

        // --- TOP PANEL (Inputs with icons) ---
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left side: Item quantities (Item icons look better as squares, e.g., 40x40)
        JPanel leftInput = new JPanel(new GridBagLayout());
        leftInput.setBorder(BorderFactory.createTitledBorder("Set initial quantities of items!"));
        
        // Define square size for food items
        int itemW = 40; int itemH = 40;
        addInputRow(leftInput, "Tea", "images/tea.png", 0, itemW, itemH);
        addInputRow(leftInput, "Cake", "images/cake.png", 1, itemW, itemH);
        addInputRow(leftInput, "Coffee", "images/coffee.png", 2, itemW, itemH);

        // Right side: Staff and Customers (Characters look better taller, e.g., 50x80)
        JPanel rightInput = new JPanel(new GridBagLayout());
        rightInput.setBorder(BorderFactory.createTitledBorder("Specify initial number of customers and staff!"));
        
        // Define rectangular size for characters to match their aspect ratio
        int charW = 50; int charH = 80;
        addInputRow(rightInput, "Customers", "images/costumer.png", 0, charW, charH);
        addInputRow(rightInput, "Staff", "images/staff.png", 1, charW, charH);

        topPanel.add(leftInput);
        topPanel.add(rightInput);

        // --- CENTER PANEL (Start Button and Output Area) ---
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JButton startButton = new JButton("START");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(150, 50));

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(BorderFactory.createTitledBorder("OUTPUT"));

        centerPanel.add(startButton, BorderLayout.NORTH);
        centerPanel.add(scroll, BorderLayout.CENTER);

        // --- BOTTOM PANEL (Speed Slider and Extra Actions) ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel speedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        speedPanel.add(new JLabel("Adjust the speed:"));
        JSlider speedSlider = new JSlider(0, 100, 50);
        speedPanel.add(speedSlider);

        JButton addCustomerBtn = new JButton("Add a customer!");

        bottomPanel.add(speedPanel, BorderLayout.WEST);
        bottomPanel.add(addCustomerBtn, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Updated helper method to create input rows, now accepting custom width and height
     * for the icon to preserve aspect ratio.
     */
    private static void addInputRow(JPanel panel, String labelText, String imgPath, int row, int imgWidth, int imgHeight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Attempt to load and scale the image with specific width and height
        JLabel label;
        File imgFile = new File(imgPath);
        if (imgFile.exists()) {
            // Updated scaling using the new method parameters
            ImageIcon icon = new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH));
            label = new JLabel(labelText, icon, JLabel.LEFT);
        } else {
            label = new JLabel(labelText + " (No Img)"); // Fallback
        }

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(new JTextField(5), gbc);
    }
}