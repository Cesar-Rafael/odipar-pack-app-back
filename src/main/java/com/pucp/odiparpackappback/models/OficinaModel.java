package com.pucp.odiparpackappback.models;

public class OficinaModel {
    private int id;
    private int ubigeo;
    private String departamento;
    private String provincia;
    private double latitud;
    private double longitud;
    private Region region;
    private boolean esPrincipal;
    private Long fechaCreacion;
    private Long fechaModificacion;
    private boolean activo;

    //Coordenadas

    public OficinaModel(int id, int ubigeo) {
        this.id = id;
        this.ubigeo = ubigeo;
    }

    public OficinaModel(int id, int ubigeo, String departamento, String provincia, double latitud, double longitud, Region region, boolean esPrincipal) {
        this.id = id;
        this.ubigeo = ubigeo;
        this.departamento = departamento;
        this.provincia = provincia;
        this.latitud = latitud;
        this.longitud = longitud;
        this.region = region;
        this.esPrincipal = esPrincipal;
    }

    @Override
    public String toString() {
        return "Ciudad{" +
                "id=" + id +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaModificacion=" + fechaModificacion +
                ", ubigeo='" + ubigeo + '\'' +
                ", departamento='" + departamento + '\'' +
                ", provincia='" + provincia + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", region='" + region + '\'' +
                ", activo=" + activo +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(int ubigeo) {
        this.ubigeo = ubigeo;
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

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }
}
