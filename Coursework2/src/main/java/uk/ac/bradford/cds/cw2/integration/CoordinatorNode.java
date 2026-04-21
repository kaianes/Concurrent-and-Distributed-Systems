package uk.ac.bradford.cds.cw2.integration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CoordinatorNode {

    private record WorkerEndpoint(String host, int port) { }
    private record UserInput(String functionType, String functionData, double a, double b, int n) { }

    public static void main(String[] args) {
        WorkerEndpoint worker1;
        WorkerEndpoint worker2;

        if (args.length == 0) {
            worker1 = new WorkerEndpoint("localhost", 6001);
            worker2 = new WorkerEndpoint("localhost", 6002);
        } else if (args.length == 4) {
            try {
                worker1 = new WorkerEndpoint(args[0], Integer.parseInt(args[1]));
                worker2 = new WorkerEndpoint(args[2], Integer.parseInt(args[3]));
            } catch (NumberFormatException ex) {
                System.err.println("Ports must be numeric.");
                return;
            }
        } else {
            System.err.println("Usage: CoordinatorNode [workerHost1 workerPort1 workerHost2 workerPort2]");
            return;
        }

        UserInput input = readUserInput();
        if (input == null) {
            return;
        }

        String taskMessage = "TASK|" + input.functionType() + "|" + input.functionData() + "|"
                + input.a() + "|" + input.b() + "|" + input.n();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<String> future1 = executor.submit(() -> contactWorker(worker1, taskMessage));
            Future<String> future2 = executor.submit(() -> contactWorker(worker2, taskMessage));

            String response1 = getFutureResult(future1);
            String response2 = getFutureResult(future2);

            printComparison(input, response1, response2);
        } finally {
            executor.shutdown();
        }
    }

    private static UserInput readUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Function type? Enter PREDEF or POLY:");
        String functionType = scanner.nextLine().trim().toUpperCase();

        String functionData;
        if ("PREDEF".equals(functionType)) {
            System.out.println("Choose predefined function: SIN, COS, or EXP");
            functionData = scanner.nextLine().trim().toUpperCase();
        } else if ("POLY".equals(functionType)) {
            System.out.println("Enter polynomial coefficients (highest degree first), comma separated:");
            functionData = scanner.nextLine().trim();
        } else {
            System.err.println("Invalid function format: type must be PREDEF or POLY.");
            return null;
        }

        try {
            // Validate function before sending work to remote nodes.
            MathUtils.validateFunction(functionType, functionData);
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            return null;
        }

        try {
            System.out.println("Enter a:");
            double a = Double.parseDouble(scanner.nextLine().trim());

            System.out.println("Enter b:");
            double b = Double.parseDouble(scanner.nextLine().trim());

            if (a >= b) {
                System.err.println("Invalid interval: a must be < b.");
                return null;
            }

            System.out.println("Enter number of subintervals n:");
            int n = Integer.parseInt(scanner.nextLine().trim());
            if (n <= 0) {
                System.err.println("Invalid number of subintervals: n must be > 0.");
                return null;
            }

            return new UserInput(functionType, functionData, a, b, n);
        } catch (NumberFormatException ex) {
            System.err.println("Invalid numeric input.");
            return null;
        }
    }

    private static String contactWorker(WorkerEndpoint endpoint, String taskMessage) {
        try (
                Socket socket = new Socket(endpoint.host(), endpoint.port());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
                )
        ) {
            out.println(taskMessage);
            String response = in.readLine();
            if (response == null || response.isBlank()) {
                return "ERR|Empty worker response";
            }
            return response;
        } catch (IOException ex) {
            return "ERR|Connection error: " + ex.getMessage();
        }
    }

    private static String getFutureResult(Future<String> future) {
        try {
            return future.get();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return "ERR|Interrupted while waiting for worker response";
        } catch (ExecutionException ex) {
            return "ERR|Worker task failed: " + ex.getMessage();
        }
    }

    private static void printComparison(UserInput input, String response1, String response2) {
        System.out.println();
        System.out.println("Integration input:");
        System.out.println("Function type: " + input.functionType());
        System.out.println("Function data: " + input.functionData());
        System.out.println("Interval: [" + input.a() + ", " + input.b() + "]");
        System.out.println("Subintervals n: " + input.n());

        System.out.println();
        ParsedResult r1 = parseWorkerReply(response1);
        ParsedResult r2 = parseWorkerReply(response2);

        printWorkerResult(r1);
        printWorkerResult(r2);

        if (!r1.success || !r2.success) {
            System.out.println();
            System.out.println("Comparison unavailable because at least one worker failed.");
            return;
        }

        double difference = Math.abs(r1.value - r2.value);
        String fasterMethod = r1.timeMs <= r2.timeMs ? r1.method : r2.method;

        System.out.println();
        System.out.println("Absolute difference between methods: " + difference);
        System.out.println("Faster method: " + fasterMethod);
    }

    private static void printWorkerResult(ParsedResult result) {
        if (result.success) {
            System.out.println(result.method + " -> value=" + result.value + ", timeMs=" + result.timeMs);
        } else {
            System.out.println("ERROR -> " + result.errorMessage);
        }
    }

    private static ParsedResult parseWorkerReply(String line) {
        if (line == null || line.isBlank()) {
            return ParsedResult.error("Invalid worker response");
        }

        String[] parts = line.split("\\|", -1);
        if (parts.length == 4 && "OK".equals(parts[0])) {
            try {
                String method = parts[1];
                double value = Double.parseDouble(parts[2]);
                long timeMs = Long.parseLong(parts[3]);
                return ParsedResult.success(method, value, timeMs);
            } catch (NumberFormatException ex) {
                return ParsedResult.error("Invalid worker numeric values");
            }
        }

        if (parts.length >= 2 && "ERR".equals(parts[0])) {
            return ParsedResult.error(parts[1]);
        }

        return ParsedResult.error("Invalid worker response format");
    }

    private static class ParsedResult {
        final String method;
        final double value;
        final long timeMs;
        final boolean success;
        final String errorMessage;

        private ParsedResult(String method, double value, long timeMs, boolean success, String errorMessage) {
            this.method = method;
            this.value = value;
            this.timeMs = timeMs;
            this.success = success;
            this.errorMessage = errorMessage;
        }

        static ParsedResult success(String method, double value, long timeMs) {
            return new ParsedResult(method, value, timeMs, true, null);
        }

        static ParsedResult error(String errorMessage) {
            return new ParsedResult("UNKNOWN", 0.0, 0L, false, errorMessage);
        }
    }
}
