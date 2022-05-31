package com.pucp.odiparpackappback.models;

import com.pucp.odiparpackappback.services.utils.DatosUtil;

import java.util.ArrayList;

public class Mapa {
    public static ArrayList<OficinaModel> oficinasPrincipales = new ArrayList<>();
    public static ArrayList<OficinaModel> oficinas;
    public static ArrayList<TramoModel> tramos;
    public static ArrayList<UnidadTransporteModel> vehiculos;
    public static DicTramos dicTramos;
    public static ArrayList<PedidoModel> pedidos;
    public static ArrayList<Ruta> rutas = new ArrayList<>();
    public static boolean flag = true;

    public static double getFitnessSolucion() {
        return fitnessSolucion;
    }

    public static void setFitnessSolucion(double fitnessSolucion) {
        Mapa.fitnessSolucion = fitnessSolucion;
    }

    public static double fitnessSolucion = 0;

    public static void cargarOficinas(String nombreArchivo) {
        oficinas = DatosUtil.leerArchivoRutas(nombreArchivo, oficinasPrincipales);
    }

    public static void cargarTramos(String nombreArchivoTramos, String nombreArchivoVel) {
        tramos = DatosUtil.leerArchivoTramos(nombreArchivoTramos, nombreArchivoVel);
    }

    public static void cargarVehiculos(String nombreArchivo) {
        vehiculos = DatosUtil.leerArchivoVehiculos(nombreArchivo);
    }

    public static void cargarPedidos(String nombreArchivo){
        pedidos = DatosUtil.leerArchivoPedidos(nombreArchivo);
    }

    public static ArrayList<TramoModel> listarTramos(String tramos){
        return DatosUtil.listarTramos(tramos);
    }

    public static void cargarDiccionarioTramos() {
        dicTramos = DatosUtil.crearMapa();
    }
}
