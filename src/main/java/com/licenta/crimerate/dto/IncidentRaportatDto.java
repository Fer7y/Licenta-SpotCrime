package com.licenta.crimerate.dto;

import jakarta.persistence.Column;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IncidentRaportatDto {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;

    public String getNumeRaportor() {
        return numeRaportor;
    }

    public void setNumeRaportor(String numeRaportor) {
        this.numeRaportor = numeRaportor;
    }

    private String numeRaportor;

    public String getNumeLocalitate() {
        return numeLocalitate;
    }

    public void setNumeLocalitate(String numeLocalitate) {
        this.numeLocalitate = numeLocalitate;
    }

    private String numeLocalitate;

    public String getTipInfractiune() {
        return tipInfractiune;
    }

    public void setTipInfractiune(String tipInfractiune) {
        this.tipInfractiune = tipInfractiune;
    }

    private String tipInfractiune;

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    private String descriere;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public LocalDateTime getDataRaportare() {
        return dataRaportare;
    }

    public void setDataRaportare(LocalDateTime dataRaportare) {
        this.dataRaportare = dataRaportare;
    }

    private LocalDateTime dataRaportare;


    private Double latitudine;
    private Double longitudine;

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }

}
