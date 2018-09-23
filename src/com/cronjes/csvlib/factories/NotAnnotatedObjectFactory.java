package com.cronjes.csvlib.factories;

import com.cronjes.csvlib.deserializers.CsvDeserializer;
import com.cronjes.csvlib.exceptions.FieldAccessException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NotAnnotatedObjectFactory<T> extends ObjectFactory<T> {


    public NotAnnotatedObjectFactory(Class objectType, List list) {
        super(objectType, list);
    }

    @Override
    public T makeObject(Map<String, String> columns)  throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        //Get all the fields that should receive values.
        List<Field> fields = Arrays.asList(this.objectType.getDeclaredFields());
        Object obj = objectType.newInstance();
        fields.forEach(field -> {
            field.setAccessible(true);
            Optional<CsvDeserializer> deserializer = findDeserializer(field.getType());
            if (deserializer.isPresent()) {
                String columnName = field.getName();
                try {
                    if (columns.containsKey(columnName)) {
                        field.set(obj, deserializer.get().deserialize(field.getType(), columnName));
                    } else {
                        field.set(obj, deserializer.get().deserialize(field.getType(), null));
                    }
                } catch (IllegalAccessException iaex) {
                    throw new FieldAccessException(iaex.getMessage());
                }
            }
        });

        return (T) obj;


    }

}
