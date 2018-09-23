package com.cronjes.csvlib.reader;

import com.cronjes.csvlib.classifiers.TypeClassifier;
import com.cronjes.csvlib.classifiers.TypeClassifierImpl;
import com.cronjes.csvlib.deserializers.CsvDeserializer;
import com.cronjes.csvlib.exceptions.ClassDefinitionException;
import com.cronjes.csvlib.exceptions.CsvFormatException;
import com.cronjes.csvlib.factories.ObjectFactory;
import com.cronjes.csvlib.factories.ObjectFactoryCreator;
import com.cronjes.csvlib.parser.CsvParser;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.IntStream;

public class CsvReader<T> {

    private InputStream inputStream;
    private boolean hasHeaderRow;
    private TypeClassifier typeClassifier = new TypeClassifierImpl();
    private ObjectFactoryCreator<T> factoryCreator = new ObjectFactoryCreator<>();
    private ObjectFactory<T> objectFactory;
    private List<CsvDeserializer> deserializers = new ArrayList<>();
    private List<String> columnNames;
    private CsvParser parser;
    private Class<T> objectType;

    protected CsvReader(InputStream inputStream, boolean hasHeaderRow, TypeClassifier typeClassifier,
               List<CsvDeserializer> deserializers, CsvParser parser, Class<T> objectType) {
        this.inputStream = inputStream;
        this.hasHeaderRow = hasHeaderRow;
        this.typeClassifier = typeClassifier;
        this.deserializers = deserializers;
        this.factoryCreator = new ObjectFactoryCreator<>();
        this.parser = parser;
        this.objectType = objectType;
        initObjectFactory();
    }

    protected CsvReader(String csvFilePath, boolean hasHeaderRow, TypeClassifier typeClassifier,
                      List<CsvDeserializer> deserializers, CsvParser parser, Class<T> objectType) throws FileNotFoundException {
        File file = new File(csvFilePath);
        this.inputStream = new FileInputStream(file);
        this.hasHeaderRow = hasHeaderRow;
        this.typeClassifier = typeClassifier;
        this.deserializers = deserializers;
        this.parser = parser;
        this.objectType = objectType;
        initObjectFactory();
    }

    private void initObjectFactory() {
        this.objectFactory = factoryCreator.createObjectFactory(this.objectType, this.deserializers);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public boolean isHasHeaderRow() {
        return hasHeaderRow;
    }

    public void setHasHeaderRow(boolean hasHeaderRow) {
        this.hasHeaderRow = hasHeaderRow;
    }

    public TypeClassifier getTypeClassifier() {
        return typeClassifier;
    }

    public void setTypeClassifier(TypeClassifier typeClassifier) {
        this.typeClassifier = typeClassifier;
    }


    public List<CsvDeserializer> getDeserializers() {
        return deserializers;
    }

    public void setDeserializers(List<CsvDeserializer> deserializers) {
        this.deserializers = deserializers;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    private Map<String, String> makeMap(List<String> lineData) {
        Map<String, String> data = new HashMap<>();
        Iterator<String> keys = columnNames.iterator();
        Iterator<String> values = lineData.iterator();
        while (keys.hasNext() && values.hasNext()) {
            data.put(keys.next(), values.next());
        }
        if (keys.hasNext() || values.hasNext()) {
            throw new CsvFormatException("Misformed CSV file.  Number of items in line is not same as header.");
        }
        return data;
    }

    private void makeColumns(int size) {
        IntStream.range(0, size).forEach(i -> columnNames.add(String.valueOf(i)));
    }

    public List<T> readCsvFile() throws IOException{
        List<T> objects = new ArrayList<>();
        Scanner scanner = new Scanner(inputStream);
        if (hasHeaderRow) {
            this.columnNames = parser.parseLine(scanner.nextLine());
        }

        while (scanner.hasNextLine()) {
            List<String> lineData = parser.parseLine(scanner.nextLine());
            if (columnNames.size() == 0) {
                makeColumns(lineData.size());
            }

            Map<String, String> lineMap = makeMap(lineData);

            try {
                objects.add(objectFactory.makeObject(lineMap));
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException ex) {
                throw new ClassDefinitionException("Could not create instance of class " + objectType.getName() + " " +
                       ex.getMessage() );

            }
        }
        return objects;

    }

}
