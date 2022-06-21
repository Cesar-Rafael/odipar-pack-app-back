package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.BloqueoRepository;
import com.pucp.odiparpackappback.models.BloqueoModel;
import com.pucp.odiparpackappback.models.OficinaModel;
import com.pucp.odiparpackappback.models.PedidoModel;
import com.pucp.odiparpackappback.models.RutaModel;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
public class BloqueoController {

    private final BloqueoRepository bloqueoRepository;
    public BloqueoController(BloqueoRepository bloqueoRepository) {
        this.bloqueoRepository = bloqueoRepository;
    }

    @GetMapping("/Bloqueo/")
    List<BloqueoModel> listarBloqueos(){
        return (List<BloqueoModel>) bloqueoRepository.findAll();
    }

    @PostMapping("/Bloqueo/PostBloqueos")
    boolean InsertarListaBloqueos(@RequestBody List<BloqueoModel> bloqueosModel){
        try{
            bloqueoRepository.saveAll(bloqueosModel);
        }catch(Exception ex){
            System.out.println(ex);
        }
        return true;
    }

    @GetMapping("/bloqueo/{fechaInicio}/{fechaFin}")
    @ResponseBody
    List<BloqueoModel> ListarBloqueosEntreFechas(@PathVariable("fechaInicio") String fechaInicio,@PathVariable("fechaFin") String fechaFin) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date fechaInicioDate = sdf.parse(fechaInicio);
            Date fechaFinDate = sdf.parse(fechaFin);
            List<BloqueoModel> bloqueos = bloqueoRepository.findBloqueoModelByFechaInicioAndAndFechaFin(fechaInicioDate, fechaFinDate);
            if (bloqueos != null) {
                return bloqueos;
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}