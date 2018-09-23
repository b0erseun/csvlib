package com.cronjes.csvlib.parser;

import com.cronjes.csvlib.exceptions.CsvFormatException;

import java.util.ArrayList;
import java.util.List;

public class CsvParserImpl implements CsvParser {
    @Override
    public List<String> parseLine(String line) {
        ParserState parserState = ParserState.IN_NORMALTEXT;

        List<String> columns = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (int c = 0; c < line.length(); c++) {
            char ch = line.charAt(c);

            switch (parserState) {
                case IN_NORMALTEXT:
                    switch (ch) {
                        case '\"':
                            parserState = ParserState.IN_QUOTEDTEXT;
                            break;

                        case ',':
                            columns.add(sb.toString());
                            sb.setLength(0);
                            break;
                        default:
                            sb.append(ch);
                            break;
                    }
                    break;
                case IN_QUOTEDTEXT:
                    switch (ch) {
                        case '\"':
                            parserState = ParserState.QUOTE;
                            break;
                        default:
                            sb.append(ch);
                            break;
                    }
                    break;
                case QUOTE:   //we just potentially had a closing quote.  a closing quote should always be followed
                    // by a comma
                    switch (ch) {
                        case ',':
                            columns.add(sb.toString());
                            sb.setLength(0);
                            parserState = ParserState.IN_NORMALTEXT;
                            break;
                        default:
                            sb.append("\"");
                            sb.append(ch);
                            parserState = ParserState.IN_QUOTEDTEXT;
                            break;
                    }
            }
        }
        switch (parserState) {
            case IN_NORMALTEXT:
            case QUOTE:
                columns.add(sb.toString());
                break;
            default:
                throw new CsvFormatException("Bad format of line [" + line + "] unmatched '\"'.");

        }
        return columns;
    }
}

enum ParserState {
    IN_QUOTEDTEXT,
    IN_NORMALTEXT,
    QUOTE
}
