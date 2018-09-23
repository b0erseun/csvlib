package com.cronjes;

import com.cronjes.csvlib.reader.CsvReader;
import com.cronjes.csvlib.reader.CsvReaderBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        // write your code here
        CsvReader<PersonM> peopleReader = new CsvReaderBuilder<>(PersonM.class)
                .withFilePath("c:/csvlibtest/people.txt")
                .withCustomDeserializer(new LocalDateDeserializer())
                .withHasHeaderRow()
                .build();


        try {
            List<PersonM> people = peopleReader.readCsvFile();

            people.stream()
                    .filter(p -> p.getDateOfBirth().isBefore(LocalDate.of(2001, 01, 01 )))
                    .forEach(p -> System.out.println(p.toString()));

        } catch (IOException ioex) {
            System.out.println("Could not read the file.");
        }





    }
}
