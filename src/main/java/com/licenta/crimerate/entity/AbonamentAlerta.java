package com.licenta.crimerate.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "abonament_alerta")
@Data
@NoArgsConstructor
public class AbonamentAlerta {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Utilizator getUtilizator() {
        return utilizator;
    }

    public void setUtilizator(Utilizator utilizator) {
        this.utilizator = utilizator;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_utilizator", nullable = false)
    private Utilizator utilizator;

    public Localitate getLocalitate() {
        return localitate;
    }

    public void setLocalitate(Localitate localitate) {
        this.localitate = localitate;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_localitate", nullable = false)
    private Localitate localitate;
}
