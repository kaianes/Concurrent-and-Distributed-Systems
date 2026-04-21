package uk.ac.bradford.cds.cw2.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class WorkerNode {

    public enum Method {
        TRAPEZOIDAL,
        MIDPOINT
    }

    private final Method method;
    private final int port;

    public WorkerNode(Method method, int port) {
        this.method = method;
        this.port = port;
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Worker " + method + " listening on port " + port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    handleClient(socket);
                } catch (IOException e) {
                    System.err.println("Worker connection error: " + e.getMessage());
                }
            }
        }
    }

    private void handleClient(Socket socket) {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
                );
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            try {
                String line = in.readLine();
                Task task = parseTask(line);

                if (task.a >= task.b) {
                    out.println("ERR|Invalid interval: a must be < b");
                    return;
                }
                if (task.n <= 0) {
                    out.println("ERR|Invalid number of subintervals: n must be > 0");
                    return;
                }

                MathUtils.validateFunction(task.functionType, task.functionData);

                long startNs = System.nanoTime();
                double value = calculate(task.functionType, task.functionData, task.a, task.b, task.n);
                long elapsedMs = (System.nanoTime() - startNs) / 1_000_000;

                out.println("OK|" + method.name() + "|" + value + "|" + elapsedMs);
            } catch (IllegalArgumentException ex) {
                out.println("ERR|" + ex.getMessage().replace("|", "/"));
            }
        } catch (IOException ex) {
            System.err.println("Worker I/O error: " + ex.getMessage());
        }
    }

    private double calculate(String functionType, String functionData, double a, double b, int n) {
        return switch (method) {
            case TRAPEZOIDAL -> MathUtils.trapezoidal(functionType, functionData, a, b, n);
            case MIDPOINT -> MathUtils.midpoint(functionType, functionData, a, b, n);
        };
    }

    private Task parseTask(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Invalid message format");
        }

        String[] parts = line.split("\\|", -1);
        if (parts.length != 6 || !"TASK".equals(parts[0])) {
            throw new IllegalArgumentException("Invalid message format");
        }

        try {
            double a = Double.parseDouble(parts[3].trim());
            double b = Double.parseDouble(parts[4].trim());
            int n = Integer.parseInt(parts[5].trim());
            return new Task(parts[1].trim(), parts[2].trim(), a, b, n);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid message format");
        }
    }

    private static class Task {
        final String functionType;
        final String functionData;
        final double a;
        final double b;
        final int n;

        private Task(String functionType, String functionData, double a, double b, int n) {
            this.functionType = functionType;
            this.functionData = functionData;
            this.a = a;
            this.b = b;
            this.n = n;
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: WorkerNode <TRAPEZOIDAL|MIDPOINT> <PORT>");
            return;
        }

        Method method;
        int port;
        try {
            method = Method.valueOf(args[0].trim().toUpperCase());
            port = Integer.parseInt(args[1].trim());
        } catch (IllegalArgumentException ex) {
            System.err.println("Invalid startup arguments. Usage: WorkerNode <TRAPEZOIDAL|MIDPOINT> <PORT>");
            return;
        }

        if (port <= 0 || port > 65535) {
            System.err.println("Invalid port.");
            return;
        }

        WorkerNode worker = new WorkerNode(method, port);
        worker.start();
    }
}
