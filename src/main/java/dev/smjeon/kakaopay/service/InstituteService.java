package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.Institute;
import dev.smjeon.kakaopay.domain.InstituteRepository;
import dev.smjeon.kakaopay.domain.Row;
import dev.smjeon.kakaopay.util.InstituteConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InstituteService {
    private final InstituteRepository instituteRepository;

    public InstituteService(InstituteRepository instituteRepository) {
        this.instituteRepository = instituteRepository;
    }

    public void saveRow(Row row) {
        for (int i = 2; i < row.size(); i++) {
            String column = row.getColumn(i);

            Institute institute = new Institute(InstituteConverter.convert(column));
            instituteRepository.save(institute);
        }
    }

    public Institute findByName(String name) {
        return instituteRepository.findByName(name).orElseThrow(NotFoundInstituteException::new);
    }

    public List<Institute> findAll() {
        return instituteRepository.findAll();
    }
}
