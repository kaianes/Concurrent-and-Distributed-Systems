package uk.ac.bradford.cds.cw2.votecounter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainSimulation {

    public static void main(String[] args) throws InterruptedException {
        int port = 59010;
        CentralAggregatorServer server = new CentralAggregatorServer(port, 10);

        Thread serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                System.err.println("Server stopped: " + e.getMessage());
            }
        });
        serverThread.start();

        // Small delay so server starts before clients connect.
        Thread.sleep(500);

        List<String> parties = List.of("PartyA", "PartyB", "PartyC");
        int centreCount = 4; // Between 3 and 5 as requested.
        List<Thread> centreThreads = new ArrayList<>();

        for (int i = 1; i <= centreCount; i++) {
            VotingCentreNode centre = new VotingCentreNode("C" + i, "localhost", port, parties);
            Thread centreThread = new Thread(centre);
            centreThreads.add(centreThread);
            centreThread.start();
        }

        for (Thread centreThread : centreThreads) {
            centreThread.join();
        }

        // Give server handlers a moment to finish updates.
        Thread.sleep(500);

        server.printFinalResults();
        server.stop();
        serverThread.join(1000);
    }
}
