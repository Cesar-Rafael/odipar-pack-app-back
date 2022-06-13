package com.pucp.odiparpackappback.models;

import com.pucp.odiparpackappback.Repositories.*;
import com.pucp.odiparpackappback.services.utils.DatosUtil;
import com.pucp.odiparpackappback.topKshortestpaths.utils.Pair;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
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
    public static double k = 0.0;

    public static LocalDateTime inicioSimulacion = LocalDateTime.of(2022, 1, 1, 0, 50, 0);
    public static LocalDateTime finSimulacion = inicioSimulacion.plusMinutes(90);

    public static double getFitnessSolucion() {
        return fitnessSolucion;
    }

    public static void setFitnessSolucion(double fitnessSolucion) {
        Mapa.fitnessSolucion = fitnessSolucion;
    }

    public static double fitnessSolucion = 0;

    public static ArrayList<Pair<Integer, Integer>> bloqueos = new ArrayList<>();

    private static PedidoRepository pedidoRepository;
    private static OficinaRepository oficinaRepository;
    private static TramoRepository tramoRepository;
    private static UnidadTransporteRepository unidadTransporteRepository;
    private static RutaRepository rutaRepository;

    private static BloqueoRepository bloqueoRepository;

    public Mapa(PedidoRepository pedidoRepository, OficinaRepository oficinaRepository, TramoRepository tramoRepository, UnidadTransporteRepository unidadTransporteRepository, BloqueoRepository bloqueoRepository, RutaRepository rutaRepository) {
        Mapa.pedidoRepository = pedidoRepository;
        Mapa.oficinaRepository = oficinaRepository;
        Mapa.tramoRepository = tramoRepository;
        Mapa.unidadTransporteRepository = unidadTransporteRepository;
        Mapa.bloqueoRepository = bloqueoRepository;
        Mapa.rutaRepository = rutaRepository;
    }

    public static void cargarOficinasDiaDia() {
        oficinas = (ArrayList<OficinaModel>) oficinaRepository.findAll();
        ArrayList<OficinaModel> oficinasPrincipalesAux = new ArrayList<>();
        oficinasPrincipalesAux = (ArrayList<OficinaModel>) oficinaRepository.findAll();
        for(int i = 0; i < oficinasPrincipalesAux.size(); i++){
            if(oficinasPrincipalesAux.get(i).getEsPrincipal()){
                oficinasPrincipales.add(oficinasPrincipalesAux.get(i));
            }
        }
    }

    public static void cargarOficinasSimulacion(String rutaOficinas) {
        File archivoPedidos;
        FileReader fr = null;
        BufferedReader br;
        try {
            archivoPedidos = new File(rutaOficinas);
            fr = new FileReader(archivoPedidos);
            br = new BufferedReader(fr);
            //Lectura de fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                // String
                String[] parts = linea.split(";");
                // Obtengo atributos
                int ubigeo = Integer.parseInt(parts[0]);
                String departamento = parts[1];

                boolean esPrincipal;
                if (Integer.parseInt(parts[2]) == 0) {
                    esPrincipal = false;
                }
                else{
                    esPrincipal = true;
                }
                parts[3] = parts[3].replace(',', '.');
                parts[3] = parts[3].replace("\"","");
                parts[4] = parts[4].replace(',', '.');
                parts[4] = parts[4].replace("\"","");
                double latitud = Double.parseDouble(parts[3]);
                double longitud = Double.parseDouble(parts[4]);
                String provincia = parts[5];
                Region region = Region.get(Integer.parseInt(parts[6]));
                //Agrega Oficina
                OficinaModel oficina = new OficinaModel(ubigeo, departamento, provincia, latitud, longitud, region, esPrincipal);
                oficinas.add(oficina);
            }
            // Oficinas Principales
            for(int i = 0; i < oficinas.size(); i++){
                if(oficinas.get(i).getEsPrincipal()){
                    oficinasPrincipales.add(oficinas.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void cargarTramosSimulacion(String rutaTramos) {
        File archivoPedidos;
        FileReader fr = null;
        BufferedReader br;
        try {
            archivoPedidos = new File(rutaTramos);
            fr = new FileReader(archivoPedidos);
            br = new BufferedReader(fr);
            //Lectura de fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                // String
                String[] parts = linea.split(";");
                // Obtengo atributos
                Long id = Long.valueOf(parts[0]);
                boolean bloqueado;
                if(Integer.parseInt(parts[1]) == 0){
                    bloqueado = false;
                }
                else{
                    bloqueado = true;
                }
                int idCiudadI = Integer.parseInt(parts[2]);
                int idCiudadJ = Integer.parseInt(parts[3]);
                parts[4] = parts[4].replace(',', '.');
                parts[4] = parts[4].replace(',', '.');
                double tiempoDeViaje = Double.parseDouble(parts[4]);
                //Agrega Ruta
                TramoModel tramo = new TramoModel(id, idCiudadI, idCiudadJ, tiempoDeViaje, bloqueado);
                tramos.add(tramo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void cargarVehiculosSimulacion(String rutaVehiculos) {
        File archivoPedidos;
        FileReader fr = null;
        BufferedReader br;
        try {
            archivoPedidos = new File(rutaVehiculos);
            fr = new FileReader(archivoPedidos);
            br = new BufferedReader(fr);
            //Lectura de fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                // String
                String[] parts = linea.split(";");
                // Obtengo atributos
                Long id = Long.valueOf(parts[0]);
                parts[1] = parts[1].replace(',', '.');
                parts[1] = parts[1].replace(',', '.');
                double abscisa = Double.parseDouble(parts[1]);
                int capacidadDisponible = Integer.parseInt(parts[2]);
                int capacidadTotal = Integer.parseInt(parts[3]);
                String codigo = parts[4];
                EstadoUnidadTransporte estado = EstadoUnidadTransporte.get(Integer.parseInt(parts[5]));
                Long idRuta = Long.valueOf(parts[6]);
                int oficinaActual = Integer.parseInt(parts[7]);
                parts[8] = parts[8].replace(',', '.');
                double ordenada = Double.parseDouble(parts[8]);
                //Agrega Vehiculo
                UnidadTransporteModel vehiculo = new UnidadTransporteModel(id, codigo, capacidadTotal, estado, oficinaActual, abscisa, ordenada);
                vehiculos.add(vehiculo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void cargarTramosDiaDia() {
        tramos = (ArrayList<TramoModel>) tramoRepository.findAll();
    }
    public static void cargarVehiculosDiaDia() {
        vehiculos = (ArrayList<UnidadTransporteModel>) unidadTransporteRepository.findAll();
    }
    public static void cargarPedidosDiaDia(Date fechaInicio, Date fechaFin){
        pedidos = (ArrayList<PedidoModel>) pedidoRepository.findPedidoModelByFechaHoraCreacionBetween(fechaInicio, fechaFin);
    }

    public static void cargarPedidosSimulacion(String rutaArchivo, Date fechaInicio, Date fechaFin){
        File archivoPedidos;
        FileReader fr = null;
        BufferedReader br;

        try {
            archivoPedidos = new File(rutaArchivo);
            fr = new FileReader(archivoPedidos);
            br = new BufferedReader(fr);

            //Lectura de fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                // String
                String[] parts = linea.split(";");
                // Obtengo atributos
                long id = Long.parseLong(parts[0]);
                int cantPaquetes = Integer.parseInt(parts[1]);
                String ciudadDestino = parts[2];
                Date fechaHoraCreacion = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.ssssss").parse(parts[3]);
                int idCiudadDestino = Integer.parseInt(parts[4]);
                long rucCliente = Long.parseLong(parts[5]);
                //Agrega Pedido
                if(fechaHoraCreacion.after(fechaInicio) && fechaHoraCreacion.before(fechaFin)){
                    PedidoModel pedido = new PedidoModel(id, rucCliente, cantPaquetes, idCiudadDestino, ciudadDestino, fechaHoraCreacion);
                    pedidos.add(pedido);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static List<BloqueoModel> obtenerTramosBloqueadosDiaDia(int oficinaI, int oficinaJ, Date fechaInicio, Date fechaFin) {
        return bloqueoRepository.findBloqueoModelByUbigeoInicioAndUbigeoFin(oficinaI, oficinaJ, fechaInicio, fechaFin);
    }

    public static List<BloqueoModel> obtenerTramosBloqueadosSimulacion(int oficinaI, int oficinaJ, Date fechaInicio, Date fechaFin, String rutaArchivo) {
        File archivoPedidos;
        FileReader fr = null;
        BufferedReader br;
        List<BloqueoModel> listaBloqueos = new ArrayList<>();
        try {
            archivoPedidos = new File(rutaArchivo);
            fr = new FileReader(archivoPedidos);
            br = new BufferedReader(fr);

            //Lectura de fichero
            String linea;
            while ((linea = br.readLine()) != null) {
                // String
                String[] parts = linea.split(";");
                // Obtengo atributos
                Long id = Long.valueOf(parts[0]);
                Date fechaFinAux = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.ssssss").parse(parts[1]);
                Date fechaInicioAux = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.ssssss").parse(parts[2]);
                int ubigeoFin = Integer.parseInt(parts[3]);
                int ubigeoInicio = Integer.parseInt(parts[4]);
                //Agrega Bloqueo
                if(fechaInicioAux.after(fechaInicio) && fechaFinAux.before(fechaFin) && (((oficinaI == ubigeoInicio) && (oficinaJ == ubigeoFin)) || ((oficinaI == ubigeoFin) && (oficinaJ == ubigeoInicio)))){
                    BloqueoModel bloqueo = new BloqueoModel(id, ubigeoInicio, ubigeoFin, fechaInicioAux, fechaFinAux);
                    listaBloqueos.add(bloqueo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return listaBloqueos;
    }

    public static ArrayList<TramoModel> listarTramos(String tramos){
        return DatosUtil.listarTramos(tramos);
    }

    public static void cargarDiccionarioTramos() {
        dicTramos = DatosUtil.crearMapa();
    }

    public static List<RutaModel> cargarRutas(ArrayList<RutaModel> rutasAux){
        return (List<RutaModel>) rutaRepository.saveAll(rutasAux);
    }
}
