package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String filename;

    public FileSerializer(String fileName) {
        this.filename = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        //формирует результирующий json и сохраняет его в файл

        ObjectMapper mapper = new ObjectMapper();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename))) {
            String jsonResult = mapper.writeValueAsString(data);
            bufferedWriter.write(jsonResult);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
