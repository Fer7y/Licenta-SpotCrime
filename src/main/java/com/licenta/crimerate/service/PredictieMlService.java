package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.PredictieMlDto;
import com.licenta.crimerate.entity.PredictieMl;
import com.licenta.crimerate.repository.PredictieMlRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PredictieMlService {

    private final PredictieMlRepository predictieMlRepository;

    public PredictieMlService(PredictieMlRepository predictieMlRepository) {
        this.predictieMlRepository = predictieMlRepository;
    }

    // Funcția ta originală
    public List<PredictieMlDto> getPredictiiByJudet(Integer judetId) {
        return predictieMlRepository.findByJudetId(judetId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Funcția ta originală
    private PredictieMlDto mapToDto(PredictieMl predictie) {
        PredictieMlDto dto = new PredictieMlDto();
        dto.setId(predictie.getId());
        dto.setNumeJudet(predictie.getJudet().getNumeJudet());
        dto.setAnVizat(predictie.getAnVizat());
        dto.setCoeficientPrezis(predictie.getCoeficientPrezis());
        dto.setDataGenerare(predictie.getDataGenerare());
        return dto;
    }

    // =========================================
    // NOU: Funcția pentru rularea dinamică a AI-ului
    // =========================================
    public String ruleazaSiAducePredictii() {
        try {
            // Deschidem terminalul invizibil și rulăm Python
            ProcessBuilder pb = new ProcessBuilder("python", "src/main/resources/static/predictie.py");
            Process process = pb.start();

            // Citim răspunsul JSON oferit de scriptul Python
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String linie;

            while ((linie = reader.readLine()) != null) {
                output.append(linie);
            }

            int exitCode = process.waitFor();

            // Dacă scriptul s-a rulat cu succes (cod 0)
            if (exitCode == 0) {
                return output.toString();
            } else {
                return "[]"; // Returnăm o listă goală în caz de eroare
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        }
    }
}