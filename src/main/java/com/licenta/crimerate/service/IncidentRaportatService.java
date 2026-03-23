package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.IncidentRaportatDto;
import com.licenta.crimerate.dto.IncidentRequestDto;
import com.licenta.crimerate.entity.AbonamentAlerta;
import com.licenta.crimerate.entity.IncidentRaportat;
import com.licenta.crimerate.entity.IstoricNotificari;
import com.licenta.crimerate.entity.Localitate;
import com.licenta.crimerate.entity.Utilizator;
import com.licenta.crimerate.repository.AbonamentAlertaRepository;
import com.licenta.crimerate.repository.IncidentRaportatRepository;
import com.licenta.crimerate.repository.IstoricNotificariRepository;
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

    // Injectăm repository-urile noi pentru notificări
    private final AbonamentAlertaRepository abonamentRepository;
    private final IstoricNotificariRepository notificariRepository;

    public IncidentRaportatService(IncidentRaportatRepository incidentRepository,
                                   UtilizatorRepository utilizatorRepository,
                                   LocalitateRepository localitateRepository,
                                   AbonamentAlertaRepository abonamentRepository,
                                   IstoricNotificariRepository notificariRepository) {
        this.incidentRepository = incidentRepository;
        this.utilizatorRepository = utilizatorRepository;
        this.localitateRepository = localitateRepository;
        this.abonamentRepository = abonamentRepository;
        this.notificariRepository = notificariRepository;
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

    // ==========================================
    // --- ZONA ADMIN: APROBARE ȘI NOTIFICĂRI ---
    // ==========================================

    // 2. Aprobă incidentul și trimite notificări cetățenilor abonați
    public String aprobaIncident(Integer incidentId) {
        IncidentRaportat incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new RuntimeException("Incidentul nu a fost găsit!"));

        // Aprobăm incidentul ca să apară pe hartă
        incident.setStatus("APROBAT");
        incidentRepository.save(incident);

        // Găsim toți utilizatorii abonați la localitatea unde a avut loc incidentul
        List<AbonamentAlerta> abonamente = abonamentRepository.findByLocalitateId(incident.getLocalitate().getId());

        // Creăm o notificare pentru fiecare utilizator abonat
        for (AbonamentAlerta abonament : abonamente) {
            IstoricNotificari notificare = new IstoricNotificari();
            notificare.setUtilizator(abonament.getUtilizator());
            notificare.setMesaj("ALERTĂ: Un incident (" + incident.getTipInfractiune() + ") a fost confirmat în orașul tău: " + incident.getLocalitate().getNumeLocalitate() + ".");
            notificare.setCitit(false);

            notificariRepository.save(notificare);
        }

        return "Incident APROBAT! Au fost trimise " + abonamente.size() + " notificări cetățenilor abonați.";
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