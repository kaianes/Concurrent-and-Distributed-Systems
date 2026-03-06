import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class BettysCafeGUI {
    private static final long SESSION_DURATION_MS = 30_000L;

    public static void main(String[] args) {
        // Main window setup
        JFrame frame = new JFrame("Betty's Cafe Control");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 650);
        frame.setLayout(new BorderLayout(10, 10));

        //  TOP PANEL (Inputs with icons) 
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left side: Item quantities
        JPanel leftInput = new JPanel(new GridBagLayout());
        leftInput.setBorder(BorderFactory.createTitledBorder("Set initial quantities of items!"));
        
        // Define square size for food items
        int itemW = 40; int itemH = 40;
        JTextField teaField = addInputRow(leftInput, "Tea", "images/tea.png", 0, itemW, itemH);
        JTextField cakeField = addInputRow(leftInput, "Cake", "images/cake.png", 1, itemW, itemH);
        JTextField coffeeField = addInputRow(leftInput, "Coffee", "images/coffee.png", 2, itemW, itemH);
        teaField.setText("2");
        cakeField.setText("2");
        coffeeField.setText("2");

        // Right side: Staff and Customers 
        JPanel rightInput = new JPanel(new GridBagLayout());
        rightInput.setBorder(BorderFactory.createTitledBorder("Specify initial number of customers and staff!"));
        
        // Define rectangular size for characters
        int charW = 50; int charH = 80;
        JTextField customersField = addInputRow(rightInput, "Customers", "images/costumer.png", 0, charW, charH);
        JTextField staffField = addInputRow(rightInput, "Staff", "images/staff.png", 1, charW, charH);
        customersField.setText("6");
        staffField.setText("3");

        topPanel.add(leftInput);
        topPanel.add(rightInput);

        //  CENTER PANEL (Start Button and Output Area) 
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

        //  BOTTOM PANEL (Speed Slider and Extra Actions) 
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

        // Runtime state shared by button actions.
        List<Thread> actors = Collections.synchronizedList(new ArrayList<>());
        final CafeSession[] sessionRef = new CafeSession[1];
        final Buffet[] buffetRef = new Buffet[1];
        final Piano[] pianoRef = new Piano[1];
        final int[] nextCustomerId = new int[1];

        startButton.addActionListener(e -> {
            if (sessionRef[0] != null && sessionRef[0].isOpen()) {
                appendLine(outputArea, "Session is already running.");
                return;
            }

            int teas = parseNonNegative(teaField.getText(), 2);
            int cakes = parseNonNegative(cakeField.getText(), 2);
            int coffees = parseNonNegative(coffeeField.getText(), 2);
            int customerCount = parseNonNegative(customersField.getText(), 6);
            int staffCount = parseNonNegative(staffField.getText(), 3);

            // 50 = normal speed. Lower = much slower. Higher = much faster.
            double speedMultiplier = sliderToMultiplier(speedSlider.getValue());

            CafeSession session = new CafeSession(speedMultiplier);
            Buffet buffet = new Buffet(cakes, teas, coffees);
            Piano piano = new Piano();
            Consumer<String> logger = message -> SwingUtilities.invokeLater(() -> appendLine(outputArea, message));

            outputArea.setText("");
            synchronized (actors) {
                actors.clear();
            }
            sessionRef[0] = session;
            buffetRef[0] = buffet;
            pianoRef[0] = piano;
            nextCustomerId[0] = customerCount + 1;

            startButton.setEnabled(false);
            appendLine(outputArea, "Starting program with " + customerCount + " clients and " + staffCount + " staff");
            appendLine(outputArea, buffet.buffetText());

            Thread runner = new Thread(() -> {
                try {
                    ItemType[] roles = {ItemType.TEA, ItemType.CAKE, ItemType.COFFEE};
                    for (int i = 0; i < staffCount; i++) {
                        String staffName = "Staff-" + (i + 1);
                        ItemType role = roles[i % roles.length];
                        Thread staffThread = new Thread(new Staff(staffName, role, session, buffet, logger), staffName);
                        actors.add(staffThread);
                        staffThread.start();
                    }

                    for (int i = 0; i < customerCount; i++) {
                        String customerName = "Client-" + (i + 1);
                        Thread customerThread = new Thread(new Customer(customerName, session, buffet, piano, logger), customerName);
                        actors.add(customerThread);
                        customerThread.start();
                    }

                    Thread.sleep(SESSION_DURATION_MS);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } finally {
                    shutdown(session, buffet, actors, logger);
                    sessionRef[0] = null;
                    buffetRef[0] = null;
                    pianoRef[0] = null;
                    SwingUtilities.invokeLater(() -> startButton.setEnabled(true));
                }
            }, "Cafe-Runner");

            runner.start();
        });

        addCustomerBtn.addActionListener(e -> {
            CafeSession session = sessionRef[0];
            Buffet buffet = buffetRef[0];
            Piano piano = pianoRef[0];

            if (session == null || buffet == null || piano == null || !session.isOpen()) {
                appendLine(outputArea, "Start the session first.");
                return;
            }

            String customerName = "Client-" + nextCustomerId[0]++;
            Consumer<String> logger = message -> SwingUtilities.invokeLater(() -> appendLine(outputArea, message));
            Thread customerThread = new Thread(new Customer(customerName, session, buffet, piano, logger), customerName);
            actors.add(customerThread);
            customerThread.start();
            appendLine(outputArea, customerName + " added.");
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Helper methods for GUI setup and actions
    private static JTextField addInputRow(JPanel panel, String labelText, String imgPath, int row, int imgWidth, int imgHeight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel label;
        File imgFile = new File(imgPath);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(new ImageIcon(imgPath).getImage().getScaledInstance(imgWidth, imgHeight, Image.SCALE_SMOOTH));
            label = new JLabel(labelText, icon, JLabel.LEFT);
        } else {
            label = new JLabel(labelText + " (No Img)"); // Fallback
        }

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(label, gbc);

        gbc.gridx = 1;
        JTextField field = new JTextField(5);
        panel.add(field, gbc);
        return field;
    }

    private static int parseNonNegative(String text, int defaultValue) {
        try {
            return Math.max(0, Integer.parseInt(text.trim()));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static double sliderToMultiplier(int sliderValue) {
        int value = Math.max(0, Math.min(100, sliderValue));
        if (value == 50) {
            return 1.0;
        }
        if (value < 50) {
            // 0 -> 10x slower, 50 -> normal
            return 1.0 + ((50 - value) / 50.0) * 9.0;
        }
        // 50 -> normal, 100 -> 10x faster
        return 1.0 - ((value - 50) / 50.0) * 0.9;
    }

    private static void appendLine(JTextArea area, String text) {
        area.append(text + System.lineSeparator());
        area.setCaretPosition(area.getDocument().getLength());
    }

    private static void shutdown(CafeSession session, Buffet buffet, List<Thread> actors, Consumer<String> logger) {
        session.close();
        logger.accept("Session is closing.");
        buffet.signalClosed();

        synchronized (actors) {
            for (Thread actor : actors) {
                actor.interrupt();
            }
            for (Thread actor : actors) {
                try {
                    actor.join(1500L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        int active = 0;
        synchronized (actors) {
            for (Thread actor : actors) {
                if (actor.isAlive()) {
                    active++;
                }
            }
        }
        logger.accept("Session ended. Active threads=" + active + ".");
    }
}
