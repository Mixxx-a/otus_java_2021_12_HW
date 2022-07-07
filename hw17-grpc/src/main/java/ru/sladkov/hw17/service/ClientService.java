package ru.sladkov.hw17.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientService implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(ClientService.class);

    private long currentValue = 0;
    private long lastServerValue = 0;
    private boolean isNew = false;

    public void count() {
        for (int i = 0; i < 50; i++) {
            long value;
            if (isNew) {
                value = currentValue + lastServerValue + 1;
                isNew = false;
            } else {
                value = currentValue + 1;
            }

            logger.info("currentValue: " + value);
            currentValue = value;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setLastServerValue(long value) {
        this.lastServerValue = value;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public void run() {
        count();
    }
}
