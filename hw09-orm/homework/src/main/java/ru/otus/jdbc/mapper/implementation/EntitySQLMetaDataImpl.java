package ru.otus.jdbc.mapper.implementation;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return "SELECT * FROM " + entityClassMetaData.getName() + " WHERE " +
                entityClassMetaData.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        int fieldsCounter = 0;
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO " + entityClassMetaData.getName() + "(");
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            stringBuilder.append(field.getName())
                    .append(", ");
            fieldsCounter++;
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());


        stringBuilder.append(") VALUES (")
                .append("?, ".repeat(Math.max(0, fieldsCounter)))
                .delete(stringBuilder.length() - 2, stringBuilder.length())
                .append(")");

        return stringBuilder.toString();
    }

    @Override
    public String getUpdateSql() {
        StringBuilder stringBuilder = new StringBuilder("UPDATE " + entityClassMetaData.getName() + " SET ");
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            stringBuilder.append(field.getName())
                    .append(" = ?, ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

        stringBuilder.append(" WHERE ")
                .append(entityClassMetaData.getIdField().getName())
                .append(" = ?");

        return stringBuilder.toString();
    }
}
