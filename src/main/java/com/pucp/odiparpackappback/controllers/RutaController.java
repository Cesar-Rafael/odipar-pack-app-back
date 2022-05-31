package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.RutaRepository;
import com.pucp.odiparpackappback.models.PedidoModel;
import com.pucp.odiparpackappback.models.RutaModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RutaController {
    private final RutaRepository rutaRepository;
    public RutaController(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @GetMapping("/Ruta/")
    public List<RutaModel> listarRutas(){
        return (List<RutaModel>) rutaRepository.findAll();
    }
}
