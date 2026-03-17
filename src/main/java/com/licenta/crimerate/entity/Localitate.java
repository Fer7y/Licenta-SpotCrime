package com.licenta.crimerate.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "localitate")
@Data
@NoArgsConstructor
public class Localitate {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Judet getJudet() {
        return judet;
    }

    public void setJudet(Judet judet) {
        this.judet = judet;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_judet", nullable = false)
    private Judet judet;

    public String getNumeLocalitate() {
        return numeLocalitate;
    }

    public void setNumeLocalitate(String numeLocalitate) {
        this.numeLocalitate = numeLocalitate;
    }

    @Column(name = "nume_localitate", nullable = false, length = 100)
    private String numeLocalitate;
}