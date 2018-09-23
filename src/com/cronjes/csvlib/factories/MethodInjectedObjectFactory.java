package com.cronjes.csvlib.factories;

import com.cronjes.csvlib.annotaions.CsvColumn;
import com.cronjes.csvlib.deserializers.CsvDeserializer;
import com.cronjes.csvlib.exceptions.MethodTypeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MethodInjectedObjectFactory<T> extends ObjectFactory<T> {

    public MethodInjectedObjectFactory(Class<T> objectType, List<CsvDeserializer> deserializers) {
        super(objectType, deserializers);
    }

    private List<Method> getAnnotatedMethods(Method[] methods) {

        return Arrays.asList(methods).stream()
                .filter(method -> method.getAnnotation(CsvColumn.class) != null)
                .collect(Collectors.toList());
    }



    @Override
    public T makeObject(Map<String, String> columns) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<Method> annotatedMethods = getAnnotatedMethods(objectType.getMethods());
        Object obj = objectType.newInstance();

        annotatedMethods.forEach(mthd -> {
            Parameter[] parameters = mthd.getParameters();
            if (parameters.length != 1) {
                throw new MethodTypeException("Method " + mthd.getName() + " does not follow the setter pattern.");
            }
            Optional<CsvDeserializer> deserializer = findDeserializer(mthd.getParameters()[0].getType());

            if (deserializer.isPresent()) {
                String columnName = getColumnNameFromAnnoation(mthd);
                Object mthdParam = deserializer.get().deserialize(mthd.getParameters()[0].getType(),
                        columns.get(columnName));
                try {
                    mthd.invoke(obj, mthdParam);
                } catch (IllegalAccessException | InvocationTargetException sex) {
                    throw new MethodTypeException("Could not invoke method " + mthd.getName() + " " + sex.getMessage());
                }
            }

        });
        return (T) obj;
    }
}
