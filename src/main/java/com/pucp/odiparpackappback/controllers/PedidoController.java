package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.PedidoRepository;
import com.pucp.odiparpackappback.dto.Simulation;
import com.pucp.odiparpackappback.models.EstadoUnidadTransporte;
import com.pucp.odiparpackappback.models.Mapa;
import com.pucp.odiparpackappback.models.PedidoModel;
import com.pucp.odiparpackappback.services.algorithm.ABC;
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

    @GetMapping("/Pedido/Listar")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    public List<PedidoModel> listarPedidos() {
        return (List<PedidoModel>) pedidoRepository.findAll();
    }

    @GetMapping("/Pedido/{id}")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    Optional<PedidoModel> ListarPedidoxId(@PathVariable("id") long id) {
        return pedidoRepository.findById(id);
    }

    @PostMapping("/Pedido/Insertar")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    PedidoModel InsertarPedido(@RequestBody PedidoModel pedidoModel) {
        return pedidoRepository.save(pedidoModel);
    }

    @PostMapping("/Pedido/Insertar/Masivo")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    boolean InsertarListaPedidos(@RequestBody List<PedidoModel> pedidosModel) {
        try {
            pedidoRepository.saveAll(pedidosModel);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return true;
    }

    @PostMapping("/ABCDD")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    public boolean ejecutarABCDiaDia() {
        ABC abc = new ABC();

        // Lectura desde BD
        Mapa.cargarOficinasDiaDia();
        Mapa.cargarTramosDiaDia();
        Mapa.cargarVehiculosDiaDia();
        Mapa.cargarBloqueosDiaDia();

        // Rango de Simulación
        Mapa.finDiaDia = LocalDateTime.now();

        // Ejecución del Algoritmo
        abc.algoritmoAbejasVPRTW(1);

        // REPORTE INTERNO
        System.out.println("REPORTE ABC DIA A DIA:");
        System.out.println("Cantidad Pedidos:");
        System.out.println(Mapa.pedidosDiaDia.size());
        for (int i = 0; i < Mapa.rutasDiaDia.size(); i++) {
            System.out.println("IdRuta:");
            System.out.println(Mapa.rutasDiaDia.get(i).getIdRuta());
            System.out.println("IdUnidadTransporte:");
            System.out.println(Mapa.rutasDiaDia.get(i).getIdUnidadTransporte());
            System.out.println("Seguimiento:");
            System.out.println(Mapa.rutasDiaDia.get(i).getSeguimiento());
            System.out.println(Mapa.rutasDiaDia.get(i).getHorasDeLlegada());
            System.out.println();
        }
        return true;
    }

    @PostMapping("/ABCS")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    Map<String, Double> ejecutarABCSimulacion(@RequestBody Simulation simulation) {
        ABC abc = new ABC();

        // Carga de Pedidos
        for(int i=0; i<simulation.pedidos.size();i++){
            Mapa.pedidosSimulacion.add(simulation.pedidos.get(i));
        }

        // Ejecución del Algoritmo
        Mapa.inicioSimulacion = LocalDateTime.ofInstant(simulation.inicioSimulacion.toInstant(), ZoneId.systemDefault());
        abc.algoritmoAbejasVPRTW(0);

        if (simulation.finalizado) {
            // REPORTE INTERNO
            System.out.println("REPORTE ABC SIMULACION:");
            for (int i = 0; i < Mapa.rutasSimulacion.size(); i++) {
                Mapa.vehiculosSimulacion.get(Math.toIntExact(Mapa.rutasSimulacion.get(i).getIdUnidadTransporte())).setEstado(EstadoUnidadTransporte.EN_TRANSITO);
                System.out.println("IdRuta:");
                System.out.println(Mapa.rutasSimulacion.get(i).getIdRuta());
                System.out.println("IdUnidadTransporte:");
                System.out.println(Mapa.rutasSimulacion.get(i).getIdUnidadTransporte());
                System.out.println("Seguimiento:");
                System.out.println(Mapa.rutasSimulacion.get(i).getSeguimiento());
                System.out.println(Mapa.rutasSimulacion.get(i).getHorasDeLlegada());
                System.out.println("Pedidos Parciales:");
                for (int j = 0; j < Mapa.rutasSimulacion.get(i).getPedidosParciales().size(); j++) {
                    System.out.println(Mapa.rutasSimulacion.get(i).getPedidosParciales().get(j));
                }
                System.out.println();
            }

            // REPORTE EXTERNO
            HashMap<Long, Long> pedidos = new HashMap<>();
            Double numRutas = Mapa.rutasSimulacion.size() + 0.0;
            Double tiempo = 0.0;
            Double numPedidos = Mapa.pedidosSimulacion.size() + 0.0;
            for (int i = 0; i < Mapa.rutasSimulacion.size(); i++) {
                for (int j = 0; j < Mapa.rutasSimulacion.get(i).getPedidosParciales().size(); j++) {
                    if (pedidos.containsKey(Mapa.rutasSimulacion.get(i).getPedidosParciales().get(j).getIdPedido())) {
                        if (Mapa.rutasSimulacion.get(i).getPedidosParciales().get(j).getFechaHoraEntrega() > pedidos.get(Mapa.rutasSimulacion.get(i).getPedidosParciales().get(j).getIdPedido()))
                            pedidos.put(Mapa.rutasSimulacion.get(i).getPedidosParciales().get(j).getIdPedido(), Mapa.rutasSimulacion.get(i).getPedidosParciales().get(j).getFechaHoraEntrega());
                    } else {
                        pedidos.put(Mapa.rutasSimulacion.get(i).getPedidosParciales().get(j).getIdPedido(), Mapa.rutasSimulacion.get(i).getPedidosParciales().get(j).getFechaHoraEntrega());
                    }
                }
            }
            for (int i = 0; i < Mapa.pedidosSimulacion.size(); i++) {
                if (pedidos.containsKey(Mapa.pedidosSimulacion.get(i).getId())) {
                    tiempo += pedidos.get(Mapa.pedidosSimulacion.get(i).getId()) - Mapa.pedidosSimulacion.get(i).getFechaHoraCreacion().getTime() / 1000;
                } else numPedidos--;
            }

            Double promTiempo = tiempo / numPedidos;
            HashMap<String, Double> map = new HashMap<>();
            map.put("cantRutas", numRutas);
            map.put("promTiempo", promTiempo);
            return map;
        }

        return null;
    }

    @GetMapping("/simulacion/reiniciar")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    boolean pararSimulacion() {
        Mapa.pedidosSimulacion.clear();
        Mapa.rutasSimulacion.clear();
        Mapa.cargarVehiculosSimulacion("src/main/resources/static/unidad_transporte_model.csv");
        return true;
    }

    @PostMapping("/PararDiaDia")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    boolean pararDiaDia() {
        Mapa.setFlag(false);
        Mapa.pedidosSimulacion.clear();
        Mapa.rutasSimulacion.clear();
        Mapa.vehiculosDiaDia.clear();
        return true;
    }
}
