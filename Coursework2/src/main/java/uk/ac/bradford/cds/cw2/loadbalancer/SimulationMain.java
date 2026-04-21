package uk.ac.bradford.cds.cw2.loadbalancer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SimulationMain {

    public static void main(String[] args) throws InterruptedException {
        Thread server1 = new Thread(new ServerNode("S1", 6001));
        Thread server2 = new Thread(new ServerNode("S2", 6002));
        Thread server3 = new Thread(new ServerNode("S3", 6003));

        server1.start();
        server2.start();
        server3.start();

        String[] serverIds = {"S1", "S2", "S3"};
        String[] serverHosts = {"localhost", "localhost", "localhost"};
        int[] serverPorts = {6001, 6002, 6003};

        Thread loadBalancer = new Thread(new LoadBalancerNode(5000, serverIds, serverHosts, serverPorts));
        loadBalancer.start();

        Thread.sleep(500);

        List<Thread> clients = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            String requestId = "C" + i;
            Thread client = new Thread(() -> sendClientRequest("localhost", 5000, requestId));
            clients.add(client);
            client.start();
        }

        for (Thread client : clients) {
            client.join();
        }

        System.out.println("All clients finished. Load balancer and servers are still running.");
    }

    private static void sendClientRequest(String host, int port, String requestId) {
        if (requestId == null || requestId.isBlank()) {
            System.err.println("Client error: request ID is empty.");
            return;
        }

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("REQ|" + requestId);
            String response = in.readLine();
            System.out.println("Client " + requestId + " got: " + response);
        } catch (IOException e) {
            System.err.println("Client " + requestId + " failed: " + e.getMessage());
        }
    }
}
