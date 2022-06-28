package com.pucp.odiparpackappback.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class RutaModel {
    private @Id
    @GeneratedValue Long id;
    private Long idRuta;
    private Long idUnidadTransporte;
    private String seguimiento;                 // idTramo separado por comas
    private String arrayHorasLlegada;         // Horas de llegada separado por comas

    public RutaModel() {

    }

    public RutaModel(Long id, Long idRuta, Long idUnidadTransporte, String seguimiento, String arrayHorasLlegada) {
        this.id = id;
        this.idRuta = idRuta;
        this.idUnidadTransporte = idUnidadTransporte;
        this.seguimiento = seguimiento;
        this.arrayHorasLlegada = arrayHorasLlegada;
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

    public String getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(String seguimiento) {
        this.seguimiento = seguimiento;
    }

    public String getArrayHorasLlegada() {
        return arrayHorasLlegada;
    }

    public void setArrayHorasLlegada(String arrayHorasLlegada) {
        this.arrayHorasLlegada = arrayHorasLlegada;
    }
}




