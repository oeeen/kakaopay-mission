package dev.smjeon.kakaopay.controller;

import dev.smjeon.kakaopay.dto.InstituteResponseDto;
import dev.smjeon.kakaopay.dto.YearsAmountResponseDto;
import dev.smjeon.kakaopay.service.FundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FundApiController {
    private final FundService fundService;

    public FundApiController(final FundService fundService) {
        this.fundService = fundService;
    }

    @PostMapping("/amount")
    public ResponseEntity saveFunds(MultipartFile file) {
        int affected = fundService.saveCsv(file);
        Map<String, Integer> body = new HashMap<>();
        body.put("AffectedRows", affected);

        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/institutes")
    public ResponseEntity<List<InstituteResponseDto>> listInstitutes() {
        List<InstituteResponseDto> instituteResponseDtos = fundService.findAllInstitutes();
        return ResponseEntity.ok().body(instituteResponseDtos);
    }

    @GetMapping("/years")
    public ResponseEntity getTotalAmountByYear() {
        List<YearsAmountResponseDto> yearsAmountResponseDtos = fundService.findYearsAmounts();
        return ResponseEntity.ok().body(yearsAmountResponseDtos);
    }
}
