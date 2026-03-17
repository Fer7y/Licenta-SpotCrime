package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.IstoricExportDto;
import com.licenta.crimerate.service.IstoricExportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exporturi")
@CrossOrigin("*")
public class IstoricExportController {

    private final IstoricExportService exportService;

    public IstoricExportController(IstoricExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping("/utilizator/{utilizatorId}")
    public ResponseEntity<List<IstoricExportDto>> getByUtilizator(@PathVariable Integer utilizatorId) {
        return ResponseEntity.ok(exportService.getExporturiByUtilizator(utilizatorId));
    }
}