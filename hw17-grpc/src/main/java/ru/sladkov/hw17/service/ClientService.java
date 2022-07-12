package ru.sladkov.hw17.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sladkov.protobuf.generated.BorderValuesRequest;
import ru.sladkov.protobuf.generated.NumberServiceGrpc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ClientService {
    private final static Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final static long FIRST_VALUE = 0L;
    private final static long LAST_VALUE = 30L;
    private final static int CYCLE_ITERATIONS = 50;
    private final static int PERIOD_SECONDS = 1;

    private final NumberServiceGrpc.NumberServiceStub stub;

    public ClientService(NumberServiceGrpc.NumberServiceStub stub) {
        this.stub = stub;
    }

    public void action() {
        BorderValuesRequest borderValuesRequest = BorderValuesRequest.newBuilder()
                .setFirstValue(FIRST_VALUE)
                .setLastValue(LAST_VALUE)
                .build();
        NumberClientObserver observer = new NumberClientObserver();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        AtomicInteger iteration = new AtomicInteger(0);
        AtomicLong currentValue = new AtomicLong(0L);
        Runnable task = () -> {
            long lastServerValue = observer.getLastValueAndReset();
            long value = currentValue.get() + lastServerValue + 1;
            logger.info("currentValue: " + value);
            currentValue.set(value);

            int currIteration = iteration.incrementAndGet();
            if (currIteration == CYCLE_ITERATIONS) {
                logger.info("cycle completed");
                executorService.shutdown();
            }
        };

        stub.getNumbers(borderValuesRequest, observer);
        executorService.scheduleAtFixedRate(task, 0, PERIOD_SECONDS, TimeUnit.SECONDS);
    }
}
