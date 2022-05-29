package com.pucp.odiparpackappback.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class BloqueoModel {
    private @Id @GeneratedValue Long id;
    private int ubigeoInicio;
    private int ubigeoFin;
    private int diaInicio;
    private int mesInicio;
    private int horaInicio;
    private int minutoInicio;
    private int diaFin;
    private int mesFin;
    private int horaFin;
    private int minutoFin;

    public BloqueoModel() {

    }
    public BloqueoModel(Long id, int ubigeoInicio, int ubigeoFin, int diaInicio, int mesInicio, int horaInicio, int minutoInicio, int diaFin, int mesFin, int horaFin, int minutoFin){
        this.id = id;
        this.ubigeoInicio = ubigeoInicio;
        this.ubigeoFin = ubigeoFin;
        this.diaInicio = diaInicio;
        this.mesInicio = mesInicio;
        this.horaInicio = horaInicio;
        this.minutoInicio = minutoInicio;
        this.diaFin = diaFin;
        this.mesFin = mesFin;
        this.horaFin = horaFin;
        this.minutoFin = minutoFin;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public int getUbigeoInicio() {
        return ubigeoInicio;
    }
    public void setUbigeoInicio(int ubigeoInicio) {
        this.ubigeoInicio = ubigeoInicio;
    }

    public int getUbigeoFin() {
        return ubigeoFin;
    }
    public void setUbigeoFin(int ubigeoFin) {
        this.ubigeoFin = ubigeoFin;
    }

    public int getDiaInicio() {
        return diaInicio;
    }
    public void setDiaInicio(int diaInicio) {
        this.diaInicio = diaInicio;
    }

    public int getMesInicio() {
        return mesInicio;
    }
    public void setMesInicio(int mesInicio) {
        this.mesInicio = mesInicio;
    }

    public int getHoraInicio() {
        return horaInicio;
    }
    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getMinutoInicio() {
        return minutoInicio;
    }
    public void setMinutoInicio(int minutoInicio) {
        this.minutoInicio = minutoInicio;
    }

    public int getDiaFin() {
        return diaFin;
    }
    public void setDiaFin(int diaFin) {
        this.diaFin = diaFin;
    }

    public int getMesFin() {
        return mesFin;
    }
    public void setMesFin(int mesFin) {
        this.mesFin = mesFin;
    }

    public int getHoraFin() {
        return horaFin;
    }
    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public int getMinutoFin() {
        return minutoFin;
    }
    public void setMinutoFin(int minutoFin) {
        this.minutoFin = minutoFin;
    }
}
