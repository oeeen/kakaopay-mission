package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.Institute;
import dev.smjeon.kakaopay.dto.InstituteResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class FundServiceTest {

    @InjectMocks
    private FundService fundService;

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
}