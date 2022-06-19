package com.pucp.odiparpackappback.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class UnidadTransporteModel {
    private @Id @GeneratedValue Long id;
    private String codigo;
    private int capacidadTotal;
    private int capacidadDisponible;
    private EstadoUnidadTransporte estado;
    private int oficinaActual = -1;
    private double abscisa;
    private double ordenada;
    private Long idRuta = Long.valueOf(-1);
    private Date fechaMantenimiento = null;

    public UnidadTransporteModel() {

    }
    public UnidadTransporteModel(Long id, String codigo, int capacidadTotal, EstadoUnidadTransporte estado, int oficinaActual, double abscisa, double ordenada) {
        this.id = id;
        this.codigo = codigo;
        this.capacidadTotal = capacidadTotal;
        this.capacidadDisponible = capacidadTotal;
        this.estado = estado;
        this.oficinaActual = oficinaActual;
        this.abscisa = abscisa;
        this.ordenada = ordenada;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCapacidadTotal() {
        return capacidadTotal;
    }
    public void setCapacidadTotal(int capacidadTotal) {
        this.capacidadTotal = capacidadTotal;
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

    public int getOficinaActual() {
        return oficinaActual;
    }
    public void setOficinaActual(int oficinaActual) {
        this.oficinaActual = oficinaActual;
    }

    public double getAbscisa() {
        return abscisa;
    }
    public void setAbscisa(double abscisa) {
        this.abscisa = abscisa;
    }

    public double getOrdenada() {
        return ordenada;
    }
    public void setOrdenada(double ordenada) {
        this.ordenada = ordenada;
    }

    public Long getIdRuta() {
        return idRuta;
    }
    public void setIdRuta(Long idRuta) {
        this.idRuta = idRuta;
    }

    public void disminuirCapacidad(int cantPaquetes) {
        capacidadDisponible -= cantPaquetes;
    }

    public Date getFechaMantenimiento() {
        return fechaMantenimiento;
    }
    public void setFechaMantenimiento(Date fechaMantenimiento) {
        this.fechaMantenimiento = fechaMantenimiento;
    }

}
