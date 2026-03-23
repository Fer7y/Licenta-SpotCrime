package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.IncidentRaportatDto;
import com.licenta.crimerate.dto.IncidentRequestDto;
import com.licenta.crimerate.entity.IncidentRaportat;
import com.licenta.crimerate.entity.Localitate;
import com.licenta.crimerate.entity.Utilizator;
import com.licenta.crimerate.repository.IncidentRaportatRepository;
import com.licenta.crimerate.repository.LocalitateRepository;
import com.licenta.crimerate.repository.UtilizatorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncidentRaportatService {

    private final IncidentRaportatRepository incidentRepository;
    private final UtilizatorRepository utilizatorRepository;
    private final LocalitateRepository localitateRepository;

    public IncidentRaportatService(IncidentRaportatRepository incidentRepository,
                                   UtilizatorRepository utilizatorRepository,
                                   LocalitateRepository localitateRepository) {
        this.incidentRepository = incidentRepository;
        this.utilizatorRepository = utilizatorRepository;
        this.localitateRepository = localitateRepository;
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

    public List<IncidentRaportatDto> getIncidenteByUtilizator(Integer utilizatorId) {
        return incidentRepository.findByUtilizatorRaportorId(utilizatorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- FUNCȚIA PENTRU SALVARE INCIDENT NOU ---
    public String raporteazaIncident(IncidentRequestDto cerere) {
        Utilizator user = utilizatorRepository.findById(cerere.getIdUtilizator())
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit!"));

        Localitate loc = localitateRepository.findById(cerere.getIdLocalitate())
                .orElseThrow(() -> new RuntimeException("Localitatea nu a fost găsită!"));

        IncidentRaportat incident = new IncidentRaportat();
        incident.setUtilizatorRaportor(user);
        incident.setLocalitate(loc);
        incident.setTipInfractiune(cerere.getTipInfractiune());
        incident.setDescriere(cerere.getDescriere());
        incident.setLatitudine(cerere.getLatitudine());
        incident.setLongitudine(cerere.getLongitudine());

        // Status automat pentru ca Adminul să îl verifice ulterior
        incident.setStatus("IN_ASTEPTARE");

        incidentRepository.save(incident);
        return "Incidentul a fost raportat cu succes și așteaptă aprobarea unui administrator!";
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

    // 1. Aduce doar incidentele care așteaptă aprobare
    public List<IncidentRaportatDto> getIncidenteInAsteptare() {
        return incidentRepository.findByStatus("IN_ASTEPTARE").stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // 2. Aprobă incidentul ca să apară pe hartă
    public String aprobaIncident(Integer incidentId) {
        IncidentRaportat incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incidentul nu a fost găsit!"));

        incident.setStatus("APROBAT");
        incidentRepository.save(incident);

        return "Incident APROBAT! Acum este vizibil pe hartă.";
    }

    // 3. Respinge incidentul (Fals / Spam)
    public String respingeIncident(Integer incidentId) {
        IncidentRaportat incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incidentul nu a fost găsit!"));

        incident.setStatus("RESPINS");
        incidentRepository.save(incident);

        return "Incidentul a fost RESPINS și va fi ascuns.";
    }
}