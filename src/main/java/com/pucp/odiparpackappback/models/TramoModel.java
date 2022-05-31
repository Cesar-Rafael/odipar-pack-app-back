package com.pucp.odiparpackappback.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class TramoModel {
    private @Id @GeneratedValue Long id;
    private int idCiudadI;
    private int idCiudadJ;
    private boolean bloqueado;
    private double tiempoDeViaje;

    public TramoModel() {

    }
    public TramoModel(Long id, int idCiudadI, int idCiudadJ, double tiempoDeViaje, boolean bloqueado) {
        this.id = id;
        this.idCiudadI = idCiudadI;
        this.idCiudadJ = idCiudadJ;
        this.tiempoDeViaje = (tiempoDeViaje * 3600);
        this.bloqueado = bloqueado;
    }

    public int getIdCiudadI() {
        return idCiudadI;
    }
    public void setIdCiudadI(int idCiudadI) {
        this.idCiudadI = idCiudadI;
    }

    public int getIdCiudadJ() {
        return idCiudadJ;
    }
    public void setIdCiudadJ(int idCiudadJ) {
        this.idCiudadJ = idCiudadJ;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }
    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public double getTiempoDeViaje() {
        return tiempoDeViaje;
    }
    public void setTiempoDeViaje(double tiempoDeViaje) {
        this.tiempoDeViaje = tiempoDeViaje;
    }
}
