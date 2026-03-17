package com.licenta.crimerate.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "incident_raportat")
@Data
@NoArgsConstructor
public class IncidentRaportat {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Utilizator getUtilizatorRaportor() {
        return utilizatorRaportor;
    }

    public void setUtilizatorRaportor(Utilizator utilizatorRaportor) {
        this.utilizatorRaportor = utilizatorRaportor;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilizator_raportor", nullable = false)
    private Utilizator utilizatorRaportor;

    public Localitate getLocalitate() {
        return localitate;
    }

    public void setLocalitate(Localitate localitate) {
        this.localitate = localitate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_localitate", nullable = false)
    private Localitate localitate;

    public String getTipInfractiune() {
        return tipInfractiune;
    }

    public void setTipInfractiune(String tipInfractiune) {
        this.tipInfractiune = tipInfractiune;
    }

    @Column(name = "tip_infractiune", nullable = false, length = 100)
    private String tipInfractiune;

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    @Column(name = "descriere", columnDefinition = "TEXT")
    private String descriere;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "status", length = 20)
    private String status = "IN_ASTEPTARE";

    public LocalDateTime getDataRaportare() {
        return dataRaportare;
    }

    public void setDataRaportare(LocalDateTime dataRaportare) {
        this.dataRaportare = dataRaportare;
    }

    @Column(name = "data_raportare", insertable = false, updatable = false)
    private LocalDateTime dataRaportare;


    @Column(name = "latitudine")
    private Double latitudine;

    @Column(name = "longitudine")
    private Double longitudine;


    public Double getLatitudine() { return latitudine; }
    public void setLatitudine(Double latitudine) { this.latitudine = latitudine; }

    public Double getLongitudine() { return longitudine; }
    public void setLongitudine(Double longitudine) { this.longitudine = longitudine; }
}
