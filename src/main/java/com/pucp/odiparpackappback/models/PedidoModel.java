package com.pucp.odiparpackappback.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table
public class PedidoModel {
    private @Id @GeneratedValue Long id;
    private Long idCliente;
    private int cantPaquetes;
    private int cantPaquetesNoAsignado;
    private int idCiudadDestino;
    private Date fechaHoraCreacion;
    private EstadoPedido estado;

    public PedidoModel() {

    }
    public PedidoModel(Long id, Long idCliente, int cantPaquetes, int idCiudadDestino, Date fechaHoraCreacion) {
        this.id = id;
        this.idCliente = idCliente;
        this.cantPaquetes = cantPaquetes;
        this.cantPaquetesNoAsignado = cantPaquetes;
        this.idCiudadDestino = idCiudadDestino;
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

    public Long getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
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
}
