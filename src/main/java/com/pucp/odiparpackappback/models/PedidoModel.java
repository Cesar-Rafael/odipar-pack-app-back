package com.pucp.odiparpackappback.models;

public class PedidoModel {
    private int idPedido;
    private String cliente;
    private int cantPaquetes;
    private int cantPaquetesNoAsignado;
    private int idCiudadDestino;
    private Long fechaHoraCreacion;
    private EstadoPedido estado;

    public PedidoModel(int idPedido, int cantPaquetes, int idCiudadDestino, Long fechaHoraCreacion) {
        this.idPedido = idPedido;
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

    public int getIdPedido(){
        return idPedido;
    }
    public void setIdPedido(int idPedido){
        this.idPedido = idPedido;
    }

    public String getCliente() {
        return cliente;
    }
    public void setCliente(String cliente) {
        this.cliente = cliente;
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

    public Long getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }
    public void setFechaHoraCreacion(Long fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public EstadoPedido getEstado() {
        return estado;
    }
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedido=" + idPedido +
                ", cantPaquetes=" + cantPaquetes +
                ", cantPaquetesNoAsignado=" + cantPaquetesNoAsignado +
                ", idCiudadDestino=" + idCiudadDestino +
                ", estado=" + estado +
                '}';
    }

    @Override
    public PedidoModel clone() {
        PedidoModel pedido = null;
        try {
            pedido = (PedidoModel) super.clone();
            pedido.setEstado(this.estado);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return pedido;
    }
}
