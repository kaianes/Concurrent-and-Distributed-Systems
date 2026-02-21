import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class CafeFrame extends JFrame {
    private final JSpinner customersSpinner = new JSpinner(new SpinnerNumberModel(6, 1, 200, 1));
    private final JSpinner staffSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 50, 1));
    private final JSpinner cakesSpinner = new JSpinner(new SpinnerNumberModel(2, 0, 200, 1));
    private final JSpinner teasSpinner = new JSpinner(new SpinnerNumberModel(2, 0, 200, 1));
    private final JSpinner coffeesSpinner = new JSpinner(new SpinnerNumberModel(2, 0, 200, 1));
    private final JSpinner sessionSecondsSpinner = new JSpinner(new SpinnerNumberModel(120, 10, 7200, 10));
    private final JComboBox<String> speedCombo = new JComboBox<>(new String[]{"Slow", "Normal", "Fast"});

    private final JButton startButton = new JButton("Start");
    private final JButton stopButton = new JButton("Stop");
    private final JButton addCustomerButton = new JButton("Add Customer");

    private final JLabel statusLabel = new JLabel("Status: stopped");
    private final JLabel buffetLabel = new JLabel("Buffet: (0 cakes, 0 teas, 0 coffees)");
    private final JLabel actorsLabel = new JLabel("Actors: customers 0, staff 0");
    private final JLabel timerLabel = new JLabel("Time left: 0s");

    private final JTextArea logArea = new JTextArea(18, 80);

    private final SessionController controller = new SessionController(this::log);
    private final Timer uiTimer;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public CafeFrame() {
        super("Betty's Cafe - Mother's Day Buffet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 650);
        setLocationRelativeTo(null);
        buildUi();
        wireActions();

        uiTimer = new Timer(500, e -> refreshSnapshot());
        uiTimer.start();
        refreshButtons(false);
    }

    private void buildUi() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new GridLayout(2, 1, 8, 8));
        top.add(buildConfigPanel());
        top.add(buildStatusPanel());

        logArea.setEditable(false);
        JScrollPane logPane = new JScrollPane(logArea);
        logPane.setBorder(BorderFactory.createTitledBorder("Verbose Output"));

        root.add(top, BorderLayout.NORTH);
        root.add(logPane, BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel buildConfigPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Simulation Config"));
        panel.add(new JLabel("Customers (N > 5):"));
        panel.add(customersSpinner);
        panel.add(new JLabel("Staff:"));
        panel.add(staffSpinner);
        panel.add(new JLabel("Initial cakes:"));
        panel.add(cakesSpinner);
        panel.add(new JLabel("Initial teas:"));
        panel.add(teasSpinner);
        panel.add(new JLabel("Initial coffees:"));
        panel.add(coffeesSpinner);
        panel.add(new JLabel("Session seconds:"));
        panel.add(sessionSecondsSpinner);
        panel.add(new JLabel("Speed:"));
        panel.add(speedCombo);
        panel.add(startButton);
        panel.add(stopButton);
        panel.add(addCustomerButton);
        return panel;
    }

    private JPanel buildStatusPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Live Status"));
        panel.add(statusLabel);
        panel.add(buffetLabel);
        panel.add(actorsLabel);
        panel.add(timerLabel);
        return panel;
    }

    private void wireActions() {
        startButton.addActionListener(e -> startSimulation());
        stopButton.addActionListener(e -> controller.stopSession());
        addCustomerButton.addActionListener(e -> controller.addCustomer());
    }

    private void startSimulation() {
        int customers = (Integer) customersSpinner.getValue();
        if (customers <= 5) {
            JOptionPane.showMessageDialog(this, "Initial customers must be > 5.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double speedMultiplier = speedMultiplier();

        logArea.setText("");
        try {
            controller.startSession(
                    customers,
                    (Integer) staffSpinner.getValue(),
                    (Integer) cakesSpinner.getValue(),
                    (Integer) teasSpinner.getValue(),
                    (Integer) coffeesSpinner.getValue(),
                    ((Integer) sessionSecondsSpinner.getValue()) * 1000L,
                    speedMultiplier
            );
            refreshButtons(true);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Session", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshSnapshot() {
        boolean running = controller.isOpen();
        statusLabel.setText("Status: " + (running ? "running" : "stopped"));
        buffetLabel.setText("Buffet: " + controller.getCakes() + " cakes, " + controller.getTeas()
                + " teas, " + controller.getCoffees() + " coffees");
        actorsLabel.setText("Actors: customers " + controller.getActiveCustomers() + "/" + controller.getTotalCustomers()
                + ", staff " + controller.getActiveStaff() + "/" + controller.getTotalStaff());
        timerLabel.setText("Time left: " + (controller.getTimeLeftMillis() / 1000L) + "s");
        refreshButtons(running);
    }

    private double speedMultiplier() {
        String speed = (String) speedCombo.getSelectedItem();
        if ("Slow".equals(speed)) {
            return 1.7;
        }
        if ("Fast".equals(speed)) {
            return 0.6;
        }
        return 1.0;
    }

    private void refreshButtons(boolean running) {
        startButton.setEnabled(!running);
        stopButton.setEnabled(running);
        addCustomerButton.setEnabled(running);
    }

    private void log(String message) {
        String line = "[" + LocalTime.now().format(timeFormatter) + "] " + message;
        System.out.println(line);
        SwingUtilities.invokeLater(() -> {
            logArea.append(line);
            logArea.append(System.lineSeparator());
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
}
