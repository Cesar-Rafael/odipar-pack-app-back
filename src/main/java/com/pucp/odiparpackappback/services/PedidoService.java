package com.pucp.odiparpackappback.services;

import com.pucp.odiparpackappback.models.PedidoModel;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {
    public List<PedidoModel> getPedidos(){
        ArrayList<PedidoModel> pedidos = new ArrayList<>();

        File archivoPedidos;
        FileReader fr = null;
        BufferedReader br;

        try {
            archivoPedidos = new File("src/main/resources/static/archivoPedidos.txt");
            fr = new FileReader(archivoPedidos);
            br = new BufferedReader(fr);

            //Lectura de fichero
            String linea;
            int id = 1;
            while ((linea = br.readLine()) != null) {

                //Obtengo atributos
                String[] parts = linea.split(",");
                //int dd = Integer.parseInt(parts[0]);
                //int hh = Integer.parseInt(parts[1]);

                //parts = parts[2].split(",");

                long fechaHoraCreacion = Long.parseLong(parts[0]);
                int destino = Integer.parseInt(parts[1]);
                int numPaquetes = Integer.parseInt(parts[2].replaceAll("\\s+", ""));

                //Pedido parcial en caso sea muy grande el pedido

                //Agrega Pedido
                PedidoModel pedido = new PedidoModel(id, numPaquetes, destino, fechaHoraCreacion);
                //System.out.println(pedido.toString());
                pedidos.add(pedido);
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

        return pedidos;
    }
}
