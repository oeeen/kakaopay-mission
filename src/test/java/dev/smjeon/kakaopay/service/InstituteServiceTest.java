package dev.smjeon.kakaopay.service;

import dev.smjeon.kakaopay.domain.Institute;
import dev.smjeon.kakaopay.domain.InstituteRepository;
import dev.smjeon.kakaopay.domain.Row;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class InstituteServiceTest {

    @InjectMocks
    private InstituteService instituteService;

    @Mock
    private InstituteRepository instituteRepository;

    @Mock
    private Institute institute;

    @Mock
    private Row row;

    @Test
    void saveRow() {
        given(row.getColumn(any(Integer.class))).willReturn("주택기금");
        given(row.size()).willReturn(6); // 임의의 숫자

        instituteService.saveRow(row);

        verify(instituteRepository, times(4)).save(any(Institute.class)); // 4 = 6 - 2
    }

    @Test
    void findByName() {
        given(instituteRepository.findByName(any(String.class))).willReturn(Optional.of(institute));

        assertDoesNotThrow(() -> instituteService.findByName("주택기금"));
    }
}