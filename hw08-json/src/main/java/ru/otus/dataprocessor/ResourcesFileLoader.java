package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;
import ru.otus.model.MeasurementMixin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат

        ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(Measurement.class, MeasurementMixin.class);
        List<Measurement> measurements;

        try {
            File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
            byte[] bytes = Files.readAllBytes(file.toPath());
            String jsonString = new String(bytes);

            measurements = List.of(mapper.readValue(jsonString, Measurement[].class));
        } catch (IOException | NullPointerException e) {
            throw new FileProcessException(e);
        }

        return measurements;
    }
}
