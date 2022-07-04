package com.pucp.odiparpackappback.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucp.odiparpackappback.Repositories.RutaRepository;
import com.pucp.odiparpackappback.models.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
public class RutaController {
    private final RutaRepository rutaRepository;

    public RutaController(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @GetMapping("/Ruta/")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    public List<RutaModel> listarRutas() {
        return (List<RutaModel>) rutaRepository.findAll();
    }

    @GetMapping("/ruta/ListarRutasxIdVehiculoDiaDia/{idVehiculo}")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public List<RutaConArraySegHorasLl> ListarRutasxIdVehiculoDiaDia(@PathVariable("idVehiculo") long idVehiculo) {
        try {
            List<RutaConArraySegHorasLl> auxRutasG = new ArrayList<>();
            for (int i = 0; i < Mapa.rutasDiaDia.size(); i++) {
                if (Mapa.rutasDiaDia.get(i).getIdUnidadTransporte() == idVehiculo) {
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
                    for (int b = 0; b < Mapa.vehiculosDiaDia.size(); b++) {
                        if (Mapa.vehiculosDiaDia.get(b).getId() == Mapa.rutasDiaDia.get(i).getIdUnidadTransporte()) {
                            codigoPlaca = Mapa.vehiculosDiaDia.get(b).getCodigo();
                        }
                    }
                    ArrayList<PedidoModel> pedidos = new ArrayList<>();
                    for (int c = 0; c < Mapa.rutasDiaDia.get(i).getPedidosParciales().size(); c++) {
                        for (int d = 0; d < Mapa.pedidosDiaDia.size(); d++) {
                            if (Mapa.rutasDiaDia.get(i).getPedidosParciales().get(c).getIdPedido() == Mapa.pedidosDiaDia.get(d).getId()) {
                                pedidos.add(Mapa.pedidosDiaDia.get(d));
                            }
                        }
                    }
                    RutaConArraySegHorasLl auxRutaG = new RutaConArraySegHorasLl((long) auxRutasG.size(), Mapa.rutasDiaDia.get(i).getIdRuta(), Mapa.rutasDiaDia.get(i).getIdUnidadTransporte(), auxAI, auxNombreProvincias, Mapa.rutasDiaDia.get(i).getHorasDeLlegada(), codigoPlaca, pedidos);
                    auxRutasG.add(auxRutaG);
                }
            }
            return auxRutasG;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    @GetMapping("/ruta/ListarRutasxIdVehiculoSimulacion/{idVehiculo}")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public List<RutaConArraySegHorasLl> ListarRutasxIdVehiculoSimulacion(@PathVariable("idVehiculo") long idVehiculo) {
        try {
            List<RutaConArraySegHorasLl> auxRutasG = new ArrayList<>();
            for (int i = 0; i < Mapa.rutasSimulacion.size(); i++) {
                if (Mapa.rutasSimulacion.get(i).getIdUnidadTransporte() == idVehiculo) {
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
                    for (int c = 0; c < Mapa.rutasSimulacion.get(i).getPedidosParciales().size(); c++) {
                        for (int d = 0; d < Mapa.pedidosSimulacion.size(); d++) {
                            if (Mapa.rutasSimulacion.get(i).getPedidosParciales().get(c).getIdPedido() == Mapa.pedidosSimulacion.get(d).getId()) {
                                pedidos.add(Mapa.pedidosSimulacion.get(d));
                            }
                        }
                    }
                    RutaConArraySegHorasLl auxRutaG = new RutaConArraySegHorasLl((long) auxRutasG.size(), Mapa.rutasSimulacion.get(i).getIdRuta(), Mapa.rutasSimulacion.get(i).getIdUnidadTransporte(), auxAI, auxNombreProvincias, Mapa.rutasSimulacion.get(i).getHorasDeLlegada(), codigoPlaca, pedidos);
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
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    public List<RutaConArraySegHorasLl> ListarRutasSimulacion() {
        try {
            List<OficinaModel> oficinasT = Mapa.oficinas;
            HashMap<Integer, String> oficinas = new HashMap<>();
            for (int i = 0; i < oficinasT.size(); i++) {
                oficinas.put(oficinasT.get(i).getUbigeo(), oficinasT.get(i).getProvincia());
            }
            List<RutaConArraySegHorasLl> auxRutasG = new ArrayList<>();
            for (int i = 0; i < Mapa.rutasSimulacion.size(); i++) {
                ArrayList<Integer> auxAI = new ObjectMapper().reader(List.class).readValue(Mapa.rutasSimulacion.get(i).getSeguimiento());
                String codigoPlaca = null;
                ArrayList<String> auxNombreProvincias = new ArrayList<>();
                for (int zz = 0; zz < auxAI.size(); zz++) {
                    for (int z = 0; z < Mapa.oficinas.size(); z++) {
                        if (Mapa.oficinas.get(z).getUbigeo() == auxAI.get(zz)) {
                            auxNombreProvincias.add(zz, Mapa.oficinas.get(z).getProvincia());
                        }
                    }
                }
                for (int b = 0; b < Mapa.vehiculosSimulacion.size(); b++) {
                    if (Mapa.vehiculosSimulacion.get(b).getId() == Mapa.rutasSimulacion.get(i).getIdUnidadTransporte()) {
                        codigoPlaca = Mapa.vehiculosSimulacion.get(b).getCodigo();
                    }
                }
                ArrayList<PedidoModel> pedidos = new ArrayList<>();
                for (int c = 0; c < Mapa.rutasSimulacion.get(i).getPedidosParciales().size(); c++) {
                    for (int d = 0; d < Mapa.pedidosSimulacion.size(); d++) {
                        if (Mapa.rutasSimulacion.get(i).getPedidosParciales().get(c).getIdPedido() == Mapa.pedidosSimulacion.get(d).getId()) {
                            Mapa.pedidosSimulacion.get(d).setCiudadDestino(oficinas.get(Mapa.pedidosSimulacion.get(d).getIdCiudadDestino()));
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

    @GetMapping("/ruta/DiaDia/listar")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    public List<RutaConArraySegHorasLl> ListarRutasDiaDia() {
        try {
            List<OficinaModel> oficinasT = Mapa.oficinas;
            HashMap<Integer, String> oficinas = new HashMap<>();
            for (int i = 0; i < oficinasT.size(); i++) {
                oficinas.put(oficinasT.get(i).getUbigeo(), oficinasT.get(i).getProvincia());
            }
            List<RutaConArraySegHorasLl> auxRutasG = new ArrayList<>();
            for (int i = 0; i < Mapa.rutasDiaDia.size(); i++) {
                ArrayList<Integer> auxAI = new ObjectMapper().reader(List.class).readValue(Mapa.rutasDiaDia.get(i).getSeguimiento());
                String codigoPlaca = null;
                ArrayList<String> auxNombreProvincias = new ArrayList<>();
                for (int zz = 0; zz < auxAI.size(); zz++) {
                    for (int z = 0; z < Mapa.oficinas.size(); z++) {
                        if (Mapa.oficinas.get(z).getUbigeo() == auxAI.get(zz)) {
                            auxNombreProvincias.add(zz, Mapa.oficinas.get(z).getProvincia());
                        }
                    }
                }
                for (int b = 0; b < Mapa.vehiculosDiaDia.size(); b++) {
                    if (Mapa.vehiculosDiaDia.get(b).getId() == Mapa.rutasDiaDia.get(i).getIdUnidadTransporte()) {
                        codigoPlaca = Mapa.vehiculosDiaDia.get(b).getCodigo();
                    }
                }
                ArrayList<PedidoModel> pedidos = new ArrayList<>();
                for (int c = 0; c < Mapa.rutasDiaDia.get(i).getPedidosParciales().size(); c++) {
                    for (int d = 0; d < Mapa.pedidosDiaDia.size(); d++) {
                        if (Mapa.rutasDiaDia.get(i).getPedidosParciales().get(c).getIdPedido() == Mapa.pedidosDiaDia.get(d).getId()) {
                            Mapa.pedidosDiaDia.get(d).setCiudadDestino(oficinas.get(Mapa.pedidosDiaDia.get(d).getIdCiudadDestino()));
                            pedidos.add(Mapa.pedidosDiaDia.get(d));
                        }
                    }
                }
                RutaConArraySegHorasLl auxRutaG = new RutaConArraySegHorasLl((long) i, Mapa.rutasDiaDia.get(i).getIdRuta(), Mapa.rutasDiaDia.get(i).getIdUnidadTransporte(), auxAI, auxNombreProvincias, Mapa.rutasDiaDia.get(i).getHorasDeLlegada(), codigoPlaca, pedidos);
                auxRutasG.add(auxRutaG);
            }
            return auxRutasG;
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    @PostMapping("/Ruta/PostRutas")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    public boolean InsertarListaRutas(@RequestBody List<RutaModel> rutasModel) {
        try {
            rutaRepository.saveAll(rutasModel);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return true;
    }

}
