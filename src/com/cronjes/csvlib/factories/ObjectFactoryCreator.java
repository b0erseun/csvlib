package com.cronjes.csvlib.factories;

import com.cronjes.csvlib.classifiers.TypeClassifierImpl;
import com.cronjes.csvlib.deserializers.CsvDeserializer;

import java.util.List;

public class ObjectFactoryCreator<T> {

    public ObjectFactory<T> createObjectFactory(Class<T> clazz, List<CsvDeserializer> deserializers) {
        FactoryType factoryType = new TypeClassifierImpl().classify(clazz);

        switch (factoryType) {
            case ANNOTATED: return new AnnotatedObjectFactory<>(clazz, deserializers );
            default: return new NotAnnotatedObjectFactory<>(clazz, deserializers);
        }

    }

}
