package com.licenta.crimerate.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "predictie_ml")
@Data
@NoArgsConstructor
public class PredictieMl {

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

    public Integer getAnVizat() {
        return anVizat;
    }

    public void setAnVizat(Integer anVizat) {
        this.anVizat = anVizat;
    }

    @Column(name = "an_vizat", nullable = false)
    private Integer anVizat;

    public BigDecimal getCoeficientPrezis() {
        return coeficientPrezis;
    }

    public void setCoeficientPrezis(BigDecimal coeficientPrezis) {
        this.coeficientPrezis = coeficientPrezis;
    }

    @Column(name = "coeficient_prezis", nullable = false, precision = 10, scale = 4)
    private BigDecimal coeficientPrezis;

    public LocalDateTime getDataGenerare() {
        return dataGenerare;
    }

    public void setDataGenerare(LocalDateTime dataGenerare) {
        this.dataGenerare = dataGenerare;
    }

    @Column(name = "data_generare", insertable = false, updatable = false)
    private LocalDateTime dataGenerare;
}