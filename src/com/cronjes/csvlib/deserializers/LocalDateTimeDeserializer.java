package com.cronjes.csvlib.deserializers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer implements CsvDeserializer {


    @Override
    public Object deserialize(Class<?> clazz, String serializedData) {
        String format = "yyyy-MM-dd HH:mm:ss";
        return LocalDateTime.parse(serializedData, DateTimeFormatter.ofPattern(format));
    }

    @Override
    public boolean canDeserialize(Class<?> clazz)
    {
        return clazz == LocalDateTime.class;
    }
}
