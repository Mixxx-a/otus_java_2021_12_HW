package ru.otus.processor.homework;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;
import java.util.List;

public class ProcessorExceptionTest {

    static Message message;

    @BeforeAll
    public static void beforeAll() {
        List<String> data = List.of("data1", "data2", "data3");
        ObjectForMessage objectForMessage = new ObjectForMessage();
        objectForMessage.setData(data);

        message = new Message.Builder(1000L)
                .field1("field1")
                .field11("field11")
                .field12("field12")
                .field13(objectForMessage)
                .build();
    }

    @Test
    public void testProcessorExceptionEvenSecond() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            Processor processor = new ProcessorException(() -> LocalDateTime.of(2022, 6, 6, 0, 0, 2));
            processor.process(message);
        });
    }

    @Test
    public void testProcessorExceptionOddSecond() {
        Assertions.assertDoesNotThrow(() -> {
            Processor processor = new ProcessorException(() -> LocalDateTime.of(2022, 6, 6, 0, 0, 1));
            processor.process(message);
        });
    }
}
