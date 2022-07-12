package ru.sladkov.hw17.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sladkov.protobuf.generated.BorderValuesRequest;
import ru.sladkov.protobuf.generated.NumberResponse;
import ru.sladkov.protobuf.generated.NumberServiceGrpc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class NumberService extends NumberServiceGrpc.NumberServiceImplBase {
    private final static Logger logger = LoggerFactory.getLogger(NumberService.class);
    private final static int PERIOD_SECONDS = 2;

    public void getNumbers(BorderValuesRequest borderValuesRequest, StreamObserver<NumberResponse> responseObserver) {
        final long firstValue = borderValuesRequest.getFirstValue();
        final long lastValue = borderValuesRequest.getLastValue();
        logger.info("Initiating NumberResponse sequence from " + firstValue + " to " + lastValue);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        AtomicLong currentValue = new AtomicLong(firstValue);
        Runnable task = () -> {
            long value = currentValue.getAndIncrement();
            NumberResponse response = NumberResponse.newBuilder()
                    .setValue(value)
                    .build();
            responseObserver.onNext(response);
            logger.info("Sent NumberMessage with value " + value);
            if (value == lastValue) {
                executorService.shutdown();
                responseObserver.onCompleted();
                logger.info("Sequence completed");
            }
        };

        executorService.scheduleAtFixedRate(task, 0, PERIOD_SECONDS, TimeUnit.SECONDS);
    }
}
