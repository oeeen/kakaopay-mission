package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.Amount;
import dev.smjeon.kakaopay.domain.Fund;
import dev.smjeon.kakaopay.domain.FundRepository;
import dev.smjeon.kakaopay.domain.Institute;
import dev.smjeon.kakaopay.domain.Row;
import dev.smjeon.kakaopay.dto.InstituteResponseDto;
import dev.smjeon.kakaopay.dto.MaxAmountResponseDto;
import dev.smjeon.kakaopay.dto.YearsAmountResponseDto;
import dev.smjeon.kakaopay.service.exception.NotFoundMaxAmountException;
import dev.smjeon.kakaopay.util.CsvParser;
import dev.smjeon.kakaopay.util.InstituteConverter;
import dev.smjeon.kakaopay.vo.DetailAmountVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
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

    public List<InstituteResponseDto> findAllInstitutes() {
        List<InstituteResponseDto> instituteResponseDtos = new ArrayList<>();
        List<Institute> institutes = instituteService.findAll();

        for (Institute institute : institutes) {
            InstituteResponseDto instituteResponseDto = new InstituteResponseDto(institute.getName(), institute.getCode());
            instituteResponseDtos.add(instituteResponseDto);
        }

        return instituteResponseDtos;
    }

    public List<YearsAmountResponseDto> findYearsAmounts() {
        List<YearsAmountResponseDto> yearsAmountResponseDtos = new ArrayList<>();
        List<Year> years = fundRepository.findDistinctYear();

        for (Year year : years) {
            YearsAmountResponseDto yearsAmountResponseDto = convertToYearsDto(year);
            yearsAmountResponseDtos.add(yearsAmountResponseDto);
        }

        return yearsAmountResponseDtos;
    }

    private YearsAmountResponseDto convertToYearsDto(Year year) {
        List<Object> sumByYearGroupByInstitute = fundRepository.findSumByYearGroupByInstitute(year);
        Map<String, Long> detailAmount = new HashMap<>();
        Long sum = 0L;

        for (Object o : sumByYearGroupByInstitute) {
            Long amount = ((Long)((Object[])o)[1]);
            String name = ((Institute)((Object[])o)[0]).getName();

            detailAmount.put(name, amount);
            sum += amount;
        }

        return new YearsAmountResponseDto(year, sum, detailAmount);
    }

    public MaxAmountResponseDto findInstituteByMaxAmount() {
        List<DetailAmountVo> detailAmountVos = new ArrayList<>();
        List<Year> years = fundRepository.findDistinctYear();

        for (Year year : years) {
            detailAmountVos.addAll(convertToDetailAmountVo(year));
        }

        DetailAmountVo detailAmountVo = detailAmountVos.stream()
                .max(Comparator.comparing(DetailAmountVo::getAmount)).orElseThrow(NotFoundMaxAmountException::new);

        return new MaxAmountResponseDto(detailAmountVo.getYear(), detailAmountVo.getInstituteName());
    }

    private List<DetailAmountVo> convertToDetailAmountVo(Year year) {
        List<Object> sumByYearGroupByInstitute = fundRepository.findSumByYearGroupByInstitute(year);
        List<DetailAmountVo> detailAmountVos = new ArrayList<>();

        for (Object o : sumByYearGroupByInstitute) {
            String name = ((Institute)((Object[])o)[0]).getName();
            Long amount = ((Long)((Object[])o)[1]);
            detailAmountVos.add(new DetailAmountVo(year, name, amount));
        }
        return detailAmountVos;
    }
}
