package com.pucp.odiparpackappback.models;

public class Tramo {
    private int idTramo;
    private int idCiudadI;
    private int idCiudadJ;
    private Long fechaCreacion;
    private Long fechaModificacion;
    private boolean bloqueado;
    private double fitness;
    private Long tiempoDeViaje;

    public Tramo(int idTramo, int idCiudadI, int idCiudadJ, double tiempoDeViaje) {
        this.idTramo = idTramo;
        this.idCiudadI = idCiudadI;
        this.idCiudadJ = idCiudadJ;
        this.fitness = tiempoDeViaje;
        this.tiempoDeViaje = (long) (tiempoDeViaje * 3600);
    }

    public Long getTiempoDeViaje() {
        return tiempoDeViaje;
    }
    public void setTiempoDeViaje(Long tiempoDeViaje) {
        this.tiempoDeViaje = tiempoDeViaje;
    }

    public double getFitness() {
        return fitness;
    }
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public int getIdTramo() {
        return idTramo;
    }
    public void setIdTramo(int idTramo) {
        this.idTramo = idTramo;
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
}
