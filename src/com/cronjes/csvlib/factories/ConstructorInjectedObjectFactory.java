package com.cronjes.csvlib.factories;

import com.cronjes.csvlib.annotaions.CsvColumn;
import com.cronjes.csvlib.deserializers.CsvDeserializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConstructorInjectedObjectFactory<T> extends ObjectFactory<T> {

    public ConstructorInjectedObjectFactory(Class<T> objectType, List<CsvDeserializer> deserializers) {
        super(objectType, deserializers);
    }

    private Constructor<T> getAnnotatedConstructor() {
        Constructor result;
        for(Constructor constructor :  objectType.getConstructors()) {

            boolean canUseConstructor = true;
            /*
                we are looking for constructors with parameters that are annotated with the @CsvColumn annotation.
                All parameters must be annotated.
             */
            for (Parameter parameter : constructor.getParameters()) {
                canUseConstructor = canUseConstructor && (parameter.getAnnotation(CsvColumn.class) != null);
            }

            if (canUseConstructor) return constructor;
        }
        return null;
    }

    private Object[] makeConstructorParameters(Parameter[] parameters, Map<String, String> csvData) {
        Object[] objs = new Object[parameters.length];
        int index = 0;
        for (Parameter parameter : parameters) {
            Class<?> type = parameter.getType();
                 Optional<CsvDeserializer> deserializer =
                    findDeserializer(type);
            if (deserializer.isPresent()) {
                String columnName = getColumnNameFromAnnoation(parameter);
                objs[index] = deserializer.get().deserialize(type, csvData.get(columnName));
            } else {
                objs[index] = null;
            }
            index++;
        }
        return objs;
    }

    @Override
    public T makeObject(Map<String, String> columns) throws InstantiationException, IllegalAccessException, InvocationTargetException {

        Constructor constructor = getAnnotatedConstructor();
        Object[] objs = makeConstructorParameters(constructor.getParameters(),columns );
        return (T)constructor.newInstance(objs);

    }
}
