package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.JudetDto;
import com.licenta.crimerate.entity.Judet;
import com.licenta.crimerate.repository.JudetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudetService {

    private final JudetRepository judetRepository;

    // Constructor manual pentru injectare
    public JudetService(JudetRepository judetRepository) {
        this.judetRepository = judetRepository;
    }

    public List<JudetDto> getAllJudete() {
        return judetRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private JudetDto mapToDto(Judet judet) {
        JudetDto dto = new JudetDto();
        dto.setId(judet.getId());
        dto.setIndicativ(judet.getIndicativ());
        dto.setNumeJudet(judet.getNumeJudet());
        return dto;
    }
}
