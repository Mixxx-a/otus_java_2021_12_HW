package ru.sladkov.hw17;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sladkov.hw17.service.ClientService;
import ru.sladkov.protobuf.generated.BorderValuesMessage;
import ru.sladkov.protobuf.generated.NumberMessage;
import ru.sladkov.protobuf.generated.RemoteNumberServiceGrpc;

import java.util.Iterator;

public class GRPCClient {

    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        RemoteNumberServiceGrpc.RemoteNumberServiceBlockingStub stub = RemoteNumberServiceGrpc.newBlockingStub(channel);

        BorderValuesMessage borderValuesMessage = BorderValuesMessage.newBuilder()
                .setFirstValue(0)
                .setLastValue(30)
                .build();

        ClientService clientService = new ClientService();

        Thread thread = new Thread(clientService);
        thread.start();

        Iterator<NumberMessage> messageIterator = stub.getNumberValues(borderValuesMessage);
        messageIterator.forEachRemaining(message -> {
            logger.info("new value from server: " + message.getValue());
            clientService.setLastServerValue(message.getValue());
            clientService.setIsNew(true);
        });

        channel.shutdown();
    }
}
