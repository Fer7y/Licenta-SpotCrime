package com.licenta.crimerate.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "istoric_export")
@Data
@NoArgsConstructor
public class IstoricExport {

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

    public String getTipFormat() {
        return tipFormat;
    }

    public void setTipFormat(String tipFormat) {
        this.tipFormat = tipFormat;
    }

    @Column(name = "tip_format", nullable = false, length = 10)
    private String tipFormat;

    public LocalDateTime getDataExport() {
        return dataExport;
    }

    public void setDataExport(LocalDateTime dataExport) {
        this.dataExport = dataExport;
    }

    @Column(name = "data_export", insertable = false, updatable = false)
    private LocalDateTime dataExport;
}
