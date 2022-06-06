package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.PedidoRepository;
import com.pucp.odiparpackappback.models.Mapa;
import com.pucp.odiparpackappback.models.PedidoModel;
import com.pucp.odiparpackappback.services.algorithm.ABC;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PedidoController {
    private final PedidoRepository pedidoRepository;

    public PedidoController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @GetMapping("/Pedido/")
    public List<PedidoModel> listarPedidos() {
        return (List<PedidoModel>) pedidoRepository.findAll();
    }

    @GetMapping("/Pedido/id")
    @ResponseBody
    List<PedidoModel> ListarPedidoxId(@RequestParam(required = false) Long id) {
        List<PedidoModel> pedidosAux = (List<PedidoModel>) pedidoRepository.findAll();
        List<PedidoModel> pedidos = new ArrayList<>();
        for (int i = 0; i < pedidosAux.size(); i++) {
            if (Objects.equals(pedidosAux.get(i).getId(), id)) {
                pedidos.add(pedidosAux.get(i));
            }
        }
        return pedidos;
    }

    @PostMapping("/Pedido/PostPedido")
    PedidoModel InsertarPedido(@RequestBody PedidoModel pedidoModel) {
        return pedidoRepository.save(pedidoModel);
    }

    @PostMapping("/Pedido/PostPedidos")
    boolean InsertarListaPedidos(@RequestBody List<PedidoModel> pedidosModel) {
        for (int i = 0; i < pedidosModel.size(); i++) {
            pedidoRepository.save(pedidosModel.get(i));
        }
        return true;
    }

    @GetMapping("/ABC/")
    boolean ejecutarABC() {
        ABC abc = new ABC();

        //Mapa.cargarPedidos();
        Mapa.cargarOficinas();
        Mapa.cargarTramos();
        Mapa.cargarVehiculos();

        abc.algoritmoAbejasVPRTW(10, 10, 10);

        // BD RutaModel
        System.out.println("REPORTE:");
        System.out.println("Cantidad Pedidos:");
        System.out.println(Mapa.pedidos.size());
        for (int i = 0; i < Mapa.rutas.size(); i++) {
            System.out.println("IdRuta:");
            System.out.println(Mapa.rutas.get(i).getIdRuta());
            System.out.println("IdUnidadTransporte:");
            System.out.println(Mapa.rutas.get(i).getIdUnidadTransporte());
            System.out.println("Seguimiento:");
            System.out.println(Mapa.rutas.get(i).getSeguimiento());
            System.out.println("Tramos:");
            for (int j = 0; j < Mapa.rutas.get(i).getTramos().size(); j++) {
                System.out.println("Ciudad i:");
                System.out.println(Mapa.rutas.get(i).getTramos().get(j).getIdCiudadI());
                System.out.println("Ciudad j:");
                System.out.println(Mapa.rutas.get(i).getTramos().get(j).getIdCiudadJ());
            }
            System.out.println();
        }

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ejecutarABC2();
            }
        }, 300000, 300000);

        return true;
    }

    @GetMapping("/ABC2/")
    boolean ejecutarABC2() {
        ABC abc = new ABC();
        abc.algoritmoAbejasVPRTW(10, 10, 10);
        System.out.println("Me he ejecutado :)");
        return true;
    }
}
