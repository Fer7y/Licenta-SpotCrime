package com.licenta.crimerate.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "istoric_criminalitate")
@Data
@NoArgsConstructor
public class IstoricCriminalitate {

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

    public Integer getAn() {
        return an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    @Column(name = "an", nullable = false)
    private Integer an;

    public BigDecimal getCoeficient() {
        return coeficient;
    }

    public void setCoeficient(BigDecimal coeficient) {
        this.coeficient = coeficient;
    }

    @Column(name = "coeficient", nullable = false, precision = 10, scale = 4)
    private BigDecimal coeficient;

    public String getDomeniuIncadrare() {
        return domeniuIncadrare;
    }

    public void setDomeniuIncadrare(String domeniuIncadrare) {
        this.domeniuIncadrare = domeniuIncadrare;
    }

    @Column(name = "domeniu_incadrare", length = 20)
    private String domeniuIncadrare;
}
