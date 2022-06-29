package com.pucp.odiparpackappback.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucp.odiparpackappback.Repositories.RutaRepository;
import com.pucp.odiparpackappback.models.*;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RutaController {
    private final RutaRepository rutaRepository;

    public RutaController(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @GetMapping("/Ruta/")
    public List<RutaModel> listarRutas() {
        return (List<RutaModel>) rutaRepository.findAll();
    }

    @GetMapping("/ruta/ListarRutasxIdVehiculoDiaDia/{idVehiculo}")
    @ResponseBody
    public List<RutaConArraySegHorasLl> ListarRutasxIdVehiculoDiaDia(@PathVariable("idVehiculo") long idVehiculo) {
        try {
            List<RutaConArraySegHorasLl> auxRutasG = new ArrayList<>();
            List<RutaModel> rutas = rutaRepository.findByIdUnidadTransporte(idVehiculo);
            for (int i = 0; i < rutas.size(); i++) {
                ArrayList<Integer> auxAI = new ObjectMapper().reader(List.class).readValue(rutas.get(i).getSeguimiento());
                ArrayList<String> auxNombreProvincias = new ArrayList<>();
                for(int zz=0; zz < auxAI.size(); zz++){
                    for(int z=0; z<Mapa.oficinas.size(); z++){
                        if(Mapa.oficinas.get(z).getUbigeo() == auxAI.get(zz)){
                            auxNombreProvincias.add(Mapa.oficinas.get(z).getProvincia());
                        }
                    }
                }
                StringBuilder sb = new StringBuilder(rutas.get(i).getArrayHorasLlegada());
                sb.insert(0, '[');
                sb.insert(rutas.get(i).getArrayHorasLlegada().length(), ']');
                ArrayList<Integer> auxAIantesL = new ObjectMapper().reader(List.class).readValue(sb.toString());
                ArrayList<Long> auxAL = new ArrayList<Long>();
                for (int a = 0; a < auxAIantesL.size(); a++) {
                    auxAL.add(auxAIantesL.get(a).longValue());
                }
                String codigoPlaca = null;
                for (int b = 0; b < Mapa.vehiculosDiaDia.size(); b++) {
                    if (Mapa.vehiculosDiaDia.get(b).getId() == rutas.get(i).getIdUnidadTransporte()) {
                        codigoPlaca = Mapa.vehiculosDiaDia.get(i).getCodigo();
                    }
                }
                ArrayList<PedidoModel> pedidos = new ArrayList<>();
                for (int j = 0; j < Mapa.rutasDiaDia.size(); j++) {
                    if (Mapa.rutasDiaDia.get(j).getIdUnidadTransporte() == idVehiculo) {
                        for (int c = 0; c < Mapa.rutasDiaDia.get(j).getPedidosParciales().size(); c++) {
                            for (int d = 0; d < Mapa.pedidosDiaDia.size(); d++) {
                                System.out.println("Si me imprimo");
                                if (Mapa.rutasDiaDia.get(j).getPedidosParciales().get(c).getIdPedido() == Mapa.pedidosDiaDia.get(d).getId()) {
                                    System.out.println("Aqui si");
                                    pedidos.add(Mapa.pedidosDiaDia.get(d));
                                }
                            }
                        }
                    }
                }
                RutaConArraySegHorasLl auxRutaG = new RutaConArraySegHorasLl(rutas.get(i).getId(), rutas.get(i).getIdRuta(), rutas.get(i).getIdUnidadTransporte(), auxAI, auxNombreProvincias,auxAL, codigoPlaca, pedidos);
                auxRutasG.add(auxRutaG);
            }
            return auxRutasG;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    @GetMapping("/ruta/ListarRutasxIdVehiculoSimulacion/{idVehiculo}")
    @ResponseBody
    public List<RutaConArraySegHorasLl> ListarRutasxIdVehiculoSimulacion(@PathVariable("idVehiculo") long idVehiculo) {
        try {
            List<RutaConArraySegHorasLl> auxRutasG = new ArrayList<>();
            for (int i = 0; i < Mapa.rutasSimulacion.size(); i++) {
                if (Mapa.rutasSimulacion.get(i).getIdUnidadTransporte() == idVehiculo) {
                    ArrayList<Integer> auxAI = new ObjectMapper().reader(List.class).readValue(Mapa.rutasSimulacion.get(i).getSeguimiento());
                    ArrayList<String> auxNombreProvincias = new ArrayList<>();
                    for(int zz=0; zz < auxAI.size(); zz++){
                        for(int z=0; z<Mapa.oficinas.size(); z++){
                            if(Mapa.oficinas.get(z).getUbigeo() == auxAI.get(zz)){
                                auxNombreProvincias.add(zz, Mapa.oficinas.get(z).getProvincia());
                            }
                        }
                    }
                    String codigoPlaca = null;
                    for (int b = 0; b < Mapa.vehiculosSimulacion.size(); b++) {
                        if (Mapa.vehiculosSimulacion.get(b).getId() == Mapa.rutasSimulacion.get(i).getIdUnidadTransporte()) {
                            codigoPlaca = Mapa.vehiculosSimulacion.get(i).getCodigo();
                        }
                    }
                    ArrayList<PedidoModel> pedidos = new ArrayList<>();
                    for (int c = 0; c < Mapa.rutasSimulacion.get(i).getPedidosParciales().size(); c++) {
                        for (int d = 0; d < Mapa.pedidosSimulacion.size(); d++) {
                            if (Mapa.rutasSimulacion.get(i).getPedidosParciales().get(c).getIdPedido() == Mapa.pedidosSimulacion.get(d).getId()) {
                                pedidos.add(Mapa.pedidosSimulacion.get(d));
                            }
                        }
                    }
                    RutaConArraySegHorasLl auxRutaG = new RutaConArraySegHorasLl(Ruta.getId(), Mapa.rutasSimulacion.get(i).getIdRuta(), Mapa.rutasSimulacion.get(i).getIdUnidadTransporte(), auxAI, auxNombreProvincias, Mapa.rutasSimulacion.get(i).getHorasDeLlegada(), codigoPlaca, pedidos);
                    auxRutasG.add(auxRutaG);
                }
            }
            return auxRutasG;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    @GetMapping("/ruta/simulacion/listar")
    public List<RutaConArraySegHorasLl> ListarRutasSimulacion() {
        try {
            List<RutaConArraySegHorasLl> auxRutasG = new ArrayList<>();
            for (int i = 0; i < Mapa.rutasSimulacion.size(); i++) {
                ArrayList<Integer> auxAI = new ObjectMapper().reader(List.class).readValue(Mapa.rutasSimulacion.get(i).getSeguimiento());
                String codigoPlaca = null;
                ArrayList<String> auxNombreProvincias = new ArrayList<>();
                for(int zz=0; zz < auxAI.size(); zz++){
                    for(int z=0; z<Mapa.oficinas.size(); z++){
                        if(Mapa.oficinas.get(z).getUbigeo() == auxAI.get(zz)){
                            auxNombreProvincias.add(Mapa.oficinas.get(z).getProvincia());
                        }
                    }
                }
                for (int b = 0; b < Mapa.vehiculosSimulacion.size(); b++) {
                    if (Mapa.vehiculosSimulacion.get(b).getId() == Mapa.rutasSimulacion.get(i).getIdUnidadTransporte()) {
                        codigoPlaca = Mapa.vehiculosSimulacion.get(i).getCodigo();
                    }
                }
                ArrayList<PedidoModel> pedidos = new ArrayList<>();
                for (int c = 0; c < Mapa.rutasSimulacion.get(i).getPedidosParciales().size(); c++) {
                    for (int d = 0; d < Mapa.pedidosSimulacion.size(); d++) {
                        if (Mapa.rutasSimulacion.get(i).getPedidosParciales().get(c).getIdPedido() == Mapa.pedidosSimulacion.get(d).getId()) {
                            pedidos.add(Mapa.pedidosSimulacion.get(d));
                        }
                    }
                }
                RutaConArraySegHorasLl auxRutaG = new RutaConArraySegHorasLl((long) i, Mapa.rutasSimulacion.get(i).getIdRuta(), Mapa.rutasSimulacion.get(i).getIdUnidadTransporte(), auxAI, auxNombreProvincias, Mapa.rutasSimulacion.get(i).getHorasDeLlegada(), codigoPlaca, pedidos);
                auxRutasG.add(auxRutaG);
            }
            return auxRutasG;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    @PostMapping("/Ruta/PostRutas")
    public boolean InsertarListaRutas(@RequestBody List<RutaModel> rutasModel) {
        try {
            rutaRepository.saveAll(rutasModel);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return true;
    }

}
