package com.pucp.odiparpackappback.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class PedidoModel {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private Long rucCliente;
    private int cantPaquetes;
    private int cantPaquetesNoAsignado;
    private int idCiudadDestino;
    private String ciudadDestino;
    private Date fechaHoraCreacion;
    private EstadoPedido estado;

    public PedidoModel() {

    }

    public PedidoModel(Long id, Long rucCliente, int cantPaquetes, int idCiudadDestino, String ciudadDestino, Date fechaHoraCreacion) {
        this.rucCliente = rucCliente;
        this.cantPaquetes = cantPaquetes;
        this.cantPaquetesNoAsignado = cantPaquetes;
        this.idCiudadDestino = idCiudadDestino;
        this.ciudadDestino = ciudadDestino;
        this.fechaHoraCreacion = fechaHoraCreacion;
        this.estado = EstadoPedido.NO_ASIGNADO;
    }

    public void disminuirPaquetes(int cantPaquetesAtendidos) {
        cantPaquetes -= cantPaquetesAtendidos;
        if (cantPaquetes == 0) {
            estado = EstadoPedido.EN_PROCESO;
        }
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }
    public void setFechaHoraCreacion(Date fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public int getCantPaquetes() {
        return cantPaquetes;
    }
    public void setCantPaquetes(int cantPaquetes) {
        this.cantPaquetes = cantPaquetes;
    }

    public int getCantPaquetesNoAsignado() {return cantPaquetesNoAsignado;}
    public void setCantPaquetesNoAsignado(int cantPaquetesNoAsignado) {
        this.cantPaquetesNoAsignado = cantPaquetesNoAsignado;
    }

    public int getIdCiudadDestino() {
        return idCiudadDestino;
    }
    public void setIdCiudadDestino(int idCiudadDestino) {
        this.idCiudadDestino = idCiudadDestino;
    }

    public EstadoPedido getEstado() {
        return estado;
    }
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public String getCiudadDestino() {
        return ciudadDestino;
    }
    public void setCiudadDestino(String ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }

    public Long getRucCliente() {
        return rucCliente;
    }
    public void setRucCliente(Long rucCliente) {
        this.rucCliente = rucCliente;
    }
}
