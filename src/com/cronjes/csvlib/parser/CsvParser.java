package com.cronjes.csvlib.parser;

import java.util.List;

public interface CsvParser {

    List<String> parseLine(String line);

}
