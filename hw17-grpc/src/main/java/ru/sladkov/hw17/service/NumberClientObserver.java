package ru.sladkov.hw17.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sladkov.protobuf.generated.NumberResponse;

public class NumberClientObserver implements StreamObserver<NumberResponse> {
    private static final Logger logger = LoggerFactory.getLogger(NumberClientObserver.class);
    private long lastValue = 0;

    @Override
    public void onNext(NumberResponse response) {
        long value = response.getValue();
        logger.info("onNext value = " + value);
        setLastValue(value);
    }

    @Override
    public void onError(Throwable t) {
        logger.error("onError", t);
    }

    @Override
    public void onCompleted() {
        logger.info("onCompleted");
    }

    private synchronized void setLastValue(long value) {
        this.lastValue = value;
    }

    public synchronized long getLastValueAndReset() {
        long value = this.lastValue;
        this.lastValue = 0;
        return value;
    }
}
