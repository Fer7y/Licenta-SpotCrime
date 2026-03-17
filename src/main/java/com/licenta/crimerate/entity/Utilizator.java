package com.licenta.crimerate.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilizator")
public class Utilizator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nume_complet", nullable = false, length = 100)
    private String numeComplet;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "parola_hash", nullable = false)
    private String parolaHash;

    @Column(name = "rol", length = 20)
    private String rol = "CETATEAN";

    @Column(name = "data_inregistrare", insertable = false, updatable = false)
    private LocalDateTime dataInregistrare;

    // Constructori, Getteri și Setteri
    public Utilizator() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNumeComplet() { return numeComplet; }
    public void setNumeComplet(String numeComplet) { this.numeComplet = numeComplet; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getParolaHash() { return parolaHash; }
    public void setParolaHash(String parolaHash) { this.parolaHash = parolaHash; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public LocalDateTime getDataInregistrare() { return dataInregistrare; }
    public void setDataInregistrare(LocalDateTime dataInregistrare) { this.dataInregistrare = dataInregistrare; }
}