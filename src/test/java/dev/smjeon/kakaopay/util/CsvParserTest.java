package dev.smjeon.kakaopay.util;

import dev.smjeon.kakaopay.domain.Row;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CsvParserTest {
    private static final Logger logger = LoggerFactory.getLogger(CsvParserTest.class);

    @Test
    void parseTest() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("input.csv");
        MockMultipartFile multipartFile = new MockMultipartFile(classPathResource.getFilename(), classPathResource.getInputStream());
        List<Row> parse = CsvParser.parse(multipartFile);

        assertThat(parse.get(0).getRow())
                .containsExactly("연도",
                        "월",
                        "주택도시기금1)(억원)",
                        "국민은행(억원)",
                        "우리은행(억원)",
                        "신한은행(억원)",
                        "한국시티은행(억원)",
                        "하나은행(억원)",
                        "농협은행/수협은행(억원)",
                        "외환은행(억원)",
                        "기타은행(억원)");
    }
}