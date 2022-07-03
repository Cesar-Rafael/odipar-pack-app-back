package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.OficinaRepository;
import com.pucp.odiparpackappback.models.OficinaModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class OficinaController {

    private final OficinaRepository oficinaRepository;

    OficinaController(OficinaRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    @GetMapping("/Oficina/Listar")
    @CrossOrigin
    List<OficinaModel> listarOficinas() {
        return (List<OficinaModel>) oficinaRepository.findAll();
    }

    @GetMapping("/Oficina/provincia")
    @CrossOrigin
    @ResponseBody
    List<OficinaModel> ListarOficinasxProvincia(@RequestParam(required = false) String provincia) {
        List<OficinaModel> oficinasAux = (List<OficinaModel>) oficinaRepository.findAll();
        List<OficinaModel> oficinas = new ArrayList<>();
        for (int i = 0; i < oficinasAux.size(); i++) {
            if (Objects.equals(oficinasAux.get(i).getProvincia(), provincia)) {
                oficinas.add(oficinasAux.get(i));
            }
        }
        return oficinas;
    }
}