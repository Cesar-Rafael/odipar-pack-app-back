package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.PedidoRepository;
import com.pucp.odiparpackappback.models.PedidoModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PedidoController {
    private final PedidoRepository pedidoRepository;
    public PedidoController(PedidoRepository pedidoRepository){
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping("/Pedido/")
    public List<PedidoModel> listarPedidos(){
        return (List<PedidoModel>) pedidoRepository.findAll();
    }
}
