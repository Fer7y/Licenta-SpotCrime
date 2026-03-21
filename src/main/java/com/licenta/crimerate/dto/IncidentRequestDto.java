package com.licenta.crimerate.dto;

public class IncidentRequestDto {
    private Integer idUtilizator;
    private Integer idLocalitate;
    private String tipInfractiune;
    private String descriere;
    private Double latitudine;
    private Double longitudine;

    // Getteri și Setteri manuali
    public Integer getIdUtilizator() {
        return idUtilizator;
    }

    public void setIdUtilizator(Integer idUtilizator) {
        this.idUtilizator = idUtilizator;
    }

    public Integer getIdLocalitate() {
        return idLocalitate;
    }

    public void setIdLocalitate(Integer idLocalitate) {
        this.idLocalitate = idLocalitate;
    }

    public String getTipInfractiune() {
        return tipInfractiune;
    }

    public void setTipInfractiune(String tipInfractiune) {
        this.tipInfractiune = tipInfractiune;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

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