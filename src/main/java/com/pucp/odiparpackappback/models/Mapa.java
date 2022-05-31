package com.pucp.odiparpackappback.models;

import com.pucp.odiparpackappback.Repositories.OficinaRepository;
import com.pucp.odiparpackappback.Repositories.PedidoRepository;
import com.pucp.odiparpackappback.Repositories.TramoRepository;
import com.pucp.odiparpackappback.Repositories.UnidadTransporteRepository;
import com.pucp.odiparpackappback.services.utils.DatosUtil;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

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

    public Mapa(PedidoRepository pedidoRepository, OficinaRepository oficinaRepository, TramoRepository tramoRepository, UnidadTransporteRepository unidadTransporteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.oficinaRepository = oficinaRepository;
        this.tramoRepository = tramoRepository;
        this.unidadTransporteRepository = unidadTransporteRepository;
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
    public static void cargarPedidos(){
        pedidos = (ArrayList<PedidoModel>) pedidoRepository.findAll();
    }

    public static ArrayList<TramoModel> listarTramos(String tramos){
        return DatosUtil.listarTramos(tramos);
    }

    public static void cargarDiccionarioTramos() {
        dicTramos = DatosUtil.crearMapa();
    }
}
