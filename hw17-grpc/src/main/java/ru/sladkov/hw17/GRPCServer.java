package ru.sladkov.hw17;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sladkov.hw17.service.NumberService;

import java.io.IOException;

public class GRPCServer {
    public static final int SERVER_PORT = 8190;
    private static final Logger logger = LoggerFactory.getLogger(GRPCServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new NumberService())
                .build();
        server.start();

        Thread shutdownHook = new Thread(() -> {
            logger.info("server shutting down...");
            server.shutdown();
            logger.info("server shut down");
        });

        Runtime.getRuntime().addShutdownHook(shutdownHook);

        logger.info("server waiting for client connections...");
        server.awaitTermination();
    }
}
