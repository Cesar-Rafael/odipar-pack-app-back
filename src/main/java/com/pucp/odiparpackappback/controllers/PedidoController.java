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
    public List<PedidoModel> listarPedidos() {
        return (List<PedidoModel>) pedidoRepository.findAll();
    }

    @GetMapping("/Pedido/{id}")
    Optional<PedidoModel> ListarPedidoxId(@PathVariable("id") long id) {
        return pedidoRepository.findById(id);
    }

    @PostMapping("/Pedido/Insertar")
    PedidoModel InsertarPedido(@RequestBody PedidoModel pedidoModel) {
        return pedidoRepository.save(pedidoModel);
    }

    @PostMapping("/Pedido/Insertar/Masivo")
    boolean InsertarListaPedidos(@RequestBody List<PedidoModel> pedidosModel) {
        try {
            pedidoRepository.saveAll(pedidosModel);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return true;
    }

    @PostMapping("/ABCDD")
    public boolean ejecutarABCDiaDia() {
        ABC abc = new ABC();

        // Lectura desde BD
        Mapa.cargarOficinasDiaDia();
        Mapa.cargarTramosDiaDia();
        Mapa.cargarVehiculosDiaDia(Mapa.inicioDiaDia, 1);
        Mapa.cargarBloqueosDiaDia();

        // Rango de Simulación
        Mapa.inicioDiaDia = LocalDateTime.now().minusDays(3);
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

        // Actualización
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ejecutarABCDD2();
            }
        }, 18000000, 18000000); // Siempre se ejecuta después de 5 horas minutos - 18,000,000 segundos
        return true;
    }

    boolean ejecutarABCDD2() {
        ABC abc = new ABC();
        // Rango de Simulación
        Mapa.inicioDiaDia = LocalDateTime.now().minusDays(3);
        Mapa.finDiaDia = LocalDateTime.now();

        // Ejecución del Algoritmo
        abc.algoritmoAbejasVPRTW(1);
        System.out.println("¡Rutas actualizadas!");
        return true;
    }

    @PostMapping("/ABCS")
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

<<<<<<< Updated upstream
    boolean ejecutarABCS2(int velocidad) {
        ABC abc = new ABC();

        // Ejecución del Algoritmo
        // El algoritmo debe ejecutarse 28 veces, cada uno con un rango de 6 horas
        for (int zzz = 0; zzz < 27; zzz++) {
            Mapa.inicioSimulacion = Mapa.finSimulacion;
            abc.algoritmoAbejasVPRTW(0);
            System.out.println("Las Rutas han sido actualizadas...");
            // REPORTE INTERNO
            System.out.println("REPORTE ABC SIMULACION: " + (zzz + 1));
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
        }

        return true;
    }

    @GetMapping("/simulacion/reiniciar")
=======
    @GetMapping("/simulacion/detener")
>>>>>>> Stashed changes
    boolean pararSimulacion() {
        Mapa.pedidosSimulacion.clear();
        Mapa.rutasSimulacion.clear();
        Mapa.cargarVehiculosSimulacion("src/main/resources/static/unidad_transporte_model.csv");
        return true;
    }

    @PostMapping("/PararDiaDia")
    boolean pararDiaDia() {
        Mapa.setFlag(false);
        Mapa.pedidosSimulacion.clear();
        Mapa.rutasSimulacion.clear();
        Mapa.vehiculosDiaDia.clear();
        return true;
    }
}
