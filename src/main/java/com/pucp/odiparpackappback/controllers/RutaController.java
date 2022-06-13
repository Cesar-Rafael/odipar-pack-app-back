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

    @PostMapping("/Ruta/PostRutas")
    public boolean InsertarListaRutas(@RequestBody List<RutaModel> rutasModel) {
        try{
            rutaRepository.saveAll(rutasModel);
        }catch(Exception ex){
            System.out.println(ex);
        }
        return true;
    }

    @PostMapping("/Ruta/UnidadTransporte/{id}")
    @ResponseBody
    public String listarRutasxIdUT(@PathVariable("id") long id){
        List<RutaModel> aux = (List<RutaModel>) rutaRepository.findAll();
        for(int i=0; i<aux.size();i++){
            if(aux.get(i).getIdUnidadTransporte() == id){
                return aux.get(i).getSeguimiento();
            }
        }
    }
}
