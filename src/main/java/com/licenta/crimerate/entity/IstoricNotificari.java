package com.licenta.crimerate.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "istoric_notificari")
@Data
@NoArgsConstructor
public class IstoricNotificari {

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

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    @Column(name = "mesaj", nullable = false, length = 255)
    private String mesaj;

    public Boolean getCitit() {
        return citit;
    }

    public void setCitit(Boolean citit) {
        this.citit = citit;
    }

    @Column(name = "citit")
    private Boolean citit = false;

    public LocalDateTime getDataNotificare() {
        return dataNotificare;
    }

    public void setDataNotificare(LocalDateTime dataNotificare) {
        this.dataNotificare = dataNotificare;
    }

    @Column(name = "data_notificare", insertable = false, updatable = false)
    private LocalDateTime dataNotificare;
}
