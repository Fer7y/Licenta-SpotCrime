package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.JudetDto;
import com.licenta.crimerate.service.JudetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/judete")
@CrossOrigin("*")
public class JudetController {

    private final JudetService judetService;

    // Constructor manual
    public JudetController(JudetService judetService) {
        this.judetService = judetService;
    }

    @GetMapping
    public ResponseEntity<List<JudetDto>> getAllJudete() {
        return ResponseEntity.ok(judetService.getAllJudete());
    }
}
