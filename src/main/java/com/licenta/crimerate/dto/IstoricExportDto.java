package com.licenta.crimerate.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IstoricExportDto {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;

    public String getTipFormat() {
        return tipFormat;
    }

    public void setTipFormat(String tipFormat) {
        this.tipFormat = tipFormat;
    }

    private String tipFormat;

    public LocalDateTime getDataExport() {
        return dataExport;
    }

    public void setDataExport(LocalDateTime dataExport) {
        this.dataExport = dataExport;
    }

    private LocalDateTime dataExport;
}
