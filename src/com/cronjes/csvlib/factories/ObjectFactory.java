package com.cronjes.csvlib.factories;

import com.cronjes.csvlib.annotaions.CsvColumn;
import com.cronjes.csvlib.deserializers.CsvDeserializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class ObjectFactory<T> {

    protected Class<T> objectType;

    protected List<CsvDeserializer> deserializers;

    public ObjectFactory(Class<T> objectType, List<CsvDeserializer> deserializers) {
        this.objectType = objectType;
        this.deserializers = deserializers;
    }

    protected String getColumnNameFromAnnoation(Parameter parameter) {
        CsvColumn annotation = parameter.getAnnotation(CsvColumn.class);
        if (annotation != null) {
            if (!annotation.value().isEmpty()) {
                return annotation.value();
            } else {
                return String.valueOf(annotation.columnIndex());
            }
        } else {
            return null;
        }
    }

    protected String getColumnNameFromAnnoation(Field field) {
        CsvColumn annotation = field.getAnnotation(CsvColumn.class);
        if (annotation != null) {
            if (!annotation.value().isEmpty()) {
                return annotation.value();
            } else {
                return String.valueOf(annotation.columnIndex());
            }
        } else {
            return null;
        }
    }

    protected String getColumnNameFromAnnoation(Method method) {
        CsvColumn annotation = method.getAnnotation(CsvColumn.class);
        if (annotation != null) {
            if (!annotation.value().isEmpty()) {
                return annotation.value();
            } else {
                return String.valueOf(annotation.columnIndex());
            }
        } else {
            return null;
        }
    }

    protected Optional<CsvDeserializer> findDeserializer(Class<?> clazz) {
        return deserializers.stream()
                .filter(des -> des.canDeserialize(clazz))
                .findFirst();
    }


    public abstract T makeObject(Map<String, String> columns) throws InstantiationException, IllegalAccessException,
            InvocationTargetException;

}
