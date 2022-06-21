package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.UnidadTransporteRepository;
import com.pucp.odiparpackappback.models.EstadoUnidadTransporte;
import com.pucp.odiparpackappback.models.Mapa;
import com.pucp.odiparpackappback.models.UnidadTransporteModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
class UnidadTransporteController {

    private final UnidadTransporteRepository unidadTransporteRepository;

    UnidadTransporteController(UnidadTransporteRepository unidadTransporteRepository) {
        this.unidadTransporteRepository = unidadTransporteRepository;
    }

    @GetMapping("/UnidadTransporte/")
    List<UnidadTransporteModel> listarUnidadesTransporte() {
        return (List<UnidadTransporteModel>) unidadTransporteRepository.findAll();
    }

    @GetMapping("/UnidadTransporte/idRuta")
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

    @GetMapping("/UnidadTransporte/coordenadas")
    UnidadTransporteModel actualizarCoordenadas(@RequestBody UnidadTransporteModel vehiculo, @RequestBody long tiempoTranscurido) {
        //Buscar ruta
        for (int i = 0; i < Mapa.rutasSimulacion.size(); i++) {
            if (Mapa.rutasSimulacion.get(i).getIdRuta().equals(vehiculo.getIdRuta())) {
                //Buscar hora llegada
                for (int j = 0; j < Mapa.rutasSimulacion.get(i).getHorasDeLlegada().size(); j++) {
                    if (Mapa.rutasSimulacion.get(i).getHorasDeLlegada().get(j) > Mapa.rutasSimulacion.get(i).getHoraInicio() + tiempoTranscurido) {
                        long tiempo = Mapa.rutasSimulacion.get(i).getHoraInicio() + tiempoTranscurido - Mapa.rutasSimulacion.get(i).getHorasDeLlegada().get(j - 1);
                        double latUltimaOficina = vehiculo.getOrdenada();
                        double lonUltimaOficina = vehiculo.getAbscisa();
                        boolean f1 = false;
                        boolean f2 = false;
                        double latNuevaOficina = -1000;
                        double lonNuevaOficina = -1000;
                        //buscar coordenadas oficinas
                        for (int k = 0; k < Mapa.oficinas.size(); k++) {
                            if (Mapa.oficinas.get(k).getUbigeo() == Mapa.rutasSimulacion.get(i).getTramos().get(j).getIdCiudadJ()) {
                                latNuevaOficina = Mapa.oficinas.get(k).getLatitud();
                                lonNuevaOficina = Mapa.oficinas.get(k).getLongitud();
                                f1 = true;
                            }
                            //si se pasó la primera oficina encontrar en qué oficina está
                            if (j != 0 && Mapa.oficinas.get(k).getUbigeo() == Mapa.rutasSimulacion.get(i).getTramos().get(j - 1).getIdCiudadJ()) {
                                latUltimaOficina = Mapa.oficinas.get(k).getLatitud();
                                lonUltimaOficina = Mapa.oficinas.get(k).getLongitud();
                                f2 = true;
                            }
                            if (f1 == true && (f2 == true || j != 0)) break;
                        }
                        //hallar distancias
                        double lat = latNuevaOficina - latUltimaOficina;
                        double lon = lonNuevaOficina - lonUltimaOficina;
                        //regla de 3 para hallar coordenadas
                        vehiculo.setAbscisa(tiempo * lon / (Mapa.rutasSimulacion.get(i).getTramos().get(j).getTiempoDeViaje() - 3600));
                        vehiculo.setOrdenada(tiempo * lat / (Mapa.rutasSimulacion.get(i).getTramos().get(j).getTiempoDeViaje() - 3600));
                        return vehiculo;
                    }
                }
            }
        }
        return null;
    }

    void listarUnidadesTransporteMantenimiento() {
        Mapa.vehiculosSimulacion = (ArrayList<UnidadTransporteModel>) unidadTransporteRepository.findUnidadTransporteModelByEstadoEquals(EstadoUnidadTransporte.DISPONIBLE);
    }
}