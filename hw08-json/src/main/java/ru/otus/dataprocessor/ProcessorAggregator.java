package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        //группирует выходящий список по name, при этом суммирует поля value

        Map<String, Double> result = new TreeMap<>();
        for (Measurement measurement : data) {
            String name = measurement.getName();
            if (result.containsKey(name)) {
                result.put(name, result.get(name) + measurement.getValue());
            } else {
                result.put(name, measurement.getValue());
            }
        }
        return result;
    }
}
