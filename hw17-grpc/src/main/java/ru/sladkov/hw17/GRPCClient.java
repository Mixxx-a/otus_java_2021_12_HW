package ru.sladkov.hw17;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sladkov.hw17.service.ClientService;
import ru.sladkov.protobuf.generated.NumberServiceGrpc;

public class GRPCClient {
    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var asyncClient = NumberServiceGrpc.newStub(channel);

        ClientService clientService = new ClientService(asyncClient);
        clientService.action();

        channel.shutdown();
    }
}
