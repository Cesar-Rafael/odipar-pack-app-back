package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.PedidoRepository;
import com.pucp.odiparpackappback.models.Mapa;
import com.pucp.odiparpackappback.models.PedidoModel;
import com.pucp.odiparpackappback.services.algorithm.ABC;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@EnableScheduling
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

    @PutMapping("/Pedido/Put/{id}")
    PedidoModel InsertarPedido(@RequestBody PedidoModel pedidoModel, @PathVariable Long id){
        return pedidoRepository.findById(id)
                .map(pedido -> {
                    pedido.setIdCliente(pedidoModel.getIdCliente());
                    pedido.setNombreCompletoCliente(pedidoModel.getNombreCompletoCliente());
                    pedido.setCorreoCliente(pedidoModel.getCorreoCliente());
                    pedido.setCantPaquetes(pedidoModel.getCantPaquetes());
                    pedido.setCantPaquetesNoAsignado(pedidoModel.getCantPaquetesNoAsignado());
                    pedido.setIdCiudadDestino(pedidoModel.getIdCiudadDestino());
                    pedido.setCiudadDestino(pedidoModel.getCiudadDestino());
                    pedido.setFechaHoraCreacion(pedidoModel.getFechaHoraCreacion());
                    pedido.setEstado(pedidoModel.getEstado());
                    return pedidoRepository.save(pedido);
                })
                .orElseGet(() -> {
                    pedidoModel.setId(id);
                    return pedidoRepository.save(pedidoModel);
                });
    }


    @GetMapping("/ABC/")
    boolean ejecutarABC(){
        ABC abc = new ABC();

        Mapa.cargarPedidos();
        Mapa.cargarOficinas();
        Mapa.cargarTramos();
        Mapa.cargarVehiculos();

        abc.algoritmoAbejasVPRTW(10, 10, 10);

        // BD RutaModel
        System.out.println("REPORTE:");
        for(int i=0; i < Mapa.rutas.size(); i++){
            System.out.println("IdRuta:");
            System.out.println(Mapa.rutas.get(i).getIdRuta());
            System.out.println("IdUnidadTransporte:");
            System.out.println(Mapa.rutas.get(i).getIdUnidadTransporte());
            System.out.println("Seguimiento:");
            System.out.println(Mapa.rutas.get(i).getSeguimiento());
            System.out.println("Tramos:");
            for(int j=0; j < Mapa.rutas.get(i).getTramos().size(); j++){
                System.out.println(Mapa.rutas.get(i).getTramos().get(j).getIdCiudadI());
                System.out.println(Mapa.rutas.get(i).getTramos().get(j).getIdCiudadJ());
            }
            System.out.println();
        }

        return true;
    }

    @Scheduled(fixedRate = 5000)
    @GetMapping(value="/hello/")
    public void greeting() {
        System.out.println("Hello!!!");
    }
}
