package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repository.UnidadTransporteRepository;
import com.pucp.odiparpackappback.models.UnidadTransporteModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
class UnidadTransporteController {

    private final UnidadTransporteRepository unidadTransporteRepository;

    UnidadTransporteController(UnidadTransporteRepository unidadTransporteRepository) {
        this.unidadTransporteRepository = unidadTransporteRepository;
    }

    @GetMapping("/UnidadTransporte/")
    List<UnidadTransporteModel> listarUnidadesTransporte(){
        return (List<UnidadTransporteModel>) unidadTransporteRepository.findAll();
    }

    @GetMapping("/UnidadTransporte/idRuta")
    @ResponseBody
    List<UnidadTransporteModel> listarUnidadesTransportexIdRuta(@RequestParam(required = false) int idRuta){
        List<UnidadTransporteModel> unidadesTransporteAux = (List<UnidadTransporteModel>) unidadTransporteRepository.findAll();
        List<UnidadTransporteModel> unidadesTransporte = new ArrayList<>();
        for(int i = 0; i < unidadesTransporteAux.size(); i++){
            if(unidadesTransporteAux.get(i).getIdRuta() == idRuta){
                unidadesTransporte.add(unidadesTransporteAux.get(i));
            }
        }
        return unidadesTransporte;
    }

    @GetMapping("/UnidadTransporte/codigo")
    @ResponseBody
    List<UnidadTransporteModel> listarUnidadesTransportexCodigo(@RequestParam(required = false) String codigo){
        List<UnidadTransporteModel> unidadesTransporteAux = (List<UnidadTransporteModel>) unidadTransporteRepository.findAll();
        List<UnidadTransporteModel> unidadesTransporte = new ArrayList<>();
        for(int i = 0; i < unidadesTransporteAux.size(); i++){
            if(Objects.equals(unidadesTransporteAux.get(i).getCodigo(), codigo)){
                unidadesTransporte.add(unidadesTransporteAux.get(i));
            }
        }
        return unidadesTransporte;
    }
}