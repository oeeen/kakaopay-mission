package dev.smjeon.kakaopay.controller;

import dev.smjeon.kakaopay.service.HousingFinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HousingFinanceApiController {
    private HousingFinanceService housingFinanceService;

    public HousingFinanceApiController(HousingFinanceService housingFinanceService) {
        this.housingFinanceService = housingFinanceService;
    }

    @PostMapping("/prices")
    public ResponseEntity saveHouses() {
        Long affected = housingFinanceService.saveCsv();
        Map<String, Long> body = new HashMap<>();
        body.put("AffectedRows", affected);

        return ResponseEntity.ok().body(body);
    }
}
