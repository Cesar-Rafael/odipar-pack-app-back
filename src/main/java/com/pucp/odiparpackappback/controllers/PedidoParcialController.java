package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.PedidoParcialRepository;
import com.pucp.odiparpackappback.models.PedidoParcialModel;
import com.pucp.odiparpackappback.models.TramoModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PedidoParcialController {
    private final PedidoParcialRepository pedidoParcialRepository;
    public PedidoParcialController(PedidoParcialRepository pedidoParcialRepository) {
        this.pedidoParcialRepository = pedidoParcialRepository;
    }

    @GetMapping("/PedidoParcial/")
    List<PedidoParcialModel> listarPedidosParciales(){
        return (List<PedidoParcialModel>) pedidoParcialRepository.findAll();
    }
}
