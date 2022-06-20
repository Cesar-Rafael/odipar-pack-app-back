package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.RutaRepository;
import com.pucp.odiparpackappback.models.PedidoModel;
import com.pucp.odiparpackappback.models.RutaModel;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/ruta/{idVehicule}")
    @ResponseBody
    RutaModel ListarPedidoxId(@PathVariable("idVehicule") long idVehicule) {
        RutaModel ruta = rutaRepository.findByIdUnidadTransporte(idVehicule);
        if (ruta != null) {
            return ruta;
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

    @GetMapping("/Ruta/UnidadTransporte/{id}")
    @ResponseBody
    public String listarRutasxIdUT(@PathVariable("id") long id){
        RutaModel aux = rutaRepository.findByIdUnidadTransporte(id);
        return aux.getSeguimiento();
    }


}
