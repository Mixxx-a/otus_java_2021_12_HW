package ru.otus.processor.homework;

import ru.otus.DateTimeProvider;
import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorException implements Processor {

    private final DateTimeProvider dateTimeProvider;

    public ProcessorException(DateTimeProvider provider) {
        this.dateTimeProvider = provider;
    }

    @Override
    public Message process(Message message) {
        int second = dateTimeProvider.getDate().getSecond();
        if (second % 2 == 0) {
            throw new RuntimeException();
        } else return message;
    }
}
