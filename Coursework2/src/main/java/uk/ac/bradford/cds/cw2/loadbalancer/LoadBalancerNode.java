package uk.ac.bradford.cds.cw2.loadbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class LoadBalancerNode implements Runnable {

    private final int loadBalancerPort;
    private final String[] serverIds;
    private final String[] serverHosts;
    private final int[] serverPorts;
    private int nextServerIndex;

    public LoadBalancerNode(int loadBalancerPort, String[] serverIds, String[] serverHosts, int[] serverPorts) {
        if (serverIds == null || serverHosts == null || serverPorts == null
                || serverIds.length == 0
                || serverIds.length != serverHosts.length
                || serverIds.length != serverPorts.length) {
            throw new IllegalArgumentException("Server arrays must be non-empty and same length.");
        }
        this.loadBalancerPort = loadBalancerPort;
        this.serverIds = serverIds;
        this.serverHosts = serverHosts;
        this.serverPorts = serverPorts;
        this.nextServerIndex = 0;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(loadBalancerPort)) {
            System.out.println("Load balancer listening on port " + loadBalancerPort);
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Load balancer stopped: " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (Socket socket = clientSocket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String request = in.readLine();
            if (request == null) {
                out.println("ERR|BAD_REQUEST");
                return;
            }

            String[] requestParts = request.split("\\|");
            if (requestParts.length != 2 || !"REQ".equals(requestParts[0]) || requestParts[1].isBlank()) {
                out.println("ERR|BAD_REQUEST");
                return;
            }

            String requestId = requestParts[1];
            long timeReceived = System.currentTimeMillis();

            int serverIndex = getNextServerIndex();
            String assignedServerId = serverIds[serverIndex];

            String serverResponse = sendToServer(serverIndex, requestId, timeReceived);
            if (serverResponse == null) {
                out.println("ERR|SERVER_UNAVAILABLE");
                return;
            }

            String[] responseParts = serverResponse.split("\\|");
            if (responseParts.length != 5 || !"DONE".equals(responseParts[0])) {
                out.println("ERR|BAD_SERVER_RESPONSE");
                return;
            }

            if (responseParts[1].isBlank() || responseParts[2].isBlank()) {
                out.println("ERR|BAD_SERVER_RESPONSE");
                return;
            }

            long startTime;
            long endTime;
            try {
                startTime = Long.parseLong(responseParts[3]);
                endTime = Long.parseLong(responseParts[4]);
            } catch (NumberFormatException e) {
                out.println("ERR|BAD_SERVER_RESPONSE");
                return;
            }

            System.out.println("requestId=" + requestId
                    + ", timeReceived=" + timeReceived
                    + ", serverId=" + assignedServerId
                    + ", startTime=" + startTime
                    + ", endTime=" + endTime);

            out.println("OK|" + requestId);
        } catch (IOException e) {
            System.err.println("Load balancer client error: " + e.getMessage());
        }
    }

    private synchronized int getNextServerIndex() {
        int selected = nextServerIndex;
        nextServerIndex = (nextServerIndex + 1) % serverIds.length;
        return selected;
    }

    private String sendToServer(int serverIndex, String requestId, long timeReceived) {
        try (Socket serverSocket = new Socket(serverHosts[serverIndex], serverPorts[serverIndex]);
             BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
             PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true)) {

            out.println("PROCESS|" + requestId + "|" + timeReceived);
            return in.readLine();
        } catch (IOException e) {
            System.err.println("Load balancer cannot reach server " + serverIds[serverIndex] + ": " + e.getMessage());
            return null;
        }
    }
}
