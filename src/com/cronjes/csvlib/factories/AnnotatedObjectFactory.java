package com.cronjes.csvlib.factories;

import com.cronjes.csvlib.annotaions.CsvColumn;
import com.cronjes.csvlib.deserializers.CsvDeserializer;
import com.cronjes.csvlib.exceptions.ClassDefinitionException;
import com.cronjes.csvlib.exceptions.FieldAccessException;
import com.cronjes.csvlib.exceptions.MethodTypeException;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnnotatedObjectFactory<T> extends ObjectFactory<T> {

    public AnnotatedObjectFactory(Class<T> objectType, List<CsvDeserializer> deserializers) {
        super(objectType, deserializers);
    }

    private Constructor<T> getAnnotatedConstructor() {
        Constructor result;
        for (Constructor constructor : objectType.getConstructors()) {

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

    private boolean hasDefaultConstructor() {
        return Arrays.asList(objectType.getConstructors()).stream()
                .anyMatch(constr -> constr.getParameters().length == 0);

    }

    private List<Method> getAnnotatedMethods(Method[] methods) {
        return Arrays.asList(methods).stream()
                .filter(method -> method.getAnnotation(CsvColumn.class) != null)
                .collect(Collectors.toList());
    }

    private List<Field> getAnnotatedFields() {

        return Arrays.asList(objectType.getDeclaredFields()).stream()
                .filter(fld -> fld.getAnnotation(CsvColumn.class) != null)
                .collect(Collectors.toList());

    }


    @Override
    public T makeObject(Map<String, String> columns) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        //First contructors  we have to do contructors first since they might supply values to final fields.

        Object instance = null;  //instance of class we are creating;

        Constructor constructor = getAnnotatedConstructor();

        if (constructor != null) {
            Object[] parameters = makeConstructorParameters(constructor.getParameters(), columns);
            instance = constructor.newInstance(parameters);
        }

        if (instance == null) {
            //This will always invoke the zero argument (default) constructor. If the class does not have one, bad
            // things will happen...
            if (!hasDefaultConstructor()) {
                throw new ClassDefinitionException("The class " + objectType.getName() + " do not have any annotated " +
                        "contructors, nor does it have a default constructor.");

            }
            instance = objectType.newInstance();
        } 
        //Then Setters
        List<Method> annotatedMethods = getAnnotatedMethods(objectType.getDeclaredMethods());

        final Object obj = instance;
        annotatedMethods.forEach(method -> {
            String columnName = getColumnNameFromAnnoation(method);
            Class<?> paramType = method.getParameters()[0].getType();
            Optional<CsvDeserializer> deserializer = findDeserializer(paramType);
            if (deserializer.isPresent()) {
                try {
                    method.invoke(obj, deserializer.get().deserialize(paramType, columns.get(columnName)));
                } catch (InvocationTargetException | IllegalAccessException ex) {
                    throw new MethodTypeException("Could not invoke method " + method.getName() + ": " + ex.getMessage());
                }
            }
        });

        //Then fields
        List<Field> annotatedFields = getAnnotatedFields();
        annotatedFields.forEach(field -> {
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

        //All done  return the instance

        return (T) instance;

    }
}
