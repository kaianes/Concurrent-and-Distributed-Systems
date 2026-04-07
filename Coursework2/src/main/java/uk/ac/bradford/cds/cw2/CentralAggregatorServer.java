package uk.ac.bradford.cds.cw2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CentralAggregatorServer {

    private final int port;
    private final ExecutorService pool;
    private final Map<String, Integer> totalVotes;
    private volatile boolean running;
    private ServerSocket serverSocket;

    public CentralAggregatorServer(int port, int workerThreads) {
        this.port = port;
        this.pool = Executors.newFixedThreadPool(workerThreads);
        this.totalVotes = new HashMap<>();
    }

    public void start() throws IOException {
        running = true;
        try (ServerSocket ss = new ServerSocket(port)) {
            this.serverSocket = ss;
            System.out.println("CentralAggregatorServer running on port " + port);
            while (running) {
                try {
                    Socket clientSocket = ss.accept();
                    pool.execute(new ClientHandler(clientSocket, this));
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Accept error: " + e.getMessage());
                    }
                }
            }
        } finally {
            pool.shutdown();
            running = false;
            serverSocket = null;
        }
    }

    // Shared state update is synchronized to avoid race conditions.
    public synchronized void addVotes(Map<String, Integer> votes) {
        for (Map.Entry<String, Integer> entry : votes.entrySet()) {
            String party = entry.getKey();
            int count = entry.getValue();
            int current = totalVotes.getOrDefault(party, 0);
            totalVotes.put(party, current + count);
        }
    }

    public synchronized Map<String, Integer> getTotals() {
        return new HashMap<>(totalVotes);
    }

    public synchronized String getWinner() {
        String winner = null;
        int max = -1;

        for (Map.Entry<String, Integer> entry : totalVotes.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                winner = entry.getKey();
            }
        }

        return winner;
    }

    public synchronized void printFinalResults() {
        System.out.println("\nFinal totals:");
        for (Map.Entry<String, Integer> entry : totalVotes.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        String winner = getWinner();
        System.out.println("Winner: " + (winner == null ? "No votes received" : winner));
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException ignored) {
            // Ignore errors during shutdown in this simple coursework version.
        }
    }

    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 59010;
        int workers = args.length > 1 ? Integer.parseInt(args[1]) : 10;

        CentralAggregatorServer server = new CentralAggregatorServer(port, workers);
        server.start();
    }
}
