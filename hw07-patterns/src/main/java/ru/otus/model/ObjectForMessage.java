package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage implements Cloneable {
    private List<String> data;

    public ObjectForMessage() {
        this.data = new ArrayList<>();
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

    @Override
    public ObjectForMessage clone() {
        ObjectForMessage clone = new ObjectForMessage();
        clone.setData(new ArrayList<>(this.getData()));
        return clone;
    }
}
