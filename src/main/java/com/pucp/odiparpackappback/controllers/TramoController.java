package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.TramoRepository;
import com.pucp.odiparpackappback.models.Mapa;
import com.pucp.odiparpackappback.models.TramoModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TramoController {
    private final TramoRepository tramoRepository;

    public TramoController(TramoRepository tramoRepository) {
        this.tramoRepository = tramoRepository;
    }

    @GetMapping("/Tramo/")
    List<TramoModel> listarTramos() {
        return (List<TramoModel>) tramoRepository.findAll();
    }

    @GetMapping("/TramosUsados/")
    List<TramoModel> listarTramosUsados() {
        List<TramoModel> tramosAux = (List<TramoModel>) tramoRepository.findAll();
        List<TramoModel> tramos = new ArrayList<>();
        Long idTramo = Long.valueOf(0);
        List<Long> tramosUsados = new ArrayList<>();
        for (int i = 0; i < Mapa.rutas.size(); i++) {
            for (int j = 0; j < Mapa.rutas.get(i).getTramos().size(); j++) {
                int idCiudadI = Mapa.rutas.get(i).getTramos().get(j).getIdCiudadI();
                int idCiudadJ = Mapa.rutas.get(i).getTramos().get(j).getIdCiudadJ();
                for (int k = 0; k < tramosAux.size(); k++) {
                    if (tramosAux.get(k).getIdCiudadI() == idCiudadI && tramosAux.get(k).getIdCiudadJ() == idCiudadJ) {
                        tramos.add(tramosAux.get(k));
                        //idTramo = tramosAux.get(k).getId();
                    }
                }
                //tramosUsados.add(idTramo);
            }
        }
        return tramos;
    }
}
