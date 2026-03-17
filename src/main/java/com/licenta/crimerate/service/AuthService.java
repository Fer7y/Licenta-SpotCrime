package com.licenta.crimerate.service;


import com.licenta.crimerate.dto.LoginDto;
import com.licenta.crimerate.dto.ChangePasswordDto;
import com.licenta.crimerate.dto.RegisterDto;
import com.licenta.crimerate.dto.UtilizatorDto;
import com.licenta.crimerate.entity.Utilizator;
import com.licenta.crimerate.repository.UtilizatorRepository;
import lombok.RequiredArgsConstructor;
import com.licenta.crimerate.util.AesUtil;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    private final UtilizatorRepository utilizatorRepository;

    public AuthService(UtilizatorRepository utilizatorRepository) {
        this.utilizatorRepository = utilizatorRepository;
    }

    // 1. LOGICA DE LOGIN (Cu Email și Parolă AES)
    public UtilizatorDto login(LoginDto loginDto) {
        // Criptăm email-ul primit ca să îl putem găsi în baza de date
        String emailIntrodusCriptat = AesUtil.encrypt(loginDto.getEmail());

        Optional<Utilizator> utilizatorOpt = utilizatorRepository.findByEmail(emailIntrodusCriptat);

        if (utilizatorOpt.isPresent()) {
            Utilizator utilizator = utilizatorOpt.get();

            // Criptăm și parola introdusă
            String parolaIntrodusaCriptata = AesUtil.encrypt(loginDto.getParola());

            if (utilizator.getParolaHash().equals(parolaIntrodusaCriptata)) {
                return mapToDto(utilizator);
            }
        }
        throw new RuntimeException("Email sau parolă incorectă!");
    }

    // 2. LOGICA DE ÎNREGISTRARE (Cu Email și Parolă AES)
    public UtilizatorDto register(RegisterDto registerDto) {
        // Criptăm email-ul pentru a verifica dacă e deja luat și pentru a-l salva
        String emailCriptat = AesUtil.encrypt(registerDto.getEmail());

        // Căutăm în baza de date după email-ul criptat
        if (utilizatorRepository.findByEmail(emailCriptat).isPresent()) {
            throw new RuntimeException("Email-ul este deja folosit!");
        }

        Utilizator userNou = new Utilizator();
        userNou.setNumeComplet(registerDto.getNumeComplet());
        userNou.setEmail(emailCriptat); // Salvăm email-ul criptat

        String parolaCriptata = AesUtil.encrypt(registerDto.getParola());
        userNou.setParolaHash(parolaCriptata); // Salvăm parola criptată

        userNou.setRol("CETATEAN");

        Utilizator userSalvat = utilizatorRepository.save(userNou);
        return mapToDto(userSalvat);
    }

    // 3. MAPAREA CĂTRE FRONTEND (Decriptăm email-ul)
    private UtilizatorDto mapToDto(Utilizator utilizator) {
        UtilizatorDto dto = new UtilizatorDto();
        dto.setId(utilizator.getId());
        dto.setNumeComplet(utilizator.getNumeComplet());
        dto.setDataInregistrare(utilizator.getDataInregistrare());

        // DECRIPTĂM email-ul ca să ajungă text normal în frontend
        dto.setEmail(AesUtil.decrypt(utilizator.getEmail()));

        dto.setRol(utilizator.getRol());
        return dto;
    }
    public String schimbaParola(Integer utilizatorId, ChangePasswordDto dto) {
        Utilizator utilizator = utilizatorRepository.findById(utilizatorId)
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit!"));

        // Criptăm parola veche introdusă pentru a o compara cu cea din baza de date
        String parolaVecheCriptata = AesUtil.encrypt(dto.getParolaVeche());

        if (!utilizator.getParolaHash().equals(parolaVecheCriptata)) {
            throw new RuntimeException("Parola actuală este incorectă!");
        }

        // Criptăm parola nouă și o salvăm
        String parolaNouaCriptata = AesUtil.encrypt(dto.getParolaNoua());
        utilizator.setParolaHash(parolaNouaCriptata);
        utilizatorRepository.save(utilizator);

        return "Parola a fost actualizată cu succes!";
    }
}