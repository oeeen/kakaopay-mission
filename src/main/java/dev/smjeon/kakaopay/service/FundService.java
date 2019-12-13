package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.Amount;
import dev.smjeon.kakaopay.domain.Fund;
import dev.smjeon.kakaopay.domain.FundRepository;
import dev.smjeon.kakaopay.domain.Row;
import dev.smjeon.kakaopay.util.CsvParser;
import dev.smjeon.kakaopay.util.InstituteConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
public class FundService {
    private static final Logger logger = LoggerFactory.getLogger(FundService.class);

    private final FundRepository fundRepository;

    private final InstituteService instituteService;

    public FundService(FundRepository fundRepository, InstituteService instituteService) {
        this.fundRepository = fundRepository;
        this.instituteService = instituteService;
    }

    public int saveCsv(MultipartFile multipartFile) {
        List<Row> parse = CsvParser.parse(multipartFile);

        instituteService.saveRow(parse.get(0));
        List<Fund> funds = convertToFund(parse);
        List<Fund> savedFunds = fundRepository.saveAll(funds);

        return savedFunds.size();
    }

    private List<Fund> convertToFund(List<Row> rows) {
        List<Fund> funds = new ArrayList<>();
        Row firstRow = rows.get(0);
        for (int i = 1; i < rows.size(); i++) {
            funds.addAll(saveFund(rows.get(i), firstRow));
        }
        return funds;
    }

    private List<Fund> saveFund(Row row, Row firstRow) {
        List<Fund> oneRow = new ArrayList<>();
        int year = Integer.parseInt(row.getColumn(0));
        int month = Integer.parseInt(row.getColumn(1));
        for (int i = 2; i < row.size(); i++) {
            String column = row.getColumn(i);
            Fund fund = new Fund(
                    Year.of(year),
                    Month.of(month),
                    instituteService.findByName(InstituteConverter.convert(firstRow.getColumn(i))),
                    new Amount(column));
            oneRow.add(fundRepository.save(fund));
            logger.debug("Inserted Fund: {}", fund);
        }
        return oneRow;
    }
}
