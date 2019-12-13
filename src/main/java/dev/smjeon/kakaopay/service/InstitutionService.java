package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.Institute;
import dev.smjeon.kakaopay.domain.InstituteRepository;
import dev.smjeon.kakaopay.domain.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class InstitutionService {
    private static final String EXCLUDE_OFFSET = "[\\(0-9]";

    private final InstituteRepository instituteRepository;

    public InstitutionService(InstituteRepository instituteRepository) {
        this.instituteRepository = instituteRepository;
    }

    public void saveRow(Row row) {
        for (int i = 2; i < row.size(); i++) {
            String column = row.getColumn(i);
            Pattern p = Pattern.compile(EXCLUDE_OFFSET);
            Matcher m = p.matcher(column);

            String name = m.find() ? column.substring(0, m.start()) : column;

            Institute institute = new Institute(name);
            instituteRepository.save(institute);
        }
    }
}
