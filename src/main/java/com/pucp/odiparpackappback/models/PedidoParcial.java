package com.pucp.odiparpackappback.models;

public class PedidoParcial {
    private static Long id = Long.valueOf(0);
    private Long idPedidoParcial;        // correlativo
    private Long idPedido;               // relación con Pedido
    private int idCiudadOrigen;
    private int cantPaquetes;           // puede ser igual a la cantPaquetes del Pedido
    private Long fechaHoraEntrega;
    private Long idRuta;

    public PedidoParcial(Long idPedido, int idCiudadOrigen, int cantPaquetes, Long fechaHoraEntrega, Long idRuta){
        this.idPedidoParcial = id++;
        this.idPedido = idPedido;
        this.idCiudadOrigen = idCiudadOrigen;
        this.cantPaquetes = cantPaquetes;
        this.fechaHoraEntrega = fechaHoraEntrega;
        this.idRuta = idRuta;
    }

    public static Long getId() {
        return id;
    }
    public static void setId(Long id) {
        PedidoParcial.id = id;
    }
    public Long getIdPedidoParcial() {
        return idPedidoParcial;
    }
    public void setIdPedidoParcial(Long idPedidoParcial) {
        this.idPedidoParcial = idPedidoParcial;
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
