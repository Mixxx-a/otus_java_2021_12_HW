package ru.otus.jdbc.mapper.implementation;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;


    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData,
                            EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    T instance = entityClassMetaData.getConstructor().newInstance();
                    Long currId = rs.getLong(entityClassMetaData.getIdField().getName());
                    Field idField = entityClassMetaData.getIdField();
                    idField.setAccessible(true);
                    idField.set(instance, currId);
                    for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                        String fieldValue = rs.getString(field.getName());
                        field.setAccessible(true);
                        field.set(instance, fieldValue);
                    }
                    return instance;
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            List<T> result = new ArrayList<>();
            try {
                while (rs.next()) {
                    T instance = entityClassMetaData.getConstructor().newInstance();
                    Long currId = rs.getLong(entityClassMetaData.getIdField().getName());
                    entityClassMetaData.getIdField().set(instance, currId);
                    for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                        String fieldValue = rs.getString(field.getName());
                        field.set(instance, fieldValue);
                    }
                    result.add(instance);
                }
                return result;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T object) {
        try {
            List<Object> params = new ArrayList<>();
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                params.add(field.get(object));
            }
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T object) {
        try {
            List<Object> params = new ArrayList<>();
            for (Field field : entityClassMetaData.getFieldsWithoutId()) {
                field.setAccessible(true);
                params.add(field.get(object));
            }
            Field idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            params.add(idField.get(object));

            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
