package ru.otus.orm.entity.implementation;

import ru.otus.orm.annotation.Id;
import ru.otus.orm.entity.EntityClassMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;
    private String name;
    private Constructor<T> defaultConstructor;
    private Field idField;
    private List<Field> allFields;
    private List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        if (name == null) {
            name = clazz.getSimpleName();
        }
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        if (defaultConstructor == null) {
            try {
                defaultConstructor = clazz.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return defaultConstructor;
    }

    @Override
    public Field getIdField() {
        if (idField == null) {
            Optional<Field> optionalIdField = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Id.class))
                    .findFirst();
            if (optionalIdField.isPresent()) {
                idField = optionalIdField.get();
            } else {
                throw new RuntimeException();
            }
        }
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        if (allFields == null) {
            allFields = List.of(clazz.getDeclaredFields());
        }
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (fieldsWithoutId == null) {
            fieldsWithoutId = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> !field.isAnnotationPresent(Id.class)).toList();
        }
        return fieldsWithoutId;
    }
}
