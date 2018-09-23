package com.cronjes.csvlib.factories;

import com.cronjes.csvlib.annotaions.CsvColumn;
import com.cronjes.csvlib.deserializers.CsvDeserializer;
import com.cronjes.csvlib.exceptions.FieldAccessException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FieldInjectedObjectFactory<T> extends ObjectFactory<T> {


    public FieldInjectedObjectFactory(Class<T> objectType, List<CsvDeserializer> deserializers) {
        super(objectType, deserializers);
    }

    /**
     * Gets a list of all fields that are annotated by the @CsvColumn annotation
     * @return List of Field.
     */
    private List<Field> getAnnotatedFields() {

        return Arrays.asList(objectType.getDeclaredFields()).stream()
                .filter(fld -> fld.getAnnotation(CsvColumn.class) != null)
                .collect(Collectors.toList());

    }


    @Override
    public T makeObject(Map<String, String> columns)  throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        //Get all the fields that should receive values.
        List<Field> fields = getAnnotatedFields();
        Object obj = objectType.newInstance();
        fields.forEach(field -> {
            field.setAccessible(true);
            Optional<CsvDeserializer> deserializer = findDeserializer(field.getType());
            if (deserializer.isPresent()) {
                String columnName = getColumnNameFromAnnoation(field);
                try {
                    field.set(obj, deserializer.get().deserialize(field.getType(), columns.get(columnName)));
                } catch (IllegalAccessException iaex) {
                    throw new FieldAccessException(iaex.getMessage());
                }
            }
        });

        return (T) obj;


    }


}
