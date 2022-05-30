package com.pucp.odiparpackappback.services.utils;

import com.pucp.odiparpackappback.models.Mapa;
import com.pucp.odiparpackappback.topKshortestpaths.graph.Graph;
import com.pucp.odiparpackappback.topKshortestpaths.graph.Path;
import com.pucp.odiparpackappback.topKshortestpaths.graph.VariableGraph;
import com.pucp.odiparpackappback.topKshortestpaths.graph.shortestpaths.YenTopKShortestPathsAlg;

import java.util.ArrayList;
import java.util.Collections;

public class ShortestPathRouting {
    private static final Graph grafo = new VariableGraph(Mapa.oficinas, Mapa.tramos);
    private static final YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(grafo);;

    public static ArrayList<Path> getKShortestPaths(int k, int ubigeoDestino) {
        yenAlg.setTargetVertex(grafo.getVertex(ubigeoDestino));
        ArrayList<Path> rutas = new ArrayList<>();

        for (int i = 0; i < Mapa.oficinasPrincipales.size(); i++) {
            //System.out.println("Oficina " + Mapa.oficinasPrincipales.get(i));

            yenAlg.setSourceVertex(grafo.getVertex(Mapa.oficinasPrincipales.get(i).getUbigeo()));
            for (int j = 1; j <= k; j++) {
                if (!yenAlg.hasNext()) break;
                rutas.add(yenAlg.next());
                //System.out.println("Path " + j + " : " + yenAlg.next());
            }
        }

        Collections.sort(rutas);
        return rutas;
    }
}
