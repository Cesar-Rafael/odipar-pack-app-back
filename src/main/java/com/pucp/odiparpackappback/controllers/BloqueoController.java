package com.pucp.odiparpackappback.controllers;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.pucp.odiparpackappback.Repositories.BloqueoRepository;
import com.pucp.odiparpackappback.Repositories.OficinaRepository;
import com.pucp.odiparpackappback.dto.BloqueoBody;
import com.pucp.odiparpackappback.models.Bloqueo;
import com.pucp.odiparpackappback.models.BloqueoModel;
import com.pucp.odiparpackappback.models.Mapa;
import com.pucp.odiparpackappback.models.OficinaModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class BloqueoController {

    private final BloqueoRepository bloqueoRepository;
    private static OficinaRepository oficinaRepository;

    public BloqueoController(BloqueoRepository bloqueoRepository, OficinaRepository oficinaRepository) {
        this.bloqueoRepository = bloqueoRepository;
        this.oficinaRepository = oficinaRepository;
    }

    @GetMapping("/Bloqueo/")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    List<Bloqueo> listarBloqueos() {
        List<OficinaModel> oficinasT = (List<OficinaModel>) oficinaRepository.findAll();
        HashMap<Integer, String> oficinas = new HashMap<>();
        for(int i = 0; i< oficinasT.size(); i++){
            oficinas.put(oficinasT.get(i).getUbigeo(), oficinasT.get(i).getProvincia());
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
            respuesta.add(block);
        }

        return respuesta;
    }

    @PostMapping("/Bloqueo/PostBloqueos")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    boolean InsertarListaBloqueos(@RequestBody List<BloqueoModel> bloqueosModel) {
        try {
            bloqueoRepository.saveAll(bloqueosModel);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return true;
    }

    @PostMapping("/bloqueo/listar_por_fechas")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
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