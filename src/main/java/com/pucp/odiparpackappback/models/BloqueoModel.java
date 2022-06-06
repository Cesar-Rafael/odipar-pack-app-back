package com.pucp.odiparpackappback.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class BloqueoModel {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private int ubigeoInicio;
    private int ubigeoFin;
    private Date fechaInicio;
    private Date fechaFin;

    public BloqueoModel() {

    }
    public BloqueoModel(Long id, int ubigeoInicio, int ubigeoFin, Date fechaInicio, Date fechaFin){
        this.id = id;
        this.ubigeoInicio = ubigeoInicio;
        this.ubigeoFin = ubigeoFin;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
