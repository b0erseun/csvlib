package com.cronjes.csvlib.deserializers;

public interface CsvDeserializer {

    public Object deserialize(Class<?> clazz, String serializedData);

    boolean canDeserialize(Class<?> clazz);

}
