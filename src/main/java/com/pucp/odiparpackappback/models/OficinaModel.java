package com.pucp.odiparpackappback.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class OficinaModel {
    private @Id int ubigeo;
    private Region region;
    private String departamento;
    private String provincia;
    private double latitud;
    private double longitud;
    private boolean esPrincipal;

    public OficinaModel() {

    }
    public OficinaModel(int ubigeo, String departamento, String provincia, double latitud, double longitud, Region region, boolean esPrincipal) {
        this.ubigeo = ubigeo;
        this.departamento = departamento;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.region = region;
        this.esPrincipal = esPrincipal;
    }

    public int getUbigeo() {
        return ubigeo;
    }
    public void setUbigeo(int ubigeo) {
        this.ubigeo = ubigeo;
    }

    public Region getRegion() {
        return region;
    }
    public void setRegion(Region region) {
        this.region = region;
    }

    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public double getLatitud() {
        return latitud;
    }
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public boolean getEsPrincipal() {
        return esPrincipal;
    }
    public void setEsPrincipal(boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }
}
