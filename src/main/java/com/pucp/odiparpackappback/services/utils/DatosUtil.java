package com.pucp.odiparpackappback.services.utils;

import com.pucp.odiparpackappback.models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class DatosUtil {
    // VELOCIDADES POR REGION
    private static final double[][] velocidades = {{70.0, 50.0, 0.0}, {50.0, 60.0, 55.0}, {0.0, 55.0, 65.0}}; // COSTA, SIERRA, SELVA - COSTA, SIERRA, SELVA
    private static final double RADIO_TIERRA = 6371; // km

    public static ArrayList<TramoModel> listarTramos(String tramos) {
        ArrayList<TramoModel> listaTramos = new ArrayList<>();
        //[170582, 896574, 014536]
        String[] parts = tramos.split(",");
        int ciudadIni = Integer.parseInt(parts[0].substring(1));
        int ciudadFin;
        Long id = Long.valueOf(0);
        for (int i = 1; i < parts.length; i++) {
            ciudadFin = Integer.parseInt(parts[i].replaceAll("[^0-9]", ""));
            TramoModel tramo = new TramoModel(id, ciudadIni, ciudadFin, 0, false);
            listaTramos.add(tramo);

            ciudadIni = ciudadFin;
            id++;
            //System.out.println(tramo.toString());
        }
        return listaTramos;
    }

    // Lee Pedidos desde la BD
    public static ArrayList<PedidoModel> leerArchivoPedidos(String rutaArchivo) {
        ArrayList<PedidoModel> pedidos = new ArrayList<>();
        return pedidos;
    }

    // Lee Oficinas desde la BD
    public static ArrayList<OficinaModel> leerArchivoRutas(String rutaArchivo, ArrayList<OficinaModel> oficinasPrincipales) {
        ArrayList<OficinaModel> oficinas = new ArrayList<>();
        return oficinas;
    }

    // Lee Vehiculos desde la BD
    public static ArrayList<UnidadTransporteModel> leerArchivoVehiculos(String archivo) {
        ArrayList<UnidadTransporteModel> vehiculos = new ArrayList<>();
        return vehiculos;
    }

    public static int obtenerUbigeoCiudad(String ciudad) {
        for (OficinaModel o : Mapa.oficinasPrincipales) {
            if (o.getProvincia().equals(ciudad)) return o.getUbigeo();
        }
        return -1;
    }

    public static DicTramos crearMapa() {
        ArrayList<TramoModel> tramos = Mapa.tramos;
        //agregar a dic
        DicTramos dicTramos = new DicTramos();
        Map<Integer, Integer[]> miMapa = dicTramos.getMap();
        tramos.sort(Comparator.comparing(TramoModel::getIdCiudadI));
        int id = 0;
        ArrayList<Integer> aux = new ArrayList<>();
        Integer[] a;
        for (TramoModel t : tramos) {
            if (id == t.getIdCiudadI()) {
                aux.add(t.getIdCiudadJ());
            } else {
                if (id != 0) {
                    a = new Integer[aux.size()];
                    a = aux.toArray(a);
                    System.out.print("[" + id + "]");
                    for (Integer x : a)
                        System.out.print(x + " ");
                    System.out.print('\n');
                    miMapa.put(id, a);
                    dicTramos.putMap(miMapa);
                }
                id = t.getIdCiudadI();
                aux.clear();
                aux.add(t.getIdCiudadJ());
            }
        }

        return dicTramos;
    }

    public static OficinaModel buscarOficina(int ubigeoOficina) {
        for (OficinaModel o : Mapa.oficinas) {
            if (o.getUbigeo() == ubigeoOficina) return o;
        }
        return null;
    }

    public static double gradosAradianes(double grados) {
        double PI = Math.PI;
        return grados * PI / 180;
    }

    public static double haversineFormula(OficinaModel oficina1, OficinaModel oficina2) {
        double lat1 = gradosAradianes(oficina1.getLatitud()), lon1 = gradosAradianes(oficina1.getLongitud());
        double lat2 = gradosAradianes(oficina2.getLatitud()), lon2 = gradosAradianes(oficina2.getLongitud());

        double diferenciaEntreLongitudes = lon2 - lon1;
        double diferenciaEntreLatitudes = lat2 - lat1;
        double a = Math.pow(Math.sin(diferenciaEntreLatitudes / 2.0), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(diferenciaEntreLongitudes / 2.0), 2);
        return RADIO_TIERRA * 2 * Math.atan(Math.sqrt(a) / Math.sqrt(1 - a));
    }

    public static double calcularTiempoDeViaje(OficinaModel oficina1, OficinaModel oficina2, int[][] vel) {
        double distancia = haversineFormula(oficina1, oficina2);
        double velocidad = 60;
        for (int[] i : vel) {
            if ((oficina1.getRegion().getCode() == i[0] && oficina2.getRegion().getCode() == i[1]) || (oficina1.getRegion().getCode() == i[1] && oficina2.getRegion().getCode() == i[0])) {
                velocidad = i[2];
                break;
            }
        }
        //System.out.println(velocidad + " =>" + oficina1.getRegion().getCode() + " =>" + oficina2.getRegion().getCode());
        return distancia / velocidad;
    }


    public static ArrayList<TramoModel> leerArchivoTramos(String archivoTramo, String archivoVelocidades) {
        ArrayList<TramoModel> tramos = new ArrayList<>();
        int[][] velocidades = new int[5][3];
        //archivo tramos
        File archivoRutas;
        FileReader fr = null;
        BufferedReader br;
        //archivo velocidades
        File archivoVel;
        FileReader fr2 = null;
        BufferedReader br2;

        try {
            //archivo tramos
            archivoRutas = new File(archivoTramo);
            fr = new FileReader(archivoRutas);
            br = new BufferedReader(fr);
            //archivo velocidades
            archivoVel = new File(archivoVelocidades);
            fr2 = new FileReader(archivoVel);
            br2 = new BufferedReader(fr2);

            //Lectura de velocidades
            String linea;

            int i = 0;
            while ((linea = br2.readLine()) != null) {
                int[] dato = new int[3];
                //Obtengo atributos
                String[] parts = linea.split(" ");
                if (parts[0].equals("Costa")) {
                    dato[0] = 0;
                } else if (parts[0].equals("Sierra")) {
                    dato[0] = 1;
                } else {
                    dato[0] = 2;
                }
                if (parts[2].equals("Costa")) {
                    dato[1] = 0;
                } else if (parts[2].equals("Sierra")) {
                    dato[1] = 1;
                } else {
                    dato[1] = 2;
                }
                dato[2] = Integer.parseInt(parts[4]);
                //System.out.println(i + " " + dato[0] + " => " + dato[1] + "=>" + dato[2]);

                velocidades[i] = dato;
                i++;

            }
            //Lectura de Tramos
            Long id = Long.valueOf(1);
            while ((linea = br.readLine()) != null) {

                //Obtengo atributos
                String[] parts = linea.split(" ");
                int ciudad1 = Integer.parseInt(parts[0]);
                int ciudad2 = Integer.parseInt(parts[2]);

                OficinaModel oficina1 = buscarOficina(ciudad1);
                OficinaModel oficina2 = buscarOficina(ciudad2);

                double tiempoDeViaje = calcularTiempoDeViaje(oficina1, oficina2, velocidades);

                //Agrega tramo
                TramoModel tramo = new TramoModel(id, ciudad1, ciudad2, tiempoDeViaje, false);
                tramos.add(tramo);
                id++;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
                if (null != fr2) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }

        return tramos;
    }

    public static double calcularTiempoViajeEntreTramos(int ubigeoI, int ubigeoJ) {
        double tiempo = 0.0;
        for (TramoModel t : Mapa.tramos) {
            if (t.getIdCiudadI() == ubigeoI && t.getIdCiudadJ() == ubigeoJ) {
                tiempo = t.getTiempoDeViaje();
                break;
            }
        }
        return tiempo;
    }
}
