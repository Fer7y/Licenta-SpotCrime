package com.licenta.crimerate.dto;

import lombok.Data;

@Data
public class AbonamentAlertaDto {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;

    public String getNumeLocalitate() {
        return numeLocalitate;
    }

    public void setNumeLocalitate(String numeLocalitate) {
        this.numeLocalitate = numeLocalitate;
    }

    private String numeLocalitate;

    public String getNumeJudet() {
        return numeJudet;
    }

    public void setNumeJudet(String numeJudet) {
        this.numeJudet = numeJudet;
    }

    private String numeJudet;
}
