package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.IncidentRaportatDto;
import com.licenta.crimerate.entity.IncidentRaportat;
import com.licenta.crimerate.repository.IncidentRaportatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncidentRaportatService {

    private final IncidentRaportatRepository incidentRepository;

    public IncidentRaportatService(IncidentRaportatRepository incidentRepository) {
        this.incidentRepository = incidentRepository;
    }

    public List<IncidentRaportatDto> getAllIncidente() {
        return incidentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<IncidentRaportatDto> getIncidenteAprobate() {
        return incidentRepository.findByStatus("APROBAT").stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private IncidentRaportatDto mapToDto(IncidentRaportat incident) {
        IncidentRaportatDto dto = new IncidentRaportatDto();
        dto.setId(incident.getId());
        dto.setNumeRaportor(incident.getUtilizatorRaportor().getNumeComplet());
        dto.setNumeLocalitate(incident.getLocalitate().getNumeLocalitate());
        dto.setTipInfractiune(incident.getTipInfractiune());
        dto.setDescriere(incident.getDescriere());
        dto.setStatus(incident.getStatus());
        dto.setDataRaportare(incident.getDataRaportare());
        dto.setLatitudine(incident.getLatitudine());
        dto.setLongitudine(incident.getLongitudine());
        return dto;
    }

    public List<IncidentRaportatDto> getIncidenteByUtilizator(Integer utilizatorId) {
        return incidentRepository.findByUtilizatorRaportorId(utilizatorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}
