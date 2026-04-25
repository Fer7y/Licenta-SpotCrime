package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.PredictieMlDto;
import com.licenta.crimerate.service.PredictieMlService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predictii")
@CrossOrigin("*")
public class PredictieMlController {

    private final PredictieMlService predictieMlService;

    public PredictieMlController(PredictieMlService predictieMlService) {
        this.predictieMlService = predictieMlService;
    }

    // Ruta ta originală
    @GetMapping("/judet/{judetId}")
    public ResponseEntity<List<PredictieMlDto>> getPredictiiByJudet(@PathVariable Integer judetId) {
        return ResponseEntity.ok(predictieMlService.getPredictiiByJudet(judetId));
    }

    // =========================================
    // NOU: Ruta apelată de pagina de Predicții AI
    // =========================================
    @GetMapping(value = "/dinamic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPredictiiDinamice() {
        String jsonRezultat = predictieMlService.ruleazaSiAducePredictii();
        return ResponseEntity.ok(jsonRezultat);
    }
}