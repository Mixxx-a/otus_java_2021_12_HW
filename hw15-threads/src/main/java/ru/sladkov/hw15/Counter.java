package ru.sladkov.hw15;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter {
    private static final Logger logger = LoggerFactory.getLogger(Counter.class);
    private int currentCount = 1;
    private int step = 1;
    private int lastThread = 2;

    public synchronized void action(boolean isUpdate, int threadNumber) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (lastThread == threadNumber) {
                    this.wait();
                }
                logger.info(String.valueOf(currentCount));
                lastThread = threadNumber;
                if (isUpdate) {
                    updateCount();
                }
                sleep();
                this.notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void updateCount() {
        currentCount += step;
        if (currentCount == 10) {
            step = -1;
        } else if (currentCount == 1) {
            step = 1;
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}