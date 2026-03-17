package com.licenta.crimerate.controler;

import com.licenta.crimerate.dto.LoginDto;
import com.licenta.crimerate.dto.UtilizatorDto;
import com.licenta.crimerate.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            UtilizatorDto utilizator = authService.login(loginDto);
            return ResponseEntity.ok(utilizator);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody com.licenta.crimerate.dto.RegisterDto registerDto) {
        try {
            UtilizatorDto utilizator = authService.register(registerDto);
            return ResponseEntity.ok(utilizator);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/{id}/schimba-parola")
    public ResponseEntity<String> schimbaParola(@PathVariable Integer id, @RequestBody com.licenta.crimerate.dto.ChangePasswordDto dto) {
        try {
            String mesaj = authService.schimbaParola(id, dto);
            return ResponseEntity.ok(mesaj);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
