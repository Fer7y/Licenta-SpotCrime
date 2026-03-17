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

    @GetMapping("/utilizator/{utilizatorId}")
    public ResponseEntity<List<IstoricNotificariDto>> getAllByUtilizator(@PathVariable Integer utilizatorId) {
        return ResponseEntity.ok(notificariService.getNotificariByUtilizator(utilizatorId));
    }

    @GetMapping("/utilizator/{utilizatorId}/necitite")
    public ResponseEntity<List<IstoricNotificariDto>> getNecititeByUtilizator(@PathVariable Integer utilizatorId) {
        return ResponseEntity.ok(notificariService.getNotificariNecitite(utilizatorId));
    }
}
