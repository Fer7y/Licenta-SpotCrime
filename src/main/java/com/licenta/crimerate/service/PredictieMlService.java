package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.PredictieMlDto;
import com.licenta.crimerate.entity.PredictieMl;
import com.licenta.crimerate.repository.PredictieMlRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PredictieMlService {

    private final PredictieMlRepository predictieMlRepository;

    public PredictieMlService(PredictieMlRepository predictieMlRepository) {
        this.predictieMlRepository = predictieMlRepository;
    }

    public List<PredictieMlDto> getPredictiiByJudet(Integer judetId) {
        return predictieMlRepository.findByJudetId(judetId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PredictieMlDto mapToDto(PredictieMl predictie) {
        PredictieMlDto dto = new PredictieMlDto();
        dto.setId(predictie.getId());
        dto.setNumeJudet(predictie.getJudet().getNumeJudet());
        dto.setAnVizat(predictie.getAnVizat());
        dto.setCoeficientPrezis(predictie.getCoeficientPrezis());
        dto.setDataGenerare(predictie.getDataGenerare());
        return dto;
    }
}
