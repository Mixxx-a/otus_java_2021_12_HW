package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.Listener;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.listener.homework.HistoryReader;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.ProcessorException;
import ru.otus.processor.homework.ProcessorSwap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
             Секунда должна определяьться во время выполнения.
             Тест - важная часть задания
             Обязательно посмотрите пример к паттерну Мементо!
       4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
          Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
          Для него уже есть тест, убедитесь, что тест проходит
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */

        List<String> data = List.of("data1", "data2", "data3");
        ObjectForMessage objectForMessage = new ObjectForMessage();
        objectForMessage.setData(data);

        Message message = new Message.Builder(2L)
                .field1("field1")
                .field4("field4")
                .field11("field11")
                .field12("field12")
                .field13(objectForMessage)
                .build();

        List<Processor> processors = List.of(new ProcessorSwap(), new ProcessorException(LocalDateTime.now()));

        ComplexProcessor complexProcessor = new ComplexProcessor(processors, System.out::println);
        HistoryReader historyListener = new HistoryListener();
        complexProcessor.addListener((Listener) historyListener);

        System.out.println("message:" + message);

        Message result = complexProcessor.handle(message);
        System.out.println("result1:" + result);

        System.out.println("Change field 13 in message");
        message.getField13().setData(new ArrayList<>());

        System.out.println("message:" + message);

        System.out.println("history:" + historyListener.findMessageById(message.getId()));
    }
}
