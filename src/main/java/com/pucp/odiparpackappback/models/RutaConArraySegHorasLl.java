package com.pucp.odiparpackappback.models;
import java.util.ArrayList;

public class RutaConArraySegHorasLl {
    private Long id;
    private Long idRuta;
    private Long idUnidadTransporte;
    private ArrayList<Integer> arraySeguimiento;
    private ArrayList<Long> arrayHorasLlegada;

    public RutaConArraySegHorasLl(Long id, Long idRuta, Long idUnidadTransporte, ArrayList<Integer> arraySeguimiento, ArrayList<Long> arrayHorasLlegada) {
        this.id = id;
        this.idRuta = idRuta;
        this.idUnidadTransporte = idUnidadTransporte;
        this.arraySeguimiento = arraySeguimiento;
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
}
