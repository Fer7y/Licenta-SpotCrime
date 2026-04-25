package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.IstoricExportDto;
import com.licenta.crimerate.service.IstoricExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exporturi") // Ruta ta originală
@CrossOrigin("*")
public class IstoricExportController {

    private final IstoricExportService exportService;

    public IstoricExportController(IstoricExportService exportService) {
        this.exportService = exportService;
    }

    // ==========================================
    // --- RUTA TA EXISTENTĂ ---
    // ==========================================
    @GetMapping("/utilizator/{utilizatorId}")
    public ResponseEntity<List<IstoricExportDto>> getByUtilizator(@PathVariable Integer utilizatorId) {
        return ResponseEntity.ok(exportService.getExporturiByUtilizator(utilizatorId));
    }

    // ==========================================
    // --- NOU: RUTA PENTRU DESCĂRCARE ---
    // ==========================================
    @GetMapping("/descarca")
    public ResponseEntity<byte[]> descarcaBazaDeDate(
            @RequestParam String format,
            @RequestParam(required = false, defaultValue = "TOATE") String an) {

        // Apelează funcția nouă din Service
        byte[] fisier = exportService.genereazaFisierExport(format, an);

        // Stabilim extensia
        String extensie = "XML".equalsIgnoreCase(format) ? ".xml" : ".txt";
        String numeFisier = "Date_Criminalitate_" + an + extensie;

        // Împingem fișierul către browser ca descărcare
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + numeFisier + "\"")
                .contentType("XML".equalsIgnoreCase(format) ? MediaType.APPLICATION_XML : MediaType.TEXT_PLAIN)
                .body(fisier);
    }
}