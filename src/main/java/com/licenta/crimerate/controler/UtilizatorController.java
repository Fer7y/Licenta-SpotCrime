package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.UtilizatorDto;
import com.licenta.crimerate.service.UtilizatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilizatori")
@CrossOrigin("*")
public class UtilizatorController {

    private final UtilizatorService utilizatorService;

    public UtilizatorController(UtilizatorService utilizatorService) {
        this.utilizatorService = utilizatorService;
    }

    @GetMapping
    public ResponseEntity<List<UtilizatorDto>> getAll() {
        return ResponseEntity.ok(utilizatorService.getAllUtilizatori());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilizatorDto> getById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(utilizatorService.getUtilizatorById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
