package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.models.PedidoModel;
import com.pucp.odiparpackappback.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/Pedido")
public class PedidoController {
    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService){
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoModel> getPedidos(){
        return pedidoService.getPedidos();
    }
}
