import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && "--console".equalsIgnoreCase(args[0])) {
            runConsoleDemo();
            return;
        }

        SwingUtilities.invokeLater(() -> {
            CafeFrame frame = new CafeFrame();
            frame.setVisible(true);
        });
    }

    private static void runConsoleDemo() {
        SessionController controller = new SessionController(System.out::println);
        controller.startSession(6, 3, 2, 2, 2, 120_000L, 1.0);
        try {
            Thread.sleep(121_500L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            controller.stopSession();
        }
    }
}
