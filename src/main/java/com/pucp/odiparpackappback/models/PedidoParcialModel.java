package com.pucp.odiparpackappback.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class PedidoParcialModel {
    private @Id @GeneratedValue Long id; // correlativo
    private Long idPedido;               // relación con Pedido
    private int idCiudadOrigen;
    private int cantPaquetes;           // puede ser igual a la cantPaquetes del Pedido
    private Long fechaHoraEntrega;
    private Long idRuta;

    public PedidoParcialModel() {

    }
    public PedidoParcialModel(Long id, int idCiudadOrigen, int cantPaquetes, Long fechaHoraEntrega, Long idRuta){
        this.id = id;
        this.idPedido = idPedido;
        this.idCiudadOrigen = idCiudadOrigen;
        this.cantPaquetes = cantPaquetes;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.idRuta = idRuta;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getIdPedido() {
        return idPedido;
    }
    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }
    public int getIdCiudadOrigen() {
        return idCiudadOrigen;
    }
    public void setIdCiudadOrigen(int idCiudadOrigen) {
        this.idCiudadOrigen = idCiudadOrigen;
    }
    public int getCantPaquetes() {
        return cantPaquetes;
    }
    public void setCantPaquetes(int cantPaquetes) {
        this.cantPaquetes = cantPaquetes;
    }
    public Long getFechaHoraEntrega() {
        return fechaHoraEntrega;
    }
    public void setFechaHoraEntrega(Long fechaHoraEntrega) {
        this.fechaHoraEntrega = fechaHoraEntrega;
    }
    public Long getIdRuta() {
        return idRuta;
    }
    public void setIdRuta(Long idRuta) {
        this.idRuta = idRuta;
    }
}
