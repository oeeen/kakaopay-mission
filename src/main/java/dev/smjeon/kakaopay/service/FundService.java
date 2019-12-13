package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.Fund;
import dev.smjeon.kakaopay.domain.FundRepository;
import dev.smjeon.kakaopay.domain.Row;
import dev.smjeon.kakaopay.util.CsvParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class FundService {
    private static final Logger logger = LoggerFactory.getLogger(FundService.class);

    private final FundRepository fundRepository;

    private final InstitutionService institutionService;

    public FundService(FundRepository fundRepository, InstitutionService institutionService) {
        this.fundRepository = fundRepository;
        this.institutionService = institutionService;
    }

    public int saveCsv(MultipartFile multipartFile) {
        List<Row> parse = CsvParser.parse(multipartFile);

        institutionService.saveRow(parse.get(0));
        List<Fund> funds = new ArrayList<>();

        List<Fund> savedFunds = fundRepository.saveAll(funds);
        return savedFunds.size();
    }
}
