package com.licenta.crimerate.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PredictieMlDto {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;

    public String getNumeJudet() {
        return numeJudet;
    }

    public void setNumeJudet(String numeJudet) {
        this.numeJudet = numeJudet;
    }

    private String numeJudet;

    public Integer getAnVizat() {
        return anVizat;
    }

    public void setAnVizat(Integer anVizat) {
        this.anVizat = anVizat;
    }

    private Integer anVizat;

    public BigDecimal getCoeficientPrezis() {
        return coeficientPrezis;
    }

    public void setCoeficientPrezis(BigDecimal coeficientPrezis) {
        this.coeficientPrezis = coeficientPrezis;
    }

    private BigDecimal coeficientPrezis;

    public LocalDateTime getDataGenerare() {
        return dataGenerare;
    }

    public void setDataGenerare(LocalDateTime dataGenerare) {
        this.dataGenerare = dataGenerare;
    }

    private LocalDateTime dataGenerare;
}
