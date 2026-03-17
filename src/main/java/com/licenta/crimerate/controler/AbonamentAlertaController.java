package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.AbonamentAlertaDto;
import com.licenta.crimerate.dto.LocalitateDto;
import com.licenta.crimerate.service.AbonamentAlertaService;
import com.licenta.crimerate.service.LocalitateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abonamente")
@CrossOrigin("*")
public class AbonamentAlertaController {

    private final AbonamentAlertaService abonamentService;
    private final LocalitateService localitateService;

    public AbonamentAlertaController(AbonamentAlertaService abonamentService, LocalitateService localitateService) {
        this.abonamentService = abonamentService;
        this.localitateService = localitateService;
    }

    // 1. Aducem abonamentele userului (Ruta ta)
    @GetMapping("/utilizator/{utilizatorId}")
    public ResponseEntity<List<AbonamentAlertaDto>> getByUtilizator(@PathVariable Integer utilizatorId) {
        return ResponseEntity.ok(abonamentService.getAbonamenteByUtilizator(utilizatorId));
    }

    // 2. NOU: Căutăm orașe în timp ce utilizatorul tastează (Auto-complete)
    @GetMapping("/cauta")
    public ResponseEntity<List<LocalitateDto>> cautaLocalitate(@RequestParam String nume) {
        return ResponseEntity.ok(localitateService.cautaLocalitatiDupaNume(nume));
    }

    // 3. NOU: Salvăm orașul ales
    @PostMapping("/adauga")
    public ResponseEntity<String> adaugaAbonament(@RequestParam Integer userId, @RequestParam Integer localitateId) {
        try {
            String mesaj = abonamentService.adaugaAbonament(userId, localitateId);
            return ResponseEntity.ok(mesaj);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4. NOU: Ștergem un oraș la care am renunțat
    @DeleteMapping("/sterge/{abonamentId}")
    public ResponseEntity<String> stergeAbonament(@PathVariable Integer abonamentId) {
        abonamentService.stergeAbonament(abonamentId);
        return ResponseEntity.ok("Abonament șters!");
    }
}