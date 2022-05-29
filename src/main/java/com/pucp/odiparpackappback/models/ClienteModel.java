package com.pucp.odiparpackappback.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class ClienteModel {
    private @Id @GeneratedValue Long id;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correoElectronico;

    public ClienteModel() {

    }
    public ClienteModel(Long id, String nombres, String apellidoPaterno, String apellidoMaterno, String correoElectronico) {
        this.id = id;
        this.nombres = nombres;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.correoElectronico = correoElectronico;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }
    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }
    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
}
