package com.licenta.crimerate.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IstoricNotificariDto {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    private String mesaj;

    public Boolean getCitit() {
        return citit;
    }

    public void setCitit(Boolean citit) {
        this.citit = citit;
    }

    private Boolean citit;

    public LocalDateTime getDataNotificare() {
        return dataNotificare;
    }

    public void setDataNotificare(LocalDateTime dataNotificare) {
        this.dataNotificare = dataNotificare;
    }

    private LocalDateTime dataNotificare;
}
