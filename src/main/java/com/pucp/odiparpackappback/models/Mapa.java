package com.pucp.odiparpackappback.models;

import com.pucp.odiparpackappback.Repositories.*;
import com.pucp.odiparpackappback.services.utils.DatosUtil;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class Mapa {
    public static ArrayList<OficinaModel> oficinasPrincipales = new ArrayList<>();
    public static ArrayList<OficinaModel> oficinas = new ArrayList<>();
    public static ArrayList<TramoModel> tramos = new ArrayList<>();
    public static ArrayList<UnidadTransporteModel> vehiculos = new ArrayList<>();
    public static DicTramos dicTramos;
    public static ArrayList<PedidoModel> pedidos = new ArrayList<>();
    public static ArrayList<Ruta> rutas = new ArrayList<>();
    public static boolean flag = true;

    public static LocalDateTime inicioSimulacion = LocalDateTime.of(2022, 1, 1, 0, 50, 0);
    public static LocalDateTime finSimulacion = inicioSimulacion.plusMinutes(90);

    public static double getFitnessSolucion() {
        return fitnessSolucion;
    }

    public static void setFitnessSolucion(double fitnessSolucion) {
        Mapa.fitnessSolucion = fitnessSolucion;
    }

    public static double fitnessSolucion = 0;


    private static PedidoRepository pedidoRepository;
    private static OficinaRepository oficinaRepository;
    private static TramoRepository tramoRepository;
    private static UnidadTransporteRepository unidadTransporteRepository;

    private static BloqueoRepository bloqueoRepository;

    public Mapa(PedidoRepository pedidoRepository, OficinaRepository oficinaRepository, TramoRepository tramoRepository, UnidadTransporteRepository unidadTransporteRepository, BloqueoRepository bloqueoRepository) {
        Mapa.pedidoRepository = pedidoRepository;
        Mapa.oficinaRepository = oficinaRepository;
        Mapa.tramoRepository = tramoRepository;
        Mapa.unidadTransporteRepository = unidadTransporteRepository;
        Mapa.bloqueoRepository = bloqueoRepository;
    }

    public static void cargarOficinas() {
        oficinas = (ArrayList<OficinaModel>) oficinaRepository.findAll();

        ArrayList<OficinaModel> oficinasPrincipalesAux = new ArrayList<>();
        oficinasPrincipalesAux = (ArrayList<OficinaModel>) oficinaRepository.findAll();

        for(int i = 0; i < oficinasPrincipalesAux.size(); i++){
            if(oficinasPrincipalesAux.get(i).isEsPrincipal()){
                oficinasPrincipales.add(oficinasPrincipalesAux.get(i));
            }
        }
    }



    public static void cargarTramos() {
        tramos = (ArrayList<TramoModel>) tramoRepository.findAll();
    }
    public static void cargarVehiculos() {
        vehiculos = (ArrayList<UnidadTransporteModel>) unidadTransporteRepository.findAll();
    }
    public static void cargarPedidos(Date fechaInicio, Date fechaFin){
        pedidos = (ArrayList<PedidoModel>) pedidoRepository.findPedidoModelByFechaHoraCreacionBetween(fechaInicio, fechaFin);
    }

    public static List<BloqueoModel> obtenerTramosBloqueados(int oficinaI, int oficinaJ, Date fechaInicio, Date fechaFin) {
        return bloqueoRepository.findBloqueoModelByUbigeoInicioAndUbigeoFin(oficinaI, oficinaJ, fechaInicio, fechaFin);
    }

    public static ArrayList<TramoModel> listarTramos(String tramos){
        return DatosUtil.listarTramos(tramos);
    }

    public static void cargarDiccionarioTramos() {
        dicTramos = DatosUtil.crearMapa();
    }
}
