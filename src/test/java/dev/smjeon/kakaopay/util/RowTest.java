package dev.smjeon.kakaopay.util;

import dev.smjeon.kakaopay.domain.Row;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RowTest {

    @Test
    void create() {
        List<String> columns = Arrays.asList("2005", "1", "1019", "846", "82", "95", "30", "157", "57", "80", "99", "", "", "");
        Row row = new Row(columns);
        assertThat(row.getRow()).hasSize(11);
    }
}
