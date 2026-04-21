package uk.ac.bradford.cds.cw2.loadbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class ServerNode implements Runnable {

    private final String serverId;
    private final int port;

    public ServerNode(String serverId, int port) {
        this.serverId = serverId;
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server " + serverId + " listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleRequest(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server " + serverId + " stopped: " + e.getMessage());
        }
    }

    private void handleRequest(Socket socket) {
        try (Socket s = socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
             PrintWriter out = new PrintWriter(s.getOutputStream(), true)) {

            String line = in.readLine();
            if (line == null) {
                out.println("ERR|BAD_REQUEST");
                return;
            }

            String[] parts = line.split("\\|");
            if (parts.length != 3 || !"PROCESS".equals(parts[0]) || parts[1].isBlank()) {
                out.println("ERR|BAD_REQUEST");
                return;
            }

            try {
                Long.parseLong(parts[2]);
            } catch (NumberFormatException e) {
                out.println("ERR|BAD_REQUEST");
                return;
            }

            long startTime = System.currentTimeMillis();
            Thread.sleep(ThreadLocalRandom.current().nextInt(200, 600));
            long endTime = System.currentTimeMillis();

            out.println("DONE|" + parts[1] + "|" + serverId + "|" + startTime + "|" + endTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.err.println("Server " + serverId + " request error: " + e.getMessage());
        }
    }
}
