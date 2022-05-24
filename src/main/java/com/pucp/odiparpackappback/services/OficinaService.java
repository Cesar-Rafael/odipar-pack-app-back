package com.pucp.odiparpackappback.services;

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
public class OficinaService {
    public List<OficinaModel> getOficinas(ArrayList<OficinaModel> oficinasPrincipales){
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

        return oficinas;
    }
}
