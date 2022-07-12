package ru.sladkov.hw17.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sladkov.protobuf.generated.BorderValuesRequest;
import ru.sladkov.protobuf.generated.NumberServiceGrpc;

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

        stub.getNumbers(borderValuesRequest, observer);

        long currentValue = 0;
        for (int i = 0; i < CYCLE_ITERATIONS; i++) {
            long lastServerValue = observer.getLastValueAndReset();
            long value = currentValue + lastServerValue + 1;
            logger.info("currentValue: " + value);
            currentValue = value;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        logger.info("cycle completed");
    }
}
