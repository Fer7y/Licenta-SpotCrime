package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.LocalitateDto;
import com.licenta.crimerate.entity.Localitate;
import com.licenta.crimerate.repository.LocalitateRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalitateService {

    private final LocalitateRepository localitateRepository;

    public LocalitateService(LocalitateRepository localitateRepository) {
        this.localitateRepository = localitateRepository;
    }

    public List<LocalitateDto> getLocalitatiByJudet(Integer judetId) {
        return localitateRepository.findByJudetId(judetId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LocalitateDto> cautaLocalitatiDupaNume(String nume) {
        return localitateRepository.findTop10ByNumeLocalitateContainingIgnoreCase(nume).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    private LocalitateDto mapToDto(Localitate localitate) {
        LocalitateDto dto = new LocalitateDto();
        dto.setId(localitate.getId());
        dto.setNumeLocalitate(localitate.getNumeLocalitate());
        dto.setNumeJudet(localitate.getJudet().getNumeJudet());
        return dto;
    }
}
