package ru.sladkov.hw17.service;

import io.grpc.stub.StreamObserver;
import ru.sladkov.protobuf.generated.BorderValuesMessage;
import ru.sladkov.protobuf.generated.NumberMessage;
import ru.sladkov.protobuf.generated.RemoteNumberServiceGrpc;

public class RemoteNumberService extends RemoteNumberServiceGrpc.RemoteNumberServiceImplBase {

    public void getNumberValues(BorderValuesMessage borderValuesMessage, StreamObserver<NumberMessage> responseObserver) {
        long firstValue = borderValuesMessage.getFirstValue();
        long lastValue = borderValuesMessage.getLastValue();

        for (long i = firstValue; i <= lastValue; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            responseObserver.onNext(NumberMessage.newBuilder()
                    .setValue(i)
                    .build());
        }
        responseObserver.onCompleted();
    }
}
