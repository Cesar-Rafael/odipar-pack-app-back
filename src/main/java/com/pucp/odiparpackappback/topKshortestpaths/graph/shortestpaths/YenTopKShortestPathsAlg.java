/*
 *
 * Copyright (c) 2004-2008 Arizona State University.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY ARIZONA STATE UNIVERSITY ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL ARIZONA STATE UNIVERSITY
 * NOR ITS EMPLOYEES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.pucp.odiparpackappback.topKshortestpaths.graph.shortestpaths;

import com.pucp.odiparpackappback.models.Mapa;
import com.pucp.odiparpackappback.topKshortestpaths.graph.Path;
import com.pucp.odiparpackappback.topKshortestpaths.graph.VariableGraph;
import com.pucp.odiparpackappback.topKshortestpaths.graph.abstraction.BaseVertex;
import com.pucp.odiparpackappback.topKshortestpaths.utils.Pair;
import com.pucp.odiparpackappback.topKshortestpaths.utils.QYPriorityQueue;

import java.util.*;

/**
 * @author <a href='mailto:Yan.Qi@asu.edu'>Yan Qi</a>
 * @version $Revision: 783 $
 * @latest $Id: YenTopKShortestPathsAlg.java 783 2009-06-19 19:19:27Z qyan $
 */
public class YenTopKShortestPathsAlg {
    public static VariableGraph graph = new VariableGraph(Mapa.oficinas, Mapa.tramos);

    // intermediate variables
    public static List<Path> resultList = new Vector<Path>();
    public static Map<Path, BaseVertex> pathDerivationVertexIndex = new HashMap<Path, BaseVertex>();
    public static QYPriorityQueue<Path> pathCandidates = new QYPriorityQueue<Path>();

    // the ending vertices of the paths
    public static BaseVertex sourceVertex = null;
    public static BaseVertex targetVertex = null;
    // variables for debugging and testing
    public static int generatedPathNum = 0;

    public static BaseVertex getSourceVertex() {
        return sourceVertex;
    }

    public static void setSourceVertex(BaseVertex sourceV) {
        //this.sourceVertex = sourceVertex;
        sourceVertex = sourceV;
        init();
    }

    public static BaseVertex getTargetVertex() {
        return targetVertex;
    }

    public static void setTargetVertex(BaseVertex targetV) {
        targetVertex = targetV;
    }

    /**
     * Default constructor.
     *
     * @param graph
     * @param k
     */
//    public YenTopKShortestPathsAlg(BaseGraph graph) {
//        this(graph, null, null);
//    }

    /**
     * Constructor 2
     *
     * @param graph
     * @param sourceVertex
     * @param targetVertex
     */
//    public YenTopKShortestPathsAlg(BaseGraph graph, BaseVertex sourceVertex, BaseVertex targetVertex) {
//        if (graph == null) {
//            throw new IllegalArgumentException("A NULL graph object occurs!");
//        }
//        this.graph = new VariableGraph((Graph) graph);
//        this.sourceVertex = sourceVertex;
//        this.targetVertex = targetVertex;
//        init();
//    }

    /**
     * Initiate members in the class.
     */
    public static void init() {
        clear();
        // get the shortest path by default if both source and target exist
        if (sourceVertex != null && targetVertex != null) {
            Path shortestPath = getShortestPath(sourceVertex, targetVertex);
            if (!shortestPath.getVertexList().isEmpty()) {
                pathCandidates.add(shortestPath);
                pathDerivationVertexIndex.put(shortestPath, sourceVertex);
            }
        }
    }

    /**
     * Clear the variables of the class.
     */
    public static void clear() {
        pathCandidates = new QYPriorityQueue<Path>();
        pathDerivationVertexIndex.clear();
        resultList.clear();
        generatedPathNum = 0;
    }

    /**
     * Obtain the shortest path connecting the source and the target, by using the
     * classical Dijkstra shortest path algorithm.
     *
     * @param sourceVertex
     * @param targetVertex
     * @return
     */
    public static Path getShortestPath(BaseVertex sourceVertex, BaseVertex targetVertex) {
        DijkstraShortestPathAlg dijkstraAlg = new DijkstraShortestPathAlg(graph);
        return dijkstraAlg.getShortestPath(sourceVertex, targetVertex);
    }

    /**
     * Check if there exists a path, which is the shortest among all candidates.
     *
     * @return
     */
    public static boolean hasNext() {
        return !pathCandidates.isEmpty();
    }

