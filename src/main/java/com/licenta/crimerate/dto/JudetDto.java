package com.licenta.crimerate.dto;

import lombok.Data;

@Data
public class JudetDto {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;

    public String getIndicativ() {
        return indicativ;
    }

    public void setIndicativ(String indicativ) {
        this.indicativ = indicativ;
    }

    private String indicativ;

    public String getNumeJudet() {
        return numeJudet;
    }

    public void setNumeJudet(String numeJudet) {
        this.numeJudet = numeJudet;
    }

    private String numeJudet;
}