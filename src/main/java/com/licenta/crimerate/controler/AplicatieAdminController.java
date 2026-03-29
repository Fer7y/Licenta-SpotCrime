package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.AplicatieAdminDto;
import com.licenta.crimerate.service.AplicatieAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/aplicatii")
@CrossOrigin("*")
public class AplicatieAdminController {

    private final AplicatieAdminService aplicatieService;

    public AplicatieAdminController(AplicatieAdminService aplicatieService) {
        this.aplicatieService = aplicatieService;
    }

    @GetMapping("/eligibilitate/{utilizatorId}")
    public ResponseEntity<String> verificaEligibilitate(@PathVariable Integer utilizatorId) {
        return ResponseEntity.ok(aplicatieService.verificaEligibilitate(utilizatorId));
    }

    @PostMapping("/aplica/{utilizatorId}")
    public ResponseEntity<String> aplicaPentruAdmin(@PathVariable Integer utilizatorId, @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(aplicatieService.aplicaPentruAdmin(utilizatorId, body.get("motivatie")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/istoric/{utilizatorId}")
    public ResponseEntity<List<AplicatieAdminDto>> getIstoricUser(@PathVariable Integer utilizatorId) {
        return ResponseEntity.ok(aplicatieService.getIstoricAplicatiiUser(utilizatorId));
    }

    @GetMapping("/in-asteptare")
    public ResponseEntity<List<AplicatieAdminDto>> getInAsteptare() {
        return ResponseEntity.ok(aplicatieService.getAplicatiiInAsteptare());
    }

    @PutMapping("/raspunde/{aplicatieId}")
    public ResponseEntity<String> raspunde(@PathVariable Integer aplicatieId, @RequestBody Map<String, String> body) {
        try {
            return ResponseEntity.ok(aplicatieService.raspundeAplicatie(aplicatieId, body.get("actiune"), body.get("mesaj")));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}