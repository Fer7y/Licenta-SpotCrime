package com.licenta.crimerate.dto;

import java.math.BigDecimal;

public class IstoricCriminalitateDto {
    private Integer id;
    private Integer idJudet; // NOU: Adăugat pentru Dashboard-ul din frontend
    private String numeJudet;
    private String indicativJudet;
    private Integer an;
    private BigDecimal coeficient;
    private String domeniuIncadrare;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdJudet() { return idJudet; }
    public void setIdJudet(Integer idJudet) { this.idJudet = idJudet; }

    public String getNumeJudet() { return numeJudet; }
    public void setNumeJudet(String numeJudet) { this.numeJudet = numeJudet; }

    public String getIndicativJudet() { return indicativJudet; }
    public void setIndicativJudet(String indicativJudet) { this.indicativJudet = indicativJudet; }

    public Integer getAn() { return an; }
    public void setAn(Integer an) { this.an = an; }

    public BigDecimal getCoeficient() { return coeficient; }
    public void setCoeficient(BigDecimal coeficient) { this.coeficient = coeficient; }

    public String getDomeniuIncadrare() { return domeniuIncadrare; }
    public void setDomeniuIncadrare(String domeniuIncadrare) { this.domeniuIncadrare = domeniuIncadrare; }
}
