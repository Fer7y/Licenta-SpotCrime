package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.IncidentRaportatDto;
import com.licenta.crimerate.service.IncidentRaportatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidente")
@CrossOrigin("*")
public class IncidentRaportatController {

    private final IncidentRaportatService incidentService;

    public IncidentRaportatController(IncidentRaportatService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping
    public ResponseEntity<List<IncidentRaportatDto>> getAll() {
        return ResponseEntity.ok(incidentService.getAllIncidente());
    }

    @GetMapping("/aprobate")
    public ResponseEntity<List<IncidentRaportatDto>> getAprobate() {
        return ResponseEntity.ok(incidentService.getIncidenteAprobate());
    }
    @GetMapping("/utilizator/{utilizatorId}")
    public ResponseEntity<List<IncidentRaportatDto>> getByUtilizator(@PathVariable Integer utilizatorId) {
        return ResponseEntity.ok(incidentService.getIncidenteByUtilizator(utilizatorId));
    }
}
