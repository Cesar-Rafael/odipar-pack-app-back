package com.pucp.odiparpackappback.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucp.odiparpackappback.Repositories.RutaRepository;
import com.pucp.odiparpackappback.models.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RutaController {
    private final RutaRepository rutaRepository;
    public RutaController(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @GetMapping("/Ruta/")
    public List<RutaModel> listarRutas(){
        return (List<RutaModel>) rutaRepository.findAll();
    }

    @GetMapping("/ruta/ListarRutasxIdVehiculoDiaDia/{idVehiculo}")
    @ResponseBody
    public List<RutaConArraySegHorasLl> ListarRutasxIdVehiculoDiaDia(@PathVariable("idVehiculo") long idVehiculo) {
        try{
            List<RutaConArraySegHorasLl> auxRutasG = new ArrayList<>();
            List<RutaModel> rutas = rutaRepository.findByIdUnidadTransporte(idVehiculo);
            for(int i=0; i<rutas.size(); i++){
                ArrayList<Integer> auxAI = new ObjectMapper().reader(List.class).readValue(rutas.get(i).getSeguimiento());
                StringBuilder sb = new StringBuilder(rutas.get(i).getArrayHorasLlegada());
                sb.insert(0, '[');
                sb.insert(rutas.get(i).getArrayHorasLlegada().length(), ']');
                ArrayList<Integer> auxAIantesL = new ObjectMapper().reader(List.class).readValue(sb.toString());
                ArrayList<Long> auxAL = new ArrayList<Long>();
                for(int a = 0; a<auxAIantesL.size(); a++ ){
                    auxAL.add(auxAIantesL.get(a).longValue());
                }
                String codigoPlaca = null;
                for(int b = 0; b<Mapa.vehiculos.size(); b++){
                    if(Mapa.vehiculos.get(b).getId() == rutas.get(i).getIdUnidadTransporte()){
                        codigoPlaca = Mapa.vehiculos.get(i).getCodigo();
                    }
                }
                ArrayList<PedidoModel> pedidos = new ArrayList<>();
                for(int c=0; c<Mapa.rutas.get(i).getPedidosParciales().size();c++){
                    for(int d=0; d<Mapa.pedidos.size(); d++){
                        if(Mapa.rutas.get(i).getPedidosParciales().get(c).getIdPedido() == Mapa.pedidos.get(d).getId()){
                            pedidos.add(Mapa.pedidos.get(d));
                        }
                    }
                }
                RutaConArraySegHorasLl auxRutaG = new RutaConArraySegHorasLl(rutas.get(i).getId(), rutas.get(i).getIdRuta(), rutas.get(i).getIdUnidadTransporte(), auxAI, auxAL, codigoPlaca, pedidos);
                auxRutasG.add(auxRutaG);
            }
            return auxRutasG;
        }catch(Exception ex){
            System.out.println(ex);
        }
        return null;
    }

    @GetMapping("/ruta/ListarRutasxIdVehiculoSimulacion/{idVehiculo}")
    @ResponseBody
    public List<RutaConArraySegHorasLl> ListarRutasxIdVehiculoSimulacion(@PathVariable("idVehiculo") long idVehiculo) {
        try{
            List<RutaConArraySegHorasLl> auxRutasG = new ArrayList<>();
            for(int i = 0; i < Mapa.rutas.size(); i++){
                if(Mapa.rutas.get(i).getIdUnidadTransporte() == idVehiculo){
                    ArrayList<Integer> auxAI = new ObjectMapper().reader(List.class).readValue(Mapa.rutas.get(i).getSeguimiento());
                    String codigoPlaca = null;
                    for(int b = 0; b<Mapa.vehiculos.size(); b++){
                        if(Mapa.vehiculos.get(b).getId() == Mapa.rutas.get(i).getIdUnidadTransporte()){
                            codigoPlaca = Mapa.vehiculos.get(i).getCodigo();
                        }
                    }
                    ArrayList<PedidoModel> pedidos = new ArrayList<>();
                    for(int c=0; c<Mapa.rutas.get(i).getPedidosParciales().size();c++){
                        for(int d=0; d<Mapa.pedidos.size(); d++){
                            if(Mapa.rutas.get(i).getPedidosParciales().get(c).getIdPedido() == Mapa.pedidos.get(d).getId()){
                                pedidos.add(Mapa.pedidos.get(d));
                            }
                        }
                    }
                    RutaConArraySegHorasLl auxRutaG = new RutaConArraySegHorasLl(Mapa.rutas.get(i).getId(), Mapa.rutas.get(i).getIdRuta(), Mapa.rutas.get(i).getIdUnidadTransporte(), auxAI, Mapa.rutas.get(i).getHorasDeLlegada(), codigoPlaca, pedidos);
                    auxRutasG.add(auxRutaG);
                }
            }
            return auxRutasG;
        }catch(Exception ex){
            System.out.println(ex);
        }
        return null;
    }

    @PostMapping("/Ruta/PostRutas")
    public boolean InsertarListaRutas(@RequestBody List<RutaModel> rutasModel) {
        try{
            rutaRepository.saveAll(rutasModel);
        }catch(Exception ex){
            System.out.println(ex);
        }
        return true;
    }

}
