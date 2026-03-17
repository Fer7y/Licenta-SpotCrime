package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.IstoricCriminalitateDto;
import com.licenta.crimerate.entity.IstoricCriminalitate;
import com.licenta.crimerate.repository.IstoricCriminalitateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IstoricCriminalitateService {

    private final IstoricCriminalitateRepository istoricRepository;

    public IstoricCriminalitateService(IstoricCriminalitateRepository istoricRepository) {
        this.istoricRepository = istoricRepository;
    }

    public List<IstoricCriminalitateDto> getAllIstoric() {
        return istoricRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<IstoricCriminalitateDto> getIstoricByAn(Integer an) {
        return istoricRepository.findByAn(an).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<IstoricCriminalitateDto> getIstoricByJudet(Integer judetId) {
        return istoricRepository.findByJudetIdOrderByAnAsc(judetId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private IstoricCriminalitateDto mapToDto(IstoricCriminalitate istoric) {
        IstoricCriminalitateDto dto = new IstoricCriminalitateDto();
        dto.setId(istoric.getId());

        // AICI MAPĂM ID-UL JUDEȚULUI
        dto.setIdJudet(istoric.getJudet().getId());

        dto.setNumeJudet(istoric.getJudet().getNumeJudet());
        dto.setIndicativJudet(istoric.getJudet().getIndicativ());
        dto.setAn(istoric.getAn());
        dto.setCoeficient(istoric.getCoeficient());
        dto.setDomeniuIncadrare(istoric.getDomeniuIncadrare());
        return dto;
    }
}