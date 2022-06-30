package com.pucp.odiparpackappback.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.pucp.odiparpackappback.Repositories.BloqueoRepository;
import com.pucp.odiparpackappback.dto.BloqueoBody;
import com.pucp.odiparpackappback.models.Bloqueo;
import com.pucp.odiparpackappback.models.BloqueoModel;
import com.pucp.odiparpackappback.models.Mapa;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class BloqueoController {

    private final BloqueoRepository bloqueoRepository;

    public BloqueoController(BloqueoRepository bloqueoRepository) {
        this.bloqueoRepository = bloqueoRepository;
    }

    @GetMapping("/Bloqueo/")
    List<Bloqueo> listarBloqueos() {
        HashMap<Integer, String> oficinas = new HashMap<>();
        for(int i = 0; i< Mapa.oficinas.size(); i++){
            oficinas.put(Mapa.oficinas.get(i).getUbigeo(), Mapa.oficinas.get(i).getProvincia());
        }
        List<Bloqueo> respuesta = new ArrayList<>();
        List<BloqueoModel> bloqueos = (List<BloqueoModel>) bloqueoRepository.findAll();
        for(int i = 0; i < bloqueos.size(); i ++){
            Bloqueo block = new Bloqueo();
            block.setId(bloqueos.get(i).getId());
            block.setFechaFin(bloqueos.get(i).getFechaFin());
            block.setFechaInicio(bloqueos.get(i).getFechaInicio());
            block.setUbigeoFin(bloqueos.get(i).getUbigeoFin());
            block.setUbigeoInicio(bloqueos.get(i).getUbigeoInicio());
            block.setOficinaInicio(oficinas.get(bloqueos.get(i).getUbigeoInicio()));
            block.setOficinaFin(oficinas.get(bloqueos.get(i).getUbigeoFin()));
        }

        return respuesta;
    }

    @PostMapping("/Bloqueo/PostBloqueos")
    boolean InsertarListaBloqueos(@RequestBody List<BloqueoModel> bloqueosModel) {
        try {
            bloqueoRepository.saveAll(bloqueosModel);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return true;
    }

    @PostMapping("/bloqueo/listar_por_fechas")
    List<BloqueoModel> ListarBloqueosEntreFechas(@RequestBody BloqueoBody bloqueoBody) {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        //Date fechaInicioDate = sdf.parse(fechaInicio);
        //Date fechaFinDate = sdf.parse(fechaFin);
        Date fechaInicioDate = bloqueoBody.inicio;
        Date fechaFinDate = bloqueoBody.fin;
        List<BloqueoModel> bloqueos = bloqueoRepository.findBloqueoModelByFechaInicioAndAndFechaFin(fechaInicioDate, fechaFinDate);
        return bloqueos;
    }
}