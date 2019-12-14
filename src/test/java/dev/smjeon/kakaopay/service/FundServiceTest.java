package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.FundRepository;
import dev.smjeon.kakaopay.domain.Institute;
import dev.smjeon.kakaopay.dto.InstituteResponseDto;
import dev.smjeon.kakaopay.dto.MaxAmountResponseDto;
import dev.smjeon.kakaopay.dto.MinMaxResponseDto;
import dev.smjeon.kakaopay.dto.YearsAmountResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class FundServiceTest {

    @InjectMocks
    private FundService fundService;

    @Mock
    private FundRepository fundRepository;

    @Mock
    private InstituteService instituteService;

    @Mock
    private Institute institute;

    @Test
    void findAllInstitutes() {
        List<Institute> institutes = Collections.singletonList(new Institute("주택도시기금"));
        given(instituteService.findAll()).willReturn(institutes);
        given(institute.getName()).willReturn("주택도시기금");
        given(institute.getCode()).willReturn("bnk001");

        List<InstituteResponseDto> instituteResponseDtos = fundService.findAllInstitutes();
        assertThat(instituteResponseDtos.get(0).getName()).isEqualTo("주택도시기금");
        assertThat(instituteResponseDtos.get(0).getCode()).isEqualTo("bnk001");
    }

    @Test
    @DisplayName("년도별 각 금융기관의 지원금액 합계계산 로직 검사")
    void findYearsAmounts() {
        List<Year> years = Arrays.asList(Year.of(2005), Year.of(2006), Year.of(2007));

        List<Object> sumByYearGroupByInstitute = new ArrayList<>();
        Object[] sumInstitute = {new Institute("주택도시기금"), 1000L};
        sumByYearGroupByInstitute.add(sumInstitute);
        given(fundRepository.findDistinctYear()).willReturn(years);
        given(fundRepository.findSumByYearGroupByInstitute(any(Year.class))).willReturn(sumByYearGroupByInstitute);

        List<YearsAmountResponseDto> yearsAmountResponseDto = fundService.findYearsAmounts();

        assertThat(yearsAmountResponseDto.size()).isEqualTo(3);
        assertThat(yearsAmountResponseDto.get(0).getTotalAmount()).isEqualTo(1000L);
        assertThat(yearsAmountResponseDto.get(0).getValue("주택도시기금")).isEqualTo(1000L);
        assertThat(yearsAmountResponseDto.get(0).getYear()).isEqualTo(Year.of(2005));

        verify(fundRepository).findSumByYearGroupByInstitute(Year.of(2005));
        verify(fundRepository).findDistinctYear();
    }

    @Test
    @DisplayName("전체 지원 금액 중 가장 큰 금액의 기관명 출력 로직 검사")
    void findInstituteByMaxAmount() {
        List<Year> years = Collections.singletonList(Year.of(2005));
        List<Object> sumByYearGroupByInstitute = new ArrayList<>();
        Object[] sumInstitute = {new Institute("주택도시기금"), 1000L};
        sumByYearGroupByInstitute.add(sumInstitute);
        Object[] maxInstitute = {new Institute("신한은행"), 2000L};
        sumByYearGroupByInstitute.add(maxInstitute);

        given(fundRepository.findDistinctYear()).willReturn(years);
        given(fundRepository.findSumByYearGroupByInstitute(any(Year.class))).willReturn(sumByYearGroupByInstitute);

        MaxAmountResponseDto maxAmountResponseDto = fundService.findInstituteByMaxAmount();

        assertThat(maxAmountResponseDto.getInstituteName()).isEqualTo("신한은행");
        assertThat(maxAmountResponseDto.getYear()).isEqualTo(Year.of(2005));

        verify(fundRepository).findDistinctYear();
        verify(fundRepository).findSumByYearGroupByInstitute(Year.of(2005));
    }

    @Test
    @DisplayName("외환은행의 평균 지원 금액 중 최대값, 최소값 출력 로직")
    void findAverageMinMax() {
        List<Year> years = Collections.singletonList(Year.of(2005));
        List<Object> sumByYearGroupByInstitute = new ArrayList<>();
        Object[] maxInstitute = {new Institute("외환은행"), 1200L};
        sumByYearGroupByInstitute.add(maxInstitute);
        Object[] minInstitute = {new Institute("외환은행"), 120L};
        sumByYearGroupByInstitute.add(minInstitute);

        given(fundRepository.findDistinctYear()).willReturn(years);
        given(fundRepository.findSumByYearGroupByInstitute(any(Year.class))).willReturn(sumByYearGroupByInstitute);

        MinMaxResponseDto minMaxResponseDto = fundService.findAverageMinMax();

        assertThat(minMaxResponseDto.getMaximum().getAmount()).isEqualTo(100L);
        assertThat(minMaxResponseDto.getMinimum().getAmount()).isEqualTo(10L);

        verify(fundRepository).findDistinctYear();
        verify(fundRepository).findSumByYearGroupByInstitute(Year.of(2005));
    }
}