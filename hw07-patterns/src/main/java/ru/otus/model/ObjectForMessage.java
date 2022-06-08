package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public ObjectForMessage() {
        this.data = new ArrayList<>();
    }

    public ObjectForMessage(ObjectForMessage obj) {
        this.data = List.copyOf(obj.getData());
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        data.forEach((str) -> stringBuilder.append(str).append(","));
        return stringBuilder.toString();
    }
}
