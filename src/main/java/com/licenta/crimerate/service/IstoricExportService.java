package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.IstoricExportDto;
import com.licenta.crimerate.entity.IstoricExport;
import com.licenta.crimerate.repository.IstoricExportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IstoricExportService {

    private final IstoricExportRepository exportRepository;

    public IstoricExportService(IstoricExportRepository exportRepository) {
        this.exportRepository = exportRepository;
    }

    public List<IstoricExportDto> getExporturiByUtilizator(Integer utilizatorId) {
        return exportRepository.findByUtilizatorId(utilizatorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private IstoricExportDto mapToDto(IstoricExport export) {
        IstoricExportDto dto = new IstoricExportDto();
        dto.setId(export.getId());
        dto.setTipFormat(export.getTipFormat());
        dto.setDataExport(export.getDataExport());
        return dto;
    }
}
