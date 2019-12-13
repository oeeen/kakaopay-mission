package dev.smjeon.kakaopay.util;

import com.opencsv.CSVReader;
import dev.smjeon.kakaopay.domain.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvParser {
    private static final Logger logger = LoggerFactory.getLogger(CsvParser.class);

    public static List<Row> parse(MultipartFile multipartFile) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(multipartFile.getInputStream()))) {
            List<Row> rows = new ArrayList<>();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                List<String> columns = Arrays.asList(nextLine);
                Row row = new Row(columns);
                rows.add(row);
            }
            return rows;
        } catch (Exception e) {
            logger.error("Cannot parse csv file", e);
            throw new CsvParseException(e);
        }
    }
}
