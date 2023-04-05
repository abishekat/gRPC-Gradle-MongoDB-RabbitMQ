package cu.dssassignment2.mongospringutil.grpc.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class EduCostStatServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = 6565;

        Server server = ServerBuilder
                .forPort(port).build();

        server.start();
        System.out.println("Server Started");
        System.out.println("Listening on POrt:" + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received Shutdown Request");
            server.shutdown();
            System.out.println("Server Stopped");
        }));
        server.awaitTermination();
    }

}
