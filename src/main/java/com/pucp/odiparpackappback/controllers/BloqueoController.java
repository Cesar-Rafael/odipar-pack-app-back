package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.BloqueoRepository;
import com.pucp.odiparpackappback.dto.BloqueoBody;
import com.pucp.odiparpackappback.models.BloqueoModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class BloqueoController {

    private final BloqueoRepository bloqueoRepository;

    public BloqueoController(BloqueoRepository bloqueoRepository) {
        this.bloqueoRepository = bloqueoRepository;
    }

    @GetMapping("/Bloqueo/")
    List<BloqueoModel> listarBloqueos() {
        return (List<BloqueoModel>) bloqueoRepository.findAll();
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