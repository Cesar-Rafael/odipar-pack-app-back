package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.PedidoRepository;
import com.pucp.odiparpackappback.models.OficinaModel;
import com.pucp.odiparpackappback.models.PedidoModel;
import com.pucp.odiparpackappback.services.algorithm.ABC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @GetMapping("/Pedido/id")
    @ResponseBody
    List<PedidoModel> ListarPedidoxId(@RequestParam(required = false) Long id){
        List<PedidoModel> pedidosAux = (List<PedidoModel>) pedidoRepository.findAll();
        List<PedidoModel> pedidos = new ArrayList<>();
        for(int i = 0; i < pedidosAux.size(); i++){
            if(Objects.equals(pedidosAux.get(i).getId(), id)){
                pedidos.add(pedidosAux.get(i));
            }
        }
        return pedidos;
    }

    @GetMapping("/ABC/")
    boolean ejecutarABC(){
        ABC abc = new ABC();
        abc.algoritmoAbejasVPRTW(10, 10, 10);
        return true;
    }
}
