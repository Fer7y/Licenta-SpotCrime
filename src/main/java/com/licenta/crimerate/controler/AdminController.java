package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.IncidentRaportatDto;
import com.licenta.crimerate.service.IncidentRaportatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    private final IncidentRaportatService incidentService;

    public AdminController(IncidentRaportatService incidentService) {
        this.incidentService = incidentService;
    }

    // 1. Aduce lista pentru tabelul din Panoul de Admin
    @GetMapping("/incidente-in-asteptare")
    public ResponseEntity<List<IncidentRaportatDto>> getInAsteptare() {
        return ResponseEntity.ok(incidentService.getIncidenteInAsteptare());
    }

    // 2. Butonul VERDE (Aprobă)
    @PutMapping("/incidente/{id}/aproba")
    public ResponseEntity<String> aproba(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(incidentService.aprobaIncident(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Butonul ROȘU (Respinge)
    @PutMapping("/incidente/{id}/respinge")
    public ResponseEntity<String> respinge(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(incidentService.respingeIncident(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}