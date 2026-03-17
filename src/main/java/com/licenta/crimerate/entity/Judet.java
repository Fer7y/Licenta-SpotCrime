package com.licenta.crimerate.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "judet")
public class Judet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "indicativ", nullable = false, unique = true, length = 10)
    private String indicativ;

    @Column(name = "nume_judet", nullable = false, length = 50)
    private String numeJudet;

    // Relatia 1:M cu Istoric Criminalitate
    @OneToMany(mappedBy = "judet", cascade = CascadeType.ALL)
    private List<IstoricCriminalitate> istoricCriminalitateList;

    // Constructori, Getteri și Setteri
    public Judet() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getIndicativ() { return indicativ; }
    public void setIndicativ(String indicativ) { this.indicativ = indicativ; }

    public String getNumeJudet() { return numeJudet; }
    public void setNumeJudet(String numeJudet) { this.numeJudet = numeJudet; }
}