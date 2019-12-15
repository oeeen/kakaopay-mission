package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.Fund;
import dev.smjeon.kakaopay.domain.FundRepository;
import dev.smjeon.kakaopay.domain.Institute;
import dev.smjeon.kakaopay.domain.Row;
import dev.smjeon.kakaopay.dto.InstituteResponseDto;
import dev.smjeon.kakaopay.dto.MaxAmountResponseDto;
import dev.smjeon.kakaopay.dto.MinMaxResponseDto;
import dev.smjeon.kakaopay.dto.PredictRequestDto;
import dev.smjeon.kakaopay.dto.PredictResponseDto;
import dev.smjeon.kakaopay.dto.YearsAmountResponseDto;
import dev.smjeon.kakaopay.service.exception.NotFoundMaxAmountException;
import dev.smjeon.kakaopay.util.CsvParser;
import dev.smjeon.kakaopay.util.InstituteConverter;
import dev.smjeon.kakaopay.vo.Amount;
import dev.smjeon.kakaopay.vo.DetailAmountVo;
import dev.smjeon.kakaopay.vo.MinMaxVo;
import org.apache.commons.math3.stat.regression.SimpleRegression;
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
    private static final String KEB = "외환은행";
    private static final float MONTH_DIVISOR_OFFSET = 12;

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
            Long amount = ((Long) ((Object[]) o)[1]);
            String name = ((Institute) ((Object[]) o)[0]).getName();

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
            String name = ((Institute) ((Object[]) o)[0]).getName();
            Long amount = ((Long) ((Object[]) o)[1]);
            detailAmountVos.add(new DetailAmountVo(year, name, amount));
        }
        return detailAmountVos;
    }

    public MinMaxResponseDto findAverageMinMax() {
        List<DetailAmountVo> detailAmountVos = new ArrayList<>();
        List<Year> years = fundRepository.findDistinctYear();

        for (Year year : years) {
            detailAmountVos.addAll(convertToDetailAmountVo(year));
        }

        return convertToMinMaxDto(detailAmountVos);
    }

    private MinMaxResponseDto convertToMinMaxDto(List<DetailAmountVo> detailAmountVos) {
        DetailAmountVo maxDetailAmountVo =
                detailAmountVos.stream()
                        .filter(vo -> vo.getInstituteName().equals(KEB))
                        .max(Comparator.comparing(DetailAmountVo::getAmount)).orElseThrow(NotFoundMaxAmountException::new);
        MinMaxVo max = getMinMaxVo(maxDetailAmountVo);

        DetailAmountVo minDetailAmountVo =
                detailAmountVos.stream()
                        .filter(vo -> vo.getInstituteName().equals(KEB))
                        .min(Comparator.comparing(DetailAmountVo::getAmount)).orElseThrow(NotFoundMaxAmountException::new);
        MinMaxVo min = getMinMaxVo(minDetailAmountVo);

        return new MinMaxResponseDto(min, max);
    }

    private MinMaxVo getMinMaxVo(DetailAmountVo detailAmountVo) {
        int round = Math.round(detailAmountVo.getAmount() / MONTH_DIVISOR_OFFSET);
        return new MinMaxVo(detailAmountVo.getYear(), (long) round);
    }

    public PredictResponseDto predict(PredictRequestDto predictRequestDto) {

        Institute foundInstitute = instituteService.findByName(predictRequestDto.getInstituteName());

        List<Fund> fundsOfInstitute = fundRepository.findAllByInstituteOrderByYearAscMonthAsc(foundInstitute);

        SimpleRegression simpleRegression = new SimpleRegression();
        for (int i = 0; i < fundsOfInstitute.size(); i++) {
            simpleRegression.addData(i, fundsOfInstitute.get(i).getAmountValue());
        }

        String instituteName = predictRequestDto.getInstituteName();
        Month month = predictRequestDto.getMonth();
        int predict = (int) simpleRegression.predict(fundsOfInstitute.size() + month.getValue());

        return new PredictResponseDto(instituteName, Year.of(2018), month, Amount.of(predict));
    }
}
