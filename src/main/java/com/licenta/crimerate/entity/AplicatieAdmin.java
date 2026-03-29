package com.licenta.crimerate.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "aplicatie_admin")
public class AplicatieAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_utilizator")
    private Utilizator utilizator;

    @Column(columnDefinition = "TEXT")
    private String motivatie;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String mesajRaspunsAdmin;

    @Column(updatable = false)
    private LocalDateTime dataAplicare = LocalDateTime.now();

    // --- GETTERI ȘI SETTERI ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
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