package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.BloqueoRepository;
import com.pucp.odiparpackappback.models.BloqueoModel;
import com.pucp.odiparpackappback.models.OficinaModel;
import com.pucp.odiparpackappback.models.PedidoModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("List<BloqueoModel>/Bloqueo/PostBloqueos")
    boolean InsertarListaBloqueos(@RequestBody List<BloqueoModel> bloqueosModel){
        List<BloqueoModel> listaAux = listarBloqueos();
        for(int i = 0; i < bloqueosModel.size(); i++) {
            try {
                Long indice = Long.valueOf(listaAux.size() + i);
                bloqueosModel.get(i).setId(indice);
                // Se guarda en la BD
                bloqueoRepository.save(bloqueosModel.get(i));
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
        return true;
    }
}