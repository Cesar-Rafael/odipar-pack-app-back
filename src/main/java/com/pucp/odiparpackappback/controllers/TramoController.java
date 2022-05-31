package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.TramoRepository;
import com.pucp.odiparpackappback.models.TramoModel;
import com.pucp.odiparpackappback.models.UnidadTransporteModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TramoController {
    private final TramoRepository tramoRepository;
    public TramoController(TramoRepository tramoRepository) {
        this.tramoRepository = tramoRepository;
    }

    @GetMapping("/Tramo/")
    List<TramoModel> listarTramos(){
        return (List<TramoModel>) tramoRepository.findAll();
    }
}
