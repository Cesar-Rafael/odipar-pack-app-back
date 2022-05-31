package com.pucp.odiparpackappback.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class RutaModel {
    private @Id @GeneratedValue Long id;
    private Long idRuta;
    private Long idPedidoParcial;
    private Long idUnidadTransporte;
    private String seguimiento;                 // idTramo separado por comas

    public RutaModel() {

    }
    public RutaModel(Long id, Long idRuta, Long idPedidoParcial, Long idUnidadTransporte, String seguimiento) {
        this.id = id;
        this.idRuta = idRuta;
        this.idPedidoParcial = idPedidoParcial;
        this.idUnidadTransporte = idUnidadTransporte;
        this.seguimiento = seguimiento;
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
    public Long getIdPedido() {
        return idPedidoParcial;
    }
    public void setIdPedido(Long idPedido) {
        this.idPedidoParcial = idPedidoParcial;
    }
    public Long getIdUnidadTransporte() {
        return idUnidadTransporte;
    }
    public void setIdUnidadTransporte(Long idUnidadTransporte) {
        this.idUnidadTransporte = idUnidadTransporte;
    }
    public String getSeguimiento() {
        return seguimiento;
    }
    public void setSeguimiento(String seguimiento) {
        this.seguimiento = seguimiento;
    }
}
