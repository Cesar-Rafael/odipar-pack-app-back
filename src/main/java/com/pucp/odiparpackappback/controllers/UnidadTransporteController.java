package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.UnidadTransporteRepository;
import com.pucp.odiparpackappback.models.Mapa;
import com.pucp.odiparpackappback.models.UnidadTransporteModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
class UnidadTransporteController {

    private final UnidadTransporteRepository unidadTransporteRepository;

    UnidadTransporteController(UnidadTransporteRepository unidadTransporteRepository) {
        this.unidadTransporteRepository = unidadTransporteRepository;
    }

    @GetMapping("/UnidadTransporte/Listar/Operaciones")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    List<UnidadTransporteModel> listarUnidadesTransporte() {
        return (List<UnidadTransporteModel>) unidadTransporteRepository.findAll();
    }

    @GetMapping("/UnidadTransporte/Obtener/{id}")
    @CrossOrigin
    Optional<UnidadTransporteModel> listarUnidadTransporte(@PathVariable("id") long id) {
        return unidadTransporteRepository.findById(id);
    }

    @GetMapping("/UnidadTransporte/Listar/Simulacion")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    List<UnidadTransporteModel> listarUnidadesTransporteSimulacion() {
        Mapa.cargarVehiculosSimulacion("src/main/resources/static/unidad_transporte_model.csv");
        return Mapa.vehiculosSimulacion;
    }

    @GetMapping("/UnidadTransporte/idRuta")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    List<UnidadTransporteModel> listarUnidadesTransportexIdRuta(@RequestParam(required = false) int idRuta) {
        List<UnidadTransporteModel> unidadesTransporteAux = (List<UnidadTransporteModel>) unidadTransporteRepository.findAll();
        List<UnidadTransporteModel> unidadesTransporte = new ArrayList<>();
        for (int i = 0; i < unidadesTransporteAux.size(); i++) {
            if (unidadesTransporteAux.get(i).getIdRuta() == idRuta) {
                unidadesTransporte.add(unidadesTransporteAux.get(i));
            }
        }
        return unidadesTransporte;
    }

    @GetMapping("/UnidadTransporte/codigo")
    @CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
    List<UnidadTransporteModel> listarUnidadesTransportexCodigo(@RequestParam(required = false) String codigo) {
        List<UnidadTransporteModel> unidadesTransporteAux = (List<UnidadTransporteModel>) unidadTransporteRepository.findAll();
        List<UnidadTransporteModel> unidadesTransporte = new ArrayList<>();
        for (int i = 0; i < unidadesTransporteAux.size(); i++) {
            if (Objects.equals(unidadesTransporteAux.get(i).getCodigo(), codigo)) {
                unidadesTransporte.add(unidadesTransporteAux.get(i));
            }
        }
        return unidadesTransporte;
    }
}