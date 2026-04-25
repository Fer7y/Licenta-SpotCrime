package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.UtilizatorDto;
import com.licenta.crimerate.entity.Utilizator;
import com.licenta.crimerate.repository.UtilizatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilizatorService {

    private final UtilizatorRepository utilizatorRepository;

    public UtilizatorService(UtilizatorRepository utilizatorRepository) {
        this.utilizatorRepository = utilizatorRepository;
    }

    public List<UtilizatorDto> getAllUtilizatori() {
        return utilizatorRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UtilizatorDto getUtilizatorById(Integer id) {
        Optional<Utilizator> utilizatorOpt = utilizatorRepository.findById(id);
        if (utilizatorOpt.isPresent()) {
            return mapToDto(utilizatorOpt.get());
        }
        throw new RuntimeException("Utilizatorul nu a fost găsit!");
    }

    private UtilizatorDto mapToDto(Utilizator utilizator) {
        UtilizatorDto dto = new UtilizatorDto();
        dto.setId(utilizator.getId());
        dto.setNumeComplet(utilizator.getNumeComplet());
        dto.setEmail(utilizator.getEmail());
        dto.setRol(utilizator.getRol());
        dto.setDataInregistrare(utilizator.getDataInregistrare());
        return dto;
    }

    public void schimbaRolUtilizator(Integer id, String nouRol) {
        Utilizator utilizator = utilizatorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit!"));

        // Măsură de securitate: Adminul Suprem nu își poate pierde funcția
        if ("ADMIN_SUPREM".equals(utilizator.getRol())) {
            throw new RuntimeException("Rolul de ADMIN_SUPREM nu poate fi modificat!");
        }

        // Permitem doar schimbarea în ADMIN sau CETATEAN
        if ("ADMIN".equals(nouRol) || "CETATEAN".equals(nouRol)) {
            utilizator.setRol(nouRol);
            utilizatorRepository.save(utilizator);
        } else {
            throw new RuntimeException("Rol invalid!");
        }
    }
}

