package uk.ac.bradford.cds.cw2.votecounter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class VotingCentreNode implements Runnable {

    private final String centreId;
    private final String host;
    private final int port;
    private final List<String> parties;
    private final Random random;

    public VotingCentreNode(String centreId, String host, int port, List<String> parties) {
        this.centreId = centreId;
        this.host = host;
        this.port = port;
        this.parties = parties;
        this.random = new Random();
    }

    @Override
    public void run() {
        Map<String, Integer> localVotes = generateVotes();
        System.out.println(centreId + " local results: " + localVotes);

        String message = buildMessage(localVotes);

        try (
                Socket socket = new Socket(host, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
                )
        ) {
            out.println(message);
            String response = in.readLine();
            System.out.println(centreId + " server response: " + response);
        } catch (IOException e) {
            System.err.println(centreId + " failed to send votes: " + e.getMessage());
        }
    }

    private Map<String, Integer> generateVotes() {
        Map<String, Integer> votes = new HashMap<>();
        for (String party : parties) {
            // Simple random range for coursework simulation.
            int value = 50 + random.nextInt(151);
            votes.put(party, value);
        }
        return votes;
    }

    private String buildMessage(Map<String, Integer> votes) {
        StringBuilder sb = new StringBuilder();
        sb.append(centreId).append(";");

        for (int i = 0; i < parties.size(); i++) {
            String party = parties.get(i);
            sb.append(party).append("=").append(votes.getOrDefault(party, 0));
            if (i < parties.size() - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }
}
