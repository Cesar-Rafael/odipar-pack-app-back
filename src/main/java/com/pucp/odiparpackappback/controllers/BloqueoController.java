package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.BloqueoRepository;
import com.pucp.odiparpackappback.models.BloqueoModel;
import com.pucp.odiparpackappback.models.OficinaModel;
import org.springframework.web.bind.annotation.GetMapping;
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
}