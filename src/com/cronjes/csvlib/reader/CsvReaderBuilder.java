package com.cronjes.csvlib.reader;

import com.cronjes.csvlib.classifiers.TypeClassifier;
import com.cronjes.csvlib.classifiers.TypeClassifierImpl;
import com.cronjes.csvlib.deserializers.CsvDeserializer;
import com.cronjes.csvlib.deserializers.LocalDateTimeDeserializer;
import com.cronjes.csvlib.deserializers.PrimitiveDeserializer;
import com.cronjes.csvlib.deserializers.StringDeserializer;
import com.cronjes.csvlib.parser.CsvParser;
import com.cronjes.csvlib.parser.CsvParserImpl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CsvReaderBuilder<T> {

    private String csvFilePath;

    private List<CsvDeserializer> csvDeserializers = new ArrayList<>();

    private boolean hasHeaderRow;

    private InputStream inputStream;

    private TypeClassifier typeClassifier = new TypeClassifierImpl();

    private CsvParser parser = new CsvParserImpl();

    private final Class<T> objectType;

    public CsvReaderBuilder(Class<T> objectType) {
        this.objectType = objectType;
        csvDeserializers.add(new StringDeserializer());
        csvDeserializers.add(new LocalDateTimeDeserializer());
        csvDeserializers.add(new PrimitiveDeserializer());

    }

    public CsvReader<T> build() throws FileNotFoundException {

        if (this.csvFilePath != null) {
            return new CsvReader(csvFilePath, hasHeaderRow, this.typeClassifier, this.csvDeserializers, parser,
                    objectType);
        } else {
            return new CsvReader(inputStream, hasHeaderRow, typeClassifier, csvDeserializers, parser, objectType);
        }

    }

    public CsvReaderBuilder withFilePath(String filePath) {
        this.csvFilePath = filePath;
        return this;
    }

    public CsvReaderBuilder withHasHeaderRow() {
        this.hasHeaderRow = true;
        return this;
    }

    public CsvReaderBuilder withCustomDeserializer(CsvDeserializer csvDeserializer) {
        this.csvDeserializers.add(csvDeserializer);
        return this;
    }

    public CsvReaderBuilder withParser(CsvParser csvParser) {
        this.parser = csvParser;
        return this;
    }


}
