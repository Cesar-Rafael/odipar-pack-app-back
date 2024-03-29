package com.pucp.odiparpackappback.models;
import java.util.ArrayList;

public class RutaConArraySegHorasLl {
    private Long id;
    private Long idRuta;
    private Long idUnidadTransporte;
    private ArrayList<Integer> arraySeguimiento;
    private ArrayList<String> nombreProvincias;
    private ArrayList<Long> arrayHorasLlegada;
    private String codigoPlaca;
    private ArrayList<PedidoModel> pedidos;
    private ArrayList<PedidoParcialModel> pedidosParciales;

    public RutaConArraySegHorasLl(Long id, Long idRuta, Long idUnidadTransporte, ArrayList<Integer> arraySeguimiento, ArrayList<String> nombreProvincias, ArrayList<Long> arrayHorasLlegada, String codigoPlaca, ArrayList<PedidoModel> pedidos, ArrayList<PedidoParcialModel> pedidosParciales) {
        this.id = id;
        this.idRuta = idRuta;
        this.idUnidadTransporte = idUnidadTransporte;
        this.arraySeguimiento = arraySeguimiento;
        this.nombreProvincias = nombreProvincias;
        this.arrayHorasLlegada = arrayHorasLlegada;
        this.codigoPlaca = codigoPlaca;
        this.pedidos = pedidos;
        this.pedidosParciales = pedidosParciales;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdRuta() {
        return idRuta;
    }
    public void setIdRuta(Long idRuta) {
        this.idRuta = idRuta;
    }

    public Long getIdUnidadTransporte() {
        return idUnidadTransporte;
    }
    public void setIdUnidadTransporte(Long idUnidadTransporte) {
        this.idUnidadTransporte = idUnidadTransporte;
    }

    public ArrayList<Integer> getArraySeguimiento() {
        return arraySeguimiento;
    }
    public void setArraySeguimiento(ArrayList<Integer> arraySeguimiento) {
        this.arraySeguimiento = arraySeguimiento;
    }

    public ArrayList<Long> getArrayHorasLlegada() {
        return arrayHorasLlegada;
    }
    public void setArrayHorasLlegada(ArrayList<Long> arrayHorasLlegada) {
        this.arrayHorasLlegada = arrayHorasLlegada;
    }

    public String getCodigoPlaca() {
        return codigoPlaca;
    }
    public void setCodigoPlaca(String codigoPlaca) {
        this.codigoPlaca = codigoPlaca;
    }

    public ArrayList<String> getNombreProvincias() {
        return nombreProvincias;
    }
    public void setNombreProvincias(ArrayList<String> nombreProvincias) {
        this.nombreProvincias = nombreProvincias;
    }

    public ArrayList<PedidoParcialModel> getPedidosParciales() {
        return pedidosParciales;
    }
    public void setPedidosParciales(ArrayList<PedidoParcialModel> pedidosParciales) {
        this.pedidosParciales = pedidosParciales;
    }

    public ArrayList<PedidoModel> getPedidos() {
        return pedidos;
    }
    public void setPedidos(ArrayList<PedidoModel> pedidos) {
        this.pedidos = pedidos;
    }
}
