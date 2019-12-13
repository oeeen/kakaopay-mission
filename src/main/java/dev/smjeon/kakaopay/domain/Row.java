package dev.smjeon.kakaopay.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Row {
    private final List<String> row;

    public Row(final List<String> row) {
        this.row = row.stream()
                .filter(column -> !column.isEmpty())
                .collect(Collectors.toList());
    }

    public List<String> getRow() {
        return Collections.unmodifiableList(row);
    }

    public String getColumn(int index) {
        return row.get(index);
    }

    public int size() {
        return row.size();
    }
}
