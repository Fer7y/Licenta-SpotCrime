package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.AbonamentAlertaDto;
import com.licenta.crimerate.entity.AbonamentAlerta;
import com.licenta.crimerate.entity.Localitate;
import com.licenta.crimerate.entity.Utilizator;
import com.licenta.crimerate.repository.AbonamentAlertaRepository;
import com.licenta.crimerate.repository.LocalitateRepository;
import com.licenta.crimerate.repository.UtilizatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AbonamentAlertaService {

    private final AbonamentAlertaRepository abonamentRepository;
    private final UtilizatorRepository utilizatorRepository;
    private final LocalitateRepository localitateRepository;

    public AbonamentAlertaService(AbonamentAlertaRepository abonamentRepository,
                                  UtilizatorRepository utilizatorRepository,
                                  LocalitateRepository localitateRepository) {
        this.abonamentRepository = abonamentRepository;
        this.utilizatorRepository = utilizatorRepository;
        this.localitateRepository = localitateRepository;
    }

    public List<AbonamentAlertaDto> getAbonamenteByUtilizator(Integer utilizatorId) {
        return abonamentRepository.findByUtilizatorId(utilizatorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- NOU: ADĂUGARE ABONAMENT ---
    public String adaugaAbonament(Integer utilizatorId, Integer localitateId) {
        if (abonamentRepository.existsByUtilizatorIdAndLocalitateId(utilizatorId, localitateId)) {
            throw new RuntimeException("Ești deja abonat la această localitate!");
        }

        Utilizator user = utilizatorRepository.findById(utilizatorId)
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit!"));
        Localitate loc = localitateRepository.findById(localitateId)
                .orElseThrow(() -> new RuntimeException("Localitatea nu a fost găsită!"));

        AbonamentAlerta abonament = new AbonamentAlerta();
        abonament.setUtilizator(user);
        abonament.setLocalitate(loc);
        abonamentRepository.save(abonament);

        return "Te-ai abonat cu succes!";
    }

    // --- NOU: ȘTERGERE ABONAMENT ---
    public void stergeAbonament(Integer abonamentId) {
        abonamentRepository.deleteById(abonamentId);
    }

    private AbonamentAlertaDto mapToDto(AbonamentAlerta abonament) {
        AbonamentAlertaDto dto = new AbonamentAlertaDto();
        dto.setId(abonament.getId());
        dto.setNumeLocalitate(abonament.getLocalitate().getNumeLocalitate());
        dto.setNumeJudet(abonament.getLocalitate().getJudet().getNumeJudet());
        return dto;
    }
}