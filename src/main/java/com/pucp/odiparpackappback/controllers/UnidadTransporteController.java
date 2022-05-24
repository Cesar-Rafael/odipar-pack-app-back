package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.models.EstadoUnidadTransporte;
import com.pucp.odiparpackappback.models.UnidadTransporteModel;
import com.pucp.odiparpackappback.services.UnidadTransporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/UnidadTransporte")
public class UnidadTransporteController {
    private final UnidadTransporteService unidadTransporteService;

    @Autowired
    public UnidadTransporteController(UnidadTransporteService unidadTransporteService){
        this.unidadTransporteService = unidadTransporteService;
    }
    @GetMapping
    public List<UnidadTransporteModel> getUnidadesTransporte(){
        return unidadTransporteService.getUnidadesTransporte();
    }
}
