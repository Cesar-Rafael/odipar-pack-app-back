package com.pucp.odiparpackappback.models;

public class UnidadTransporteModel {
    private int id;
    private String codigo;
    private int capacidadTotal;
    private int oficinaActual = -1;
    private EstadoUnidadTransporte estado;
    private int capacidadDisponible;
    private boolean asignado;
    private int idRuta = -1;

    public UnidadTransporteModel(int id, String codigo, int capacidadTotal, EstadoUnidadTransporte estado, int oficinaActual) {
        this.id = id;
        this.codigo = codigo;
        this.capacidadTotal = capacidadTotal;
        this.estado = estado;
        this.oficinaActual = oficinaActual;
        this.capacidadDisponible = capacidadTotal;
        this.asignado = false;
    }

    public void disminuirCapacidad(int cantPaquetes) {
        capacidadDisponible -= cantPaquetes;
    }

    @Override
    public String toString() {
        return "UnidadTransporteModel{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", capacidadTotal=" + capacidadTotal +
                ", oficinaActual='" + oficinaActual + '\'' +
                ", estado=" + estado +
                ", capacidadDisponible=" + capacidadDisponible +
                ", idRuta=" + idRuta +
                '}';
    }

    public boolean isAsignado() {
        return asignado;
    }

    public void setAsignado(boolean asignado) {
        this.asignado = asignado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacidadTotal() {
        return capacidadTotal;
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCapacidadDisponible() {
        return capacidadDisponible;
    }

    public void setCapacidadDisponible(int capacidadDisponible) {
        this.capacidadDisponible = capacidadDisponible;
    }

    public EstadoUnidadTransporte getEstado() {
        return estado;
    }

    public void setEstado(EstadoUnidadTransporte estado) {
        this.estado = estado;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public int getOficinaActual() {
        return oficinaActual;
    }

    public void setOficinaActual(int oficinaActual) {
        this.oficinaActual = oficinaActual;
    }
}
