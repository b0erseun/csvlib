package com.cronjes;

import com.cronjes.csvlib.deserializers.CsvDeserializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer implements CsvDeserializer {


    @Override
    public Object deserialize(Class<?> clazz, String serializedData) {
        String format = "yyyy-MM-dd";
        return LocalDate.parse(serializedData, DateTimeFormatter.ofPattern(format));
    }

    @Override
    public boolean canDeserialize(Class<?> clazz) {
        return LocalDate.class == clazz;
    }
}
