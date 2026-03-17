package com.licenta.crimerate.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UtilizatorDto {
    private Integer id;
    private String numeComplet;
    private String email;
    private String rol;
    private LocalDateTime dataInregistrare;

    public LocalDateTime getDataInregistrare() {
        return dataInregistrare;
    }

    public void setDataInregistrare(LocalDateTime dataInregistrare) {
        this.dataInregistrare = dataInregistrare;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeComplet() {
        return numeComplet;
    }

    public void setNumeComplet(String numeComplet) {
        this.numeComplet = numeComplet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}