package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.IstoricNotificariDto;
import com.licenta.crimerate.service.IstoricNotificariService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificari")
@CrossOrigin("*")
public class IstoricNotificariController {

    private final IstoricNotificariService notificariService;

    public IstoricNotificariController(IstoricNotificariService notificariService) {
        this.notificariService = notificariService;
    }

    // 1. Aduce TOATE notificările unui utilizator
    @GetMapping("/utilizator/{utilizatorId}")
    public ResponseEntity<List<IstoricNotificariDto>> getAllByUtilizator(@PathVariable Integer utilizatorId) {
        return ResponseEntity.ok(notificariService.getNotificariByUtilizator(utilizatorId));
    }

    // 2. Aduce DOAR notificările necitite (pentru a aprinde bulina roșie pe clopoțel)
    @GetMapping("/utilizator/{utilizatorId}/necitite")
    public ResponseEntity<List<IstoricNotificariDto>> getNecititeByUtilizator(@PathVariable Integer utilizatorId) {
        return ResponseEntity.ok(notificariService.getNotificariNecitite(utilizatorId));
    }

    // 3. Endpoint NOU: Marchează toate notificările utilizatorului ca fiind citite când deschide pop-up-ul
    @PutMapping("/citeste-toate/{utilizatorId}")
    public ResponseEntity<String> marcheazaToateCaCitite(@PathVariable Integer utilizatorId) {
        try {
            String mesaj = notificariService.marcheazaToateCaCitite(utilizatorId);
            return ResponseEntity.ok(mesaj);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}