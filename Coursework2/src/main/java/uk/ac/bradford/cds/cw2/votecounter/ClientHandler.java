package uk.ac.bradford.cds.cw2.votecounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final CentralAggregatorServer server;

    public ClientHandler(Socket socket, CentralAggregatorServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (
                Socket clientSocket = socket;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8)
                );
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String line = in.readLine();
            Map<String, Integer> votes = parseMessage(line);

            if (votes == null) {
                out.println("ERROR");
                return;
            }

            server.addVotes(votes);
            out.println("ACK");
        } catch (IOException e) {
            System.err.println("Client handler error: " + e.getMessage());
        }
    }

    // Expected format: centreId;PartyA=10,PartyB=15,PartyC=7
    private Map<String, Integer> parseMessage(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        String[] parts = line.split(";", 2);
        if (parts.length != 2) {
            return null;
        }

        String centreId = parts[0].trim();
        String votesPart = parts[1].trim();

        if (centreId.isEmpty() || votesPart.isEmpty()) {
            return null;
        }

        String[] pairs = votesPart.split(",");
        Map<String, Integer> votes = new HashMap<>();

        for (String pair : pairs) {
            String[] voteData = pair.split("=", 2);
            if (voteData.length != 2) {
                return null;
            }

            String party = voteData[0].trim();
            String valueText = voteData[1].trim();

            if (party.isEmpty() || valueText.isEmpty()) {
                return null;
            }

            int value;
            try {
                value = Integer.parseInt(valueText);
            } catch (NumberFormatException e) {
                return null;
            }

            if (value < 0) {
                return null;
            }

            int current = votes.getOrDefault(party, 0);
            votes.put(party, current + value);
        }

        return votes;
    }
}
