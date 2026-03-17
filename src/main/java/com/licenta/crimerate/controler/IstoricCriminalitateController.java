package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.IstoricCriminalitateDto;
import com.licenta.crimerate.service.IstoricCriminalitateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/istoric")
@CrossOrigin("*")
public class IstoricCriminalitateController {

    private final IstoricCriminalitateService istoricService;

    // Constructor manual
    public IstoricCriminalitateController(IstoricCriminalitateService istoricService) {
        this.istoricService = istoricService;
    }

    @GetMapping
    public ResponseEntity<List<IstoricCriminalitateDto>> getAll() {
        return ResponseEntity.ok(istoricService.getAllIstoric());
    }

    @GetMapping("/an/{an}")
    public ResponseEntity<List<IstoricCriminalitateDto>> getByAn(@PathVariable Integer an) {
        return ResponseEntity.ok(istoricService.getIstoricByAn(an));
    }

    @GetMapping("/judet/{judetId}")
    public ResponseEntity<List<IstoricCriminalitateDto>> getByJudet(@PathVariable Integer judetId) {
        return ResponseEntity.ok(istoricService.getIstoricByJudet(judetId));
    }
}
