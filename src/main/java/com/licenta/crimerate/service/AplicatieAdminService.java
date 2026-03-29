package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.AplicatieAdminDto;
import com.licenta.crimerate.entity.AplicatieAdmin;
import com.licenta.crimerate.entity.IncidentRaportat;
import com.licenta.crimerate.entity.IstoricNotificari;
import com.licenta.crimerate.entity.Utilizator;
import com.licenta.crimerate.repository.AplicatieAdminRepository;
import com.licenta.crimerate.repository.IncidentRaportatRepository;
import com.licenta.crimerate.repository.IstoricNotificariRepository;
import com.licenta.crimerate.repository.UtilizatorRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AplicatieAdminService {

    private final AplicatieAdminRepository aplicatieRepository;
    private final UtilizatorRepository utilizatorRepository;
    private final IncidentRaportatRepository incidentRepository;
    private final IstoricNotificariRepository notificariRepository;

    public AplicatieAdminService(AplicatieAdminRepository aplicatieRepository,
                                 UtilizatorRepository utilizatorRepository,
                                 IncidentRaportatRepository incidentRepository,
                                 IstoricNotificariRepository notificariRepository) {
        this.aplicatieRepository = aplicatieRepository;
        this.utilizatorRepository = utilizatorRepository;
        this.incidentRepository = incidentRepository;
        this.notificariRepository = notificariRepository;
    }

    public String verificaEligibilitate(Integer utilizatorId) {
        List<IncidentRaportat> incidente = incidentRepository.findByUtilizatorRaportorId(utilizatorId);

        int totalRaportate = incidente.size();
        if (totalRaportate < 10) {
            return "Ai raportat doar " + totalRaportate + " incidente. Ai nevoie de minim 10 pentru a aplica.";
        }

        long aprobate = incidente.stream().filter(inc -> "APROBAT".equals(inc.getStatus())).count();
        double rataSucces = (aprobate * 100.0) / totalRaportate;

        if (rataSucces < 70.0) {
            return "Rata ta de succes este de " + String.format("%.1f", rataSucces) + "%. Ai nevoie de minim 70% acuratete pentru a deveni Admin.";
        }

        return "ELIGIBIL";
    }

    public String aplicaPentruAdmin(Integer utilizatorId, String motivatie) {
        String statusEligibilitate = verificaEligibilitate(utilizatorId);
        if (!"ELIGIBIL".equals(statusEligibilitate)) {
            throw new RuntimeException(statusEligibilitate);
        }

        if (aplicatieRepository.findByUtilizatorIdAndStatus(utilizatorId, "IN_ASTEPTARE").isPresent()) {
            throw new RuntimeException("Ai deja o aplicatie in asteptare.");
        }

        Utilizator user = utilizatorRepository.findById(utilizatorId)
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost gasit."));

        AplicatieAdmin aplicatie = new AplicatieAdmin();
        aplicatie.setUtilizator(user);
        aplicatie.setMotivatie(motivatie);
        aplicatie.setStatus("IN_ASTEPTARE");
        aplicatie.setDataAplicare(LocalDateTime.now());

        aplicatieRepository.save(aplicatie);
        return "Aplicatia a fost trimisa cu succes catre Adminul Suprem!";
    }

    public List<AplicatieAdminDto> getAplicatiiInAsteptare() {
        return aplicatieRepository.findByStatusOrderByDataAplicareAsc("IN_ASTEPTARE").stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<AplicatieAdminDto> getIstoricAplicatiiUser(Integer utilizatorId) {
        return aplicatieRepository.findByUtilizatorIdOrderByDataAplicareDesc(utilizatorId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public String raspundeAplicatie(Integer aplicatieId, String actiune, String mesaj) {
        AplicatieAdmin aplicatie = aplicatieRepository.findById(aplicatieId)
                .orElseThrow(() -> new RuntimeException("Aplicatia nu exista."));

        Utilizator user = aplicatie.getUtilizator();

        if ("APROBA".equals(actiune)) {
            aplicatie.setStatus("APROBAT");
            user.setRol("ADMIN");
            utilizatorRepository.save(user);
        } else if ("RESPINGE".equals(actiune)) {
            aplicatie.setStatus("RESPINS");
        } else {
            throw new RuntimeException("Actiune invalida.");
        }

        aplicatie.setMesajRaspunsAdmin(mesaj);
        aplicatieRepository.save(aplicatie);

        IstoricNotificari notificare = new IstoricNotificari();
        notificare.setUtilizator(user);
        notificare.setMesaj("Aplicație Admin (" + aplicatie.getStatus() + "): " + mesaj);
        notificare.setCitit(false);
        notificariRepository.save(notificare);

        return "Aplicatia a fost procesata cu succes!";
    }

    private AplicatieAdminDto mapToDto(AplicatieAdmin a) {
        AplicatieAdminDto dto = new AplicatieAdminDto();
        dto.setId(a.getId());
        dto.setIdUtilizator(a.getUtilizator().getId());
        dto.setNumeUtilizator(a.getUtilizator().getNumeComplet());
        dto.setMotivatie(a.getMotivatie());
        dto.setStatus(a.getStatus());
        dto.setMesajRaspunsAdmin(a.getMesajRaspunsAdmin());
        dto.setDataAplicare(a.getDataAplicare());
        return dto;
    }
}