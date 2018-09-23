package com.cronjes.csvlib.deserializers;

public class StringDeserializer implements CsvDeserializer {
    @Override
    public Object deserialize(Class<?> clazz, String serializedData) {
        return serializedData;
    }

    @Override
    public boolean canDeserialize(Class<?> clazz) {
        return clazz == String.class;
    }
}
