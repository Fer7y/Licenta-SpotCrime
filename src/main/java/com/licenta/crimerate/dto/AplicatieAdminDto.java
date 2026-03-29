package com.licenta.crimerate.dto;

import java.time.LocalDateTime;

public class AplicatieAdminDto {
    private Integer id;
    private Integer idUtilizator;
    private String numeUtilizator;
    private String motivatie;
    private String status;
    private String mesajRaspunsAdmin;
    private LocalDateTime dataAplicare;

    // --- GETTERI ȘI SETTERI ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUtilizator() {
        return idUtilizator;
    }

    public void setIdUtilizator(Integer idUtilizator) {
        this.idUtilizator = idUtilizator;
    }

    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public void setNumeUtilizator(String numeUtilizator) {
        this.numeUtilizator = numeUtilizator;
    }

    public String getMotivatie() {
        return motivatie;
    }

    public void setMotivatie(String motivatie) {
        this.motivatie = motivatie;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMesajRaspunsAdmin() {
        return mesajRaspunsAdmin;
    }

    public void setMesajRaspunsAdmin(String mesajRaspunsAdmin) {
        this.mesajRaspunsAdmin = mesajRaspunsAdmin;
    }

    public LocalDateTime getDataAplicare() {
        return dataAplicare;
    }

    public void setDataAplicare(LocalDateTime dataAplicare) {
        this.dataAplicare = dataAplicare;
    }
}