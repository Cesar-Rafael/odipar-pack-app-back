package com.pucp.odiparpackappback.models;

import java.util.Date;

public class Bloqueo {
    private Long id;
    private int ubigeoInicio;
    private int ubigeoFin;
    private Date fechaInicio;
    private Date fechaFin;
    private String oficinaInicio;
    private String oficinaFin;

    public Bloqueo() {
    }

    public String getOficinaInicio() {
        return oficinaInicio;
    }

    public void setOficinaInicio(String oficinaInicio) {
        this.oficinaInicio = oficinaInicio;
    }

    public String getOficinaFin() {
        return oficinaFin;
    }

    public void setOficinaFin(String oficinaFin) {
        this.oficinaFin = oficinaFin;
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
