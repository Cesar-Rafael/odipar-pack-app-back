package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.OficinaRepository;
import com.pucp.odiparpackappback.models.OficinaModel;
import com.pucp.odiparpackappback.models.UnidadTransporteModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class OficinaController {

    private final OficinaRepository oficinaRepository;
    OficinaController(OficinaRepository oficinaRepository){
        this.oficinaRepository = oficinaRepository;
    }

    @GetMapping("/Oficina/")
    List<OficinaModel> listarOficinas(){
        return (List<OficinaModel>) oficinaRepository.findAll();
    }

    @GetMapping("/Oficina/provincia")
    @ResponseBody
    List<OficinaModel> ListarOficinasxProvincia(@RequestParam(required = false) String provincia){
        List<OficinaModel> oficinasAux = (List<OficinaModel>) oficinaRepository.findAll();
        List<OficinaModel> oficinas = new ArrayList<>();
        for(int i = 0; i < oficinasAux.size(); i++){
            if(Objects.equals(oficinasAux.get(i).getProvincia(), provincia)){
                oficinas.add(oficinasAux.get(i));
            }
        }
        return oficinas;
    }
}