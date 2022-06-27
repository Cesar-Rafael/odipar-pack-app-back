package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.PedidoRepository;
import com.pucp.odiparpackappback.dto.Simulation;
import com.pucp.odiparpackappback.models.Mapa;
import com.pucp.odiparpackappback.models.PedidoModel;
import com.pucp.odiparpackappback.services.algorithm.ABC;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

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

    @PostMapping("/Pedido/Insertar")
    PedidoModel InsertarPedido(@RequestBody PedidoModel pedidoModel) {
        return pedidoRepository.save(pedidoModel);
    }

    @PostMapping("/Pedido/PostPedidos")
    boolean InsertarListaPedidos(@RequestBody List<PedidoModel> pedidosModel) {
        try {
            pedidoRepository.saveAll(pedidosModel);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return true;
    }

    @PostMapping("/ABCDD")
    public boolean ejecutarABCDiaDia(@RequestParam String inicioSimulacionAux) {
        ABC abc = new ABC();

        // Lectura desde BD
        Mapa.cargarOficinasDiaDia();
        Mapa.cargarTramosDiaDia();
        Mapa.cargarVehiculosDiaDia(Mapa.inicioDiaDia, 1);

        // Rango de Simulación
        Mapa.inicioDiaDia = LocalDateTime.now().minusDays(3);
        Mapa.finDiaDia = LocalDateTime.now();

        // Ejecución del Algoritmo
        abc.algoritmoAbejasVPRTW(10, 5, 5, 1, 1);

        // Reporte Interno
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
        abc.algoritmoAbejasVPRTW(10, 5, 5, 1, 1);
        System.out.println("¡Rutas actualizadas!");
        return true;
    }

    Timer timer;

    @PostMapping("/ABCS")
    boolean ejecutarABCSimulacion(@RequestBody Simulation simulation) {
        ABC abc = new ABC();
        // Carga de Pedidos
        Mapa.pedidosSimulacion = simulation.pedidos;

        // Lectura de Datos
        Mapa.cargarOficinasSimulacion("src/main/resources/static/oficina_model.csv");
        Mapa.cargarTramosSimulacion("src/main/resources/static/tramo_model.csv");
        Mapa.cargarVehiculosSimulacion("src/main/resources/static/unidad_transporte_model.csv");
        Mapa.cargarBloqueosSimulacion("src/main/resources/static/bloqueo_model.csv");

        // Rango de Simulación
        Mapa.inicioSimulacion = LocalDateTime.ofInstant(simulation.inicioSimulacion.toInstant(), ZoneId.systemDefault());

        // Ejecución del Algoritmo
        abc.algoritmoAbejasVPRTW(5, 2, 2, 0, simulation.velocidad);
        // Reporte Interno
        System.out.println("REPORTE ABC SIMULACION:");
        System.out.println("Cantidad Pedidos:");
        System.out.println(Mapa.pedidosSimulacion.size());
        for (int i = 0; i < Mapa.rutasSimulacion.size(); i++) {
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
        return true;
    }

    boolean ejecutarABCS2(int velocidad) {
        ABC abc = new ABC();
        Mapa.cargarVehiculosDiaDia(Mapa.finSimulacion, velocidad);
        Mapa.inicioSimulacion = Mapa.finSimulacion;
        abc.algoritmoAbejasVPRTW(5, 2, 2, 0, velocidad);
        System.out.println("¡Rutas actualizadas!");
        return true;
    }

    @GetMapping("/simulacion/detener")
    boolean pararSimulacion() {
        timer.cancel();
        Mapa.pedidosSimulacion.clear();
        Mapa.rutasSimulacion.clear();
        return true;
    }

    @PostMapping("/PararDiaDia")
    boolean pararDiaDia() {
        Mapa.setFlag(false);
        Mapa.pedidosSimulacion.clear();
        Mapa.rutasSimulacion.clear();
        return true;
    }
}
