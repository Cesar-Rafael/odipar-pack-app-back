package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.PedidoRepository;
import com.pucp.odiparpackappback.dto.Simulation;
import com.pucp.odiparpackappback.models.Mapa;
import com.pucp.odiparpackappback.models.PedidoModel;
import com.pucp.odiparpackappback.services.algorithm.ABC;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

    /*
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
    }*/

    @GetMapping("/Pedido/{id}")
    @ResponseBody
    Optional<PedidoModel> ListarPedidoxId(@PathVariable("id") long id) {
        return pedidoRepository.findById(id);
    }

    @PostMapping("/Pedido/PostPedido")
    PedidoModel InsertarPedido(@RequestBody PedidoModel pedidoModel) {
        return pedidoRepository.save(pedidoModel);
    }

    @PostMapping("/Pedido/PostPedidos")
    boolean InsertarListaPedidos(@RequestBody List<PedidoModel> pedidosModel) {
        try{
            pedidoRepository.saveAll(pedidosModel);
        }catch(Exception ex){
            System.out.println(ex);
        }
        return true;
    }

    @PostMapping("/ABCDD")
    boolean ejecutarABCDiaDia(@RequestParam String inicioSimulacionAux) {
        ABC abc = new ABC();
        // Rango de Simulación
        Mapa.inicioSimulacion = LocalDateTime.parse(inicioSimulacionAux);
        Mapa.finSimulacion = Mapa.inicioSimulacion.plusMinutes(90);
        Mapa.inicioSimulacion = Mapa.inicioSimulacion.minusHours(6);
        Mapa.finSimulacion = Mapa.finSimulacion.minusHours(6);
        // Lectura desde BD
        Mapa.cargarOficinasDiaDia();
        Mapa.cargarTramosDiaDia();
        Mapa.cargarVehiculosDiaDia(Mapa.inicioSimulacion, 1);
        // Ejecución del Algoritmo
        abc.algoritmoAbejasVPRTW(10, 5, 5, 1, 1);
        // Reporte Interno
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
            System.out.println(Mapa.rutas.get(i).getHorasDeLlegada());
            System.out.println();
        }
        // Actualización
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ejecutarABCDD2();
            }
        }, 300000, 300000);
        return true;
    }

    boolean ejecutarABCDD2() {
        ABC abc = new ABC();
        abc.algoritmoAbejasVPRTW(10, 5, 5, 1, 1);
        System.out.println("¡Rutas actualizadas!");
        return true;
    }

    @PostMapping("/ABCS")
    boolean ejecutarABCSimulacion(@RequestBody Simulation simulation) {
        ABC abc = new ABC();
        // Lectura de Datos
        Mapa.cargarOficinasSimulacion("src/main/resources/static/oficina_model.csv");
        Mapa.cargarTramosSimulacion("src/main/resources/static/tramo_model.csv");
        Mapa.cargarVehiculosSimulacion("src/main/resources/static/unidad_transporte_model.csv");

        // Rango de Simulación
        Mapa.inicioSimulacion = LocalDateTime.ofInstant(simulation.inicioSimulacion.toInstant(), ZoneId.systemDefault());
        Mapa.finSimulacion = Mapa.inicioSimulacion.plusDays(7);

        Mapa.cargarVehiculosDiaDia(Mapa.inicioSimulacion, simulation.velocidad);
        //Mapa.inicioSimulacion = Mapa.inicioSimulacion.minusHours(6);
        //Mapa.finSimulacion = Mapa.finSimulacion.minusHours(6);

        // Carga de Pedidos y Bloqueos
        Mapa.pedidos = simulation.pedidos;

        // Bloqueos de la BD

        // Ejecución del Algoritmo
        abc.algoritmoAbejasVPRTW(10, 5, 5, 0, simulation.velocidad);

        // Reporte Interno
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
            System.out.println(Mapa.rutas.get(i).getHorasDeLlegada());
            System.out.println();
        }

        // Actualización
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!Mapa.flag){     // Si ha terminado la simulación, se para la re-ejecución
                    return;
                }
                else{
                    ejecutarABCS2(simulation.velocidad);
                }
            }
        }, 300000, 300000); // Siempre se ejecuta después de 5 minutos - 300,000 segundos
        return true;
    }

    boolean ejecutarABCS2(int velocidad) {
        ABC abc = new ABC();
        Mapa.cargarVehiculosDiaDia(Mapa.inicioSimulacion, velocidad);
        abc.algoritmoAbejasVPRTW(10, 5, 5, 0, velocidad);
        System.out.println("¡Rutas actualizadas!");
        return true;
    }

    @PostMapping("/PararSimulacion")
    boolean pararSimulacion(){
        Mapa.setFlag(false);
        Mapa.pedidos.clear();
        Mapa.rutas.clear();
        return true;
    }
}
