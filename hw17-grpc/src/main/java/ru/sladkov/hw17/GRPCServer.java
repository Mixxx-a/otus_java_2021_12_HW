package ru.sladkov.hw17;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.sladkov.hw17.service.RemoteNumberService;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new RemoteNumberService())
                .build();
        server.start();
        System.out.println("server waiting for client connections...");
        server.awaitTermination();
    }
}
