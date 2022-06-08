package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

public class ProcessorException implements Processor {

    private final LocalDateTime time;

    public ProcessorException(LocalDateTime time) {
        this.time = time;
    }
    @Override
    public Message process(Message message) {
        int second = time.getSecond();
        if (second % 2 == 0) {
            throw new RuntimeException();
        } else return message;
    }
}
