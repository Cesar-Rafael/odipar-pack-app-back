package com.pucp.odiparpackappback.services;

import com.pucp.odiparpackappback.models.EstadoUnidadTransporte;
import com.pucp.odiparpackappback.models.OficinaModel;
import com.pucp.odiparpackappback.models.Region;
import com.pucp.odiparpackappback.models.UnidadTransporteModel;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class UnidadTransporteService {
        public List<UnidadTransporteModel> getUnidadesTransporte(){
                //return List.of(
                //        new UnidadTransporteModel(0,"20152948", 0, EstadoUnidadTransporte.DISPONIBLE, 6666)
                //);

                ArrayList<UnidadTransporteModel> vehiculos = new ArrayList<>();

                File archivoRutas;
                FileReader fr = null;
                BufferedReader br;

                try {
                        archivoRutas = new File("src/main/resources/static/unidadesTransporte.csv");
                        fr = new FileReader(archivoRutas);
                        br = new BufferedReader(fr);

                        //Lectura de fichero
                        String linea;
                        int id = 0;
                        while ((linea = br.readLine()) != null) {

                                //Obtengo atributos
                                String[] parts = linea.split(";");
                                String codigo = parts[0];
                                int capacidad = Integer.parseInt(parts[1]);
                                String ciudad = parts[2];
                                int ubigeo = obtenerUbigeoCiudad(ciudad);

                                //Agrega vehículos
                                UnidadTransporteModel vehiculo = new UnidadTransporteModel(id++, codigo, capacidad, EstadoUnidadTransporte.DISPONIBLE, ubigeo);
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

                return vehiculos;
        }

        public static int obtenerUbigeoCiudad(String ciudad) {
                ArrayList<OficinaModel> oficinasPrincipales = new ArrayList<>();
                // Inicio código importado
                ArrayList<OficinaModel> oficinas = new ArrayList<>();

                ArrayList<String> nombresOficinasPrincipales = new ArrayList<>();
                nombresOficinasPrincipales.add("LIMA");
                nombresOficinasPrincipales.add("AREQUIPA");
                nombresOficinasPrincipales.add("TRUJILLO");

                File archivoRutas;
                FileReader fr = null;
                BufferedReader br;

                try {
                        archivoRutas = new File("src/main/resources/static/inf226.oficinas.txt");
                        fr = new FileReader(archivoRutas);
                        br = new BufferedReader(fr);

                        //Lectura de fichero
                        String linea;
                        int id = 1;
                        while ((linea = br.readLine()) != null) {

                                //Obtengo atributos
                                String[] parts = linea.split(",");
                                int ubigeo = Integer.parseInt(parts[0]);
                                String departamento = parts[1];
                                String provincia = parts[2];
                                double latitud = Double.parseDouble(parts[3]);
                                double longitud = Double.parseDouble(parts[4]);
                                String region = parts[5];
                                boolean esPrincipal = nombresOficinasPrincipales.contains(provincia);

                                //Agrega Pedido
                                OficinaModel oficina = new OficinaModel(id, ubigeo, departamento, provincia, latitud, longitud, Region.valueOf(region), esPrincipal);

                                if (esPrincipal) oficinasPrincipales.add(oficina);
                                oficinas.add(oficina);
                                id++;
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
                // Fin código importado
                for (OficinaModel o : oficinasPrincipales) {
                        if (o.getProvincia().equals(ciudad)) return o.getUbigeo();
                }
                return -1;
        }


}