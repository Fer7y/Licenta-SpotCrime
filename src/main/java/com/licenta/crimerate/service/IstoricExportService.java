package com.licenta.crimerate.service;

import com.licenta.crimerate.dto.IstoricExportDto;
import com.licenta.crimerate.entity.IstoricExport;
import com.licenta.crimerate.repository.IstoricExportRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IstoricExportService {

    private final IstoricExportRepository exportRepository;
    private final JdbcTemplate jdbcTemplate;

    // Ambele dependințe sunt injectate aici
    public IstoricExportService(IstoricExportRepository exportRepository, JdbcTemplate jdbcTemplate) {
        this.exportRepository = exportRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // ==========================================
    // --- FUNCȚIILE TALE EXISTENTE ---
    // ==========================================
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

    // ==========================================
    // --- NOU: GENERAREA FIȘIERELOR XML / TXT ---
    // ==========================================
    public byte[] genereazaFisierExport(String format, String anStr) {
        // Query SQL direct către baza de date
        String sql = "SELECT j.nume_judet, i.an, i.coeficient " +
                "FROM istoric_criminalitate i " +
                "JOIN judet j ON i.id_judet = j.id ";

        // Filtru pentru an specific
        if (anStr != null && !anStr.equals("TOATE")) {
            sql += "WHERE i.an = " + anStr + " ";
        }
        sql += "ORDER BY i.an DESC, j.nume_judet ASC";

        List<Map<String, Object>> randuri = jdbcTemplate.queryForList(sql);

        // Creare XML
        if ("XML".equalsIgnoreCase(format)) {
            StringBuilder xml = new StringBuilder();
            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xml.append("<BazaDateCriminalitate>\n");

            for (Map<String, Object> rand : randuri) {
                xml.append("  <Inregistrare>\n");
                xml.append("    <Judet>").append(rand.get("nume_judet")).append("</Judet>\n");
                xml.append("    <An>").append(rand.get("an")).append("</An>\n");
                xml.append("    <Coeficient>").append(rand.get("coeficient")).append("</Coeficient>\n");
                xml.append("  </Inregistrare>\n");
            }
            xml.append("</BazaDateCriminalitate>");
            return xml.toString().getBytes();
        }
        // Creare TXT
        else {
            StringBuilder txt = new StringBuilder();
            txt.append("JUDET | AN | COEFICIENT\n");
            txt.append("---------------------------------\n");

            for (Map<String, Object> rand : randuri) {
                txt.append(String.format("%s | %s | %s\n",
                        rand.get("nume_judet"), rand.get("an"), rand.get("coeficient")));
            }
            return txt.toString().getBytes();
        }
    }
}