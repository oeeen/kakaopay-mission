package dev.smjeon.kakaopay.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstituteConverter {
    private static final String EXCLUDE_OFFSET = "[\\(0-9]";

    public static String convert(String column) {
        Pattern p = Pattern.compile(EXCLUDE_OFFSET);
        Matcher m = p.matcher(column);

        return m.find() ? column.substring(0, m.start()) : column;
    }
}
