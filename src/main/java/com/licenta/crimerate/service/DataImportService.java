package com.licenta.crimerate.service;

import com.licenta.crimerate.entity.Judet;
import com.licenta.crimerate.entity.Localitate;
import com.licenta.crimerate.repository.JudetRepository;
import com.licenta.crimerate.repository.LocalitateRepository;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

@Service
public class DataImportService {

    private final LocalitateRepository localitateRepository;
    private final JudetRepository judetRepository;

    public DataImportService(LocalitateRepository localitateRepository, JudetRepository judetRepository) {
        this.localitateRepository = localitateRepository;
        this.judetRepository = judetRepository;
    }

    public String importaLocalitatiDinCsv() {
        // Dacă avem deja date, nu le mai importăm o dată
        if (localitateRepository.count() > 0) {
            return "Localitățile sunt deja importate în baza de date!";
        }

        try {
            // Caută fișierul în src/main/resources/
            ClassPathResource resource = new ClassPathResource("localitati.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "UTF-8"));

            String line;
            boolean isFirstLine = true;
            int count = 0;

            while ((line = reader.readLine()) != null) {
                // Sărim peste antet (X,Y,NUME...)
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                // Despărțim valorile prin virgulă
                String[] date = line.split(",");

                if (date.length >= 5) {
                    // Extragem coloana 3 (index 2) pentru Nume și coloana 5 (index 4) pentru Indicativ
                    String numeLocalitate = date[2].trim();
                    String indicativJudet = date[4].trim();

                    // Căutăm ID-ul județului pe baza indicativului (ex: "AB")
                    Optional<Judet> judetOpt = judetRepository.findByIndicativ(indicativJudet);

                    if (judetOpt.isPresent()) {
                        Localitate localitateNoua = new Localitate();
                        localitateNoua.setNumeLocalitate(numeLocalitate);
                        localitateNoua.setJudet(judetOpt.get());

                        localitateRepository.save(localitateNoua);
                        count++;
                    }
                }
            }
            reader.close();
            return "Succes! Au fost importate " + count + " localități.";

        } catch (Exception e) {
            return "Eroare la importul CSV-ului: " + e.getMessage();
        }
    }
}