    /**
     * Get the shortest path among all that connecting source with targe.
     *
     * @return
     */
    public static Path next() {
        //3.1 prepare for removing vertices and arcs
        Path curPath = pathCandidates.poll();
        resultList.add(curPath);

        BaseVertex curDerivation = pathDerivationVertexIndex.get(curPath);
        int curPathHash = curPath.getVertexList().subList(0, curPath.getVertexList().indexOf(curDerivation)).hashCode();

        int count = resultList.size();

        //3.2 remove the vertices and arcs in the graph
        for (int i = 0; i < count - 1; ++i) {
            Path curResultPath = resultList.get(i);

            int curDevVertexId = curResultPath.getVertexList().indexOf(curDerivation);

            if (curDevVertexId < 0) {
                continue;
            }

            // Note that the following condition makes sure all candidates should be considered.
            /// The algorithm in the paper is not correct for removing some candidates by mistake.
            int pathHash = curResultPath.getVertexList().subList(0, curDevVertexId).hashCode();
            if (pathHash != curPathHash) {
                continue;
            }

            BaseVertex curSuccVertex = curResultPath.getVertexList().get(curDevVertexId + 1);

            graph.deleteEdge(new Pair<Integer, Integer>(curDerivation.getId(), curSuccVertex.getId()));
        }

        int pathLength = curPath.getVertexList().size();
        List<BaseVertex> curPathVertexList = curPath.getVertexList();
        for (int i = 0; i < pathLength - 1; ++i) {
            graph.deleteVertex(curPathVertexList.get(i).getId());
            graph.deleteEdge(new Pair<Integer, Integer>(curPathVertexList.get(i).getId(), curPathVertexList.get(i + 1).getId()));
        }

        //3.3 calculate the shortest tree rooted at target vertex in the graph
        DijkstraShortestPathAlg reverseTree = new DijkstraShortestPathAlg(graph);
        reverseTree.getShortestPathFlower(targetVertex);

        //3.4 recover the deleted vertices and update the cost and identify the new candidate results
        boolean isDone = false;
        for (int i = pathLength - 2; i >= 0 && !isDone; --i) {
            //3.4.1 get the vertex to be recovered
            BaseVertex curRecoverVertex = curPathVertexList.get(i);
            graph.recoverDeletedVertex(curRecoverVertex.getId());

            //3.4.2 check if we should stop continuing in the next iteration
            if (curRecoverVertex.getId() == curDerivation.getId()) {
                isDone = true;
            }

            //3.4.3 calculate cost using forward star form
            Path subPath = reverseTree.updateCostForward(curRecoverVertex);

            //3.4.4 get one candidate result if possible
            if (subPath != null) {
                ++generatedPathNum;

                //3.4.4.1 get the prefix from the concerned path
                double cost = 0;
                List<BaseVertex> prePathList = new Vector<BaseVertex>();
                reverseTree.correctCostBackward(curRecoverVertex);

                for (int j = 0; j < pathLength; ++j) {
                    BaseVertex curVertex = curPathVertexList.get(j);
                    if (curVertex.getId() == curRecoverVertex.getId()) {
                        j = pathLength;
                    } else {
                        cost += graph.getEdgeWeightOfGraph(curPathVertexList.get(j), curPathVertexList.get(j + 1));
                        prePathList.add(curVertex);
                    }
                }
                prePathList.addAll(subPath.getVertexList());

                //3.4.4.2 compose a candidate
                subPath.setWeight(cost + subPath.getWeight());
                subPath.getVertexList().clear();
                subPath.getVertexList().addAll(prePathList);

                //3.4.4.3 put it in the candidate pool if new
                if (!pathDerivationVertexIndex.containsKey(subPath)) {
                    pathCandidates.add(subPath);
                    pathDerivationVertexIndex.put(subPath, curRecoverVertex);
                }
            }

            //3.4.5 restore the edge
            BaseVertex succVertex = curPathVertexList.get(i + 1);
            graph.recoverDeletedEdge(new Pair<Integer, Integer>(curRecoverVertex.getId(), succVertex.getId()));

            //3.4.6 update cost if necessary
            double cost1 = graph.getEdgeWeight(curRecoverVertex, succVertex) + reverseTree.getStartVertexDistanceIndex().get(succVertex);

            if (reverseTree.getStartVertexDistanceIndex().get(curRecoverVertex) > cost1) {
                reverseTree.getStartVertexDistanceIndex().put(curRecoverVertex, cost1);
                reverseTree.getPredecessorIndex().put(curRecoverVertex, succVertex);
                reverseTree.correctCostBackward(curRecoverVertex);
            }
        }

        //3.5 restore everything
        graph.recoverDeletedEdges();
        graph.recoverDeletedVertices();

        for (Pair p : Mapa.bloqueos) {
            graph.deleteEdge(p);
        }

        return curPath;
    }

    /**
     * Get the top-K shortest paths connecting the source and the target.
     * This is a batch execution of top-K results.
     *
     * @param source
     * @param sink
     * @param k
     * @return
     */
    public static List<Path> getShortestPaths(BaseVertex source, BaseVertex target, int k) {
        sourceVertex = source;
        targetVertex = target;

        init();
        int count = 0;
        while (hasNext() && count < k) {
            next();
            ++count;
        }

        return resultList;
    }

    /**
     * Return the list of results generated on the whole.
     * (Note that some of them are duplicates)
     *
     * @return
     */
    public static List<Path> getResultList() {
        return resultList;
    }

    /**
     * The number of distinct candidates generated on the whole.
     *
     * @return
     */
    public static int getCadidateSize() {
        return pathDerivationVertexIndex.size();
    }

    public static int getGeneratedPathSize() {
        return generatedPathNum;
    }

    public static ArrayList<Path> getKShortestPaths(int k, int ubigeoDestino, Pair<Integer, Integer> edge) {
        setTargetVertex(graph.getVertex(ubigeoDestino));
        ArrayList<Path> rutas = new ArrayList<>();
        for (int i = 0; i < Mapa.oficinasPrincipales.size(); i++) {
            setSourceVertex(graph.getVertex(Mapa.oficinasPrincipales.get(i).getUbigeo()));
            for (int j = 1; j <= k; j++) {
                if (!YenTopKShortestPathsAlg.hasNext()) break;
                rutas.add(YenTopKShortestPathsAlg.next());
                if (edge != null) graph.deleteEdge(edge);
            }
        }

        if (edge != null) graph.deleteEdge(edge);

        Collections.sort(rutas);
        return rutas;
    }
}
