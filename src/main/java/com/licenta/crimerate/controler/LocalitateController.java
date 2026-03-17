package com.licenta.crimerate.controler;

import com.licenta.crimerate.service.DataImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/localitati")
@CrossOrigin("*")
public class LocalitateController {

    private final DataImportService dataImportService;

    public LocalitateController(DataImportService dataImportService) {
        this.dataImportService = dataImportService;
    }

    // Am pus GetMapping ca să dai doar click pe un link să se facă importul
    @GetMapping("/import-csv")
    public ResponseEntity<String> importaDate() {
        String rezultat = dataImportService.importaLocalitatiDinCsv();
        return ResponseEntity.ok(rezultat);
    }
}