package com.pucp.odiparpackappback.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucp.odiparpackappback.Repositories.PedidoRepository;
import com.pucp.odiparpackappback.dto.Simulation;
import com.pucp.odiparpackappback.models.*;
import com.pucp.odiparpackappback.services.algorithm.ABC;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
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

    @GetMapping("/ABCDD")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    public boolean ejecutarABCDiaDia() {
        ABC abc = new ABC();

        // Lectura desde BD
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
        for (int i = 0; i < simulation.pedidos.size(); i++) {
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
            if(Mapa.flagColapso){
                map.put("colasoLogistico", 1.0);
            }
            else{
                map.put("colasoLogistico", 0.0);
            }

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

    @GetMapping("/Pedido/ListarRutasSimulacionxIdPedido/{idPedido}")
    @ResponseBody
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    public List<RutaConArraySegHorasLl> ListarRutasSimulacionxIdPedido(@PathVariable("idPedido") long idPedido) {
        try {
            List<RutaConArraySegHorasLl> auxRutasG = new ArrayList<>();
            for(int i=0; i<Mapa.rutasSimulacion.size(); i++){
                for(int j=0; j<Mapa.rutasSimulacion.get(i).getPedidosParciales().size(); j++){
                    if(Mapa.rutasSimulacion.get(i).getPedidosParciales().get(j).getIdPedido() == idPedido){
                        ArrayList<Integer> auxAI = new ObjectMapper().reader(List.class).readValue(Mapa.rutasSimulacion.get(i).getSeguimiento());
                        ArrayList<String> auxNombreProvincias = new ArrayList<>();
                        for (int zz = 0; zz < auxAI.size(); zz++) {
                            for (int z = 0; z < Mapa.oficinas.size(); z++) {
                                if (Mapa.oficinas.get(z).getUbigeo() == auxAI.get(zz)) {
                                    auxNombreProvincias.add(zz, Mapa.oficinas.get(z).getProvincia());
                                }
                            }
                        }
                        String codigoPlaca = null;
                        for (int b = 0; b < Mapa.vehiculosSimulacion.size(); b++) {
                            if (Mapa.vehiculosSimulacion.get(b).getId() == Mapa.rutasSimulacion.get(i).getIdUnidadTransporte()) {
                                codigoPlaca = Mapa.vehiculosSimulacion.get(b).getCodigo();
                            }
                        }
                        ArrayList<PedidoModel> pedidos = new ArrayList<>();
                        for (int c = 0; c < Mapa.pedidosSimulacion.size(); c++) {
                            if(Mapa.pedidosSimulacion.get(c).getId() == idPedido){
                                //
                                for(int zzz=0; zzz<Mapa.oficinas.size();zzz++){
                                    if(Mapa.oficinas.get(zzz).getUbigeo() == Mapa.pedidosSimulacion.get(c).getIdCiudadDestino()){
                                        Mapa.pedidosSimulacion.get(c).setCiudadDestino(Mapa.oficinas.get(zzz).getProvincia());
                                    }
                                }
                                //
                                pedidos.add(Mapa.pedidosSimulacion.get(c));
                            }
                        }
                        RutaConArraySegHorasLl auxRutaG = new RutaConArraySegHorasLl((long) auxRutasG.size(), Mapa.rutasSimulacion.get(i).getIdRuta(), Mapa.rutasSimulacion.get(i).getIdUnidadTransporte(), auxAI, auxNombreProvincias, Mapa.rutasSimulacion.get(i).getHorasDeLlegada(), codigoPlaca, pedidos);
                        auxRutasG.add(auxRutaG);
                    }
                }
            }
            return auxRutasG;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    @GetMapping("/Pedido/ListarRutasDiaDiaxIdPedido/{idPedido}")
    @ResponseBody
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    public List<RutaConArraySegHorasLl> ListarRutasDiaDiaxIdPedido(@PathVariable("idPedido") long idPedido) {
        try {
            List<RutaConArraySegHorasLl> auxRutasG = new ArrayList<>();
            for(int i=0; i<Mapa.rutasDiaDia.size(); i++){
                for(int j=0; j<Mapa.rutasDiaDia.get(i).getPedidosParciales().size(); j++){
                    if(Mapa.rutasDiaDia.get(i).getPedidosParciales().get(j).getIdPedido() == idPedido){
                        ArrayList<Integer> auxAI = new ObjectMapper().reader(List.class).readValue(Mapa.rutasDiaDia.get(i).getSeguimiento());
                        ArrayList<String> auxNombreProvincias = new ArrayList<>();
                        for (int zz = 0; zz < auxAI.size(); zz++) {
                            for (int z = 0; z < Mapa.oficinas.size(); z++) {
                                if (Mapa.oficinas.get(z).getUbigeo() == auxAI.get(zz)) {
                                    auxNombreProvincias.add(zz, Mapa.oficinas.get(z).getProvincia());
                                }
                            }
                        }
                        String codigoPlaca = null;
                        for (int b = 0; b < Mapa.vehiculosDiaDia.size(); b++) {
                            if (Mapa.vehiculosDiaDia.get(b).getId() == Mapa.rutasDiaDia.get(i).getIdUnidadTransporte()) {
                                codigoPlaca = Mapa.vehiculosDiaDia.get(b).getCodigo();
                            }
                        }
                        ArrayList<PedidoModel> pedidos = new ArrayList<>();
                        for (int c = 0; c < Mapa.pedidosDiaDia.size(); c++) {
                            if(Mapa.pedidosDiaDia.get(c).getId() == idPedido){
                                //
                                for(int zzz=0; zzz<Mapa.oficinas.size();zzz++){
                                    if(Mapa.oficinas.get(zzz).getUbigeo() == Mapa.pedidosDiaDia.get(c).getIdCiudadDestino()){
                                        Mapa.pedidosDiaDia.get(c).setCiudadDestino(Mapa.oficinas.get(zzz).getProvincia());
                                    }
                                }
                                //
                                pedidos.add(Mapa.pedidosDiaDia.get(c));
                            }
                        }
                        RutaConArraySegHorasLl auxRutaG = new RutaConArraySegHorasLl((long) auxRutasG.size(), Mapa.rutasDiaDia.get(i).getIdRuta(), Mapa.rutasDiaDia.get(i).getIdUnidadTransporte(), auxAI, auxNombreProvincias, Mapa.rutasDiaDia.get(i).getHorasDeLlegada(), codigoPlaca, pedidos);
                        auxRutasG.add(auxRutaG);
                    }
                }
            }
            return auxRutasG;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }
}
