package com.pucp.odiparpackappback.services.algorithm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucp.odiparpackappback.models.*;
import com.pucp.odiparpackappback.services.utils.DatosUtil;
import com.pucp.odiparpackappback.topKshortestpaths.graph.Path;
import com.pucp.odiparpackappback.topKshortestpaths.graph.abstraction.BaseVertex;
import com.pucp.odiparpackappback.topKshortestpaths.graph.shortestpaths.YenTopKShortestPathsAlg;
import com.pucp.odiparpackappback.topKshortestpaths.utils.Pair;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ABC {

    public void algoritmoAbejasVPRTW(int numAbejasObr, int numAbejasObs, int numGen, int opcion, int velocidad) {
        // Opcion 0 - Simulacion
        // Opcion 1 - DiaDia

        ArrayList<PedidoModel> pedidos = new ArrayList<>();
        if (opcion == 0) {
            /*LocalDateTime fin = Mapa.inicioSimulacion;
            Date fechaInicio = obtenerFecha(Mapa.inicioSimulacion);
            fin = fin.plusHours(6);
            Mapa.finSimulacion = fin;
            fin = fin.plusMinutes(5 * velocidad * 288 * 7);
            Date fechaFin = obtenerFecha(fin);*/

            Date fechaInicio = obtenerFecha(Mapa.inicioSimulacion);
            Date fechaFin = obtenerFecha(Mapa.finSimulacion);

            // Rango de Pedidos
            for (PedidoModel p : Mapa.pedidosSimulacion) {
                if (fechaInicio.compareTo(p.getFechaHoraCreacion()) <= 0 && p.getFechaHoraCreacion().compareTo(fechaFin) <= 0) {
                    pedidos.add(p);
                }
            }

            Mapa.inicioSimulacion = Mapa.finSimulacion;
            Mapa.finSimulacion = Mapa.inicioSimulacion.plusHours(6);

        } else {
            // Para las operaciones Día a Día los Pedidos se leen desde la BD
            Mapa.cargarPedidosDiaDia(obtenerFecha(Mapa.inicioDiaDia), obtenerFecha(Mapa.finDiaDia));
            // "pedidos" se utilizará para el algoritmo
            pedidos = Mapa.pedidosDiaDia;
        }

        if (pedidos.size() == 0) {
            System.out.println("No hay pedidos que asignar...");
            return;
        }
        // Inicio ABC
        int contador = 0;
        // Etapa: Generación de la Población Inicial
        while (true) {
            int i = generarNumeroEnteroAleatorio(pedidos.size());
            // En caso el pedido escogido al azar no esté asignado...
            if (pedidos.get(i).getEstado() == EstadoPedido.NO_ASIGNADO) {
                boolean estado = asignarPedidoPoblacionInicial(pedidos.get(i), opcion);
                if (!estado) {
                    break;
                }
                contador++;
                if (contador == pedidos.size()) {
                    break;
                }
            }
        }

        if (opcion == 0) {
            // Llamado a InsertarListaRutas
            ArrayList<RutaModel> rutasAux = new ArrayList<>();
            for (int rm = 0; rm < Mapa.rutasSimulacion.size(); rm++) {
                RutaModel rutaAux = new RutaModel();
                rutaAux.setIdRuta(Mapa.rutasSimulacion.get(rm).getIdRuta());
                rutaAux.setSeguimiento(Mapa.rutasSimulacion.get(rm).getSeguimiento());
                rutaAux.setIdUnidadTransporte(Mapa.rutasSimulacion.get(rm).getIdUnidadTransporte());
                ArrayList<Long> list = Mapa.rutasSimulacion.get(rm).getHorasDeLlegada();
                StringBuilder listString = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    listString.append(list.get(i));
                    if (i != list.size() - 1) listString.append(",");
                }
                rutasAux.add(rutaAux);
            }
            Mapa.cargarRutas(rutasAux);
            //Mapa.inicioSimulacion = Mapa.inicioSimulacion.plusMinutes(5 * velocidad * 288);
        } else {
            // Llamado a InsertarListaRutas
            ArrayList<RutaModel> rutasAux = new ArrayList<>();
            for (int rm = 0; rm < Mapa.rutasDiaDia.size(); rm++) {
                RutaModel rutaAux = new RutaModel();
                rutaAux.setIdRuta(Mapa.rutasDiaDia.get(rm).getIdRuta());
                rutaAux.setSeguimiento(Mapa.rutasDiaDia.get(rm).getSeguimiento());
                rutaAux.setIdUnidadTransporte(Mapa.rutasDiaDia.get(rm).getIdUnidadTransporte());

                ArrayList<Long> list = Mapa.rutasDiaDia.get(rm).getHorasDeLlegada();

                StringBuilder listString = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    listString.append(list.get(i));
                    if (i != list.size() - 1) listString.append(",");
                }
                rutasAux.add(rutaAux);
            }

            Mapa.cargarRutas(rutasAux);
            //Mapa.inicioSimulacion = Mapa.inicioSimulacion.plusMinutes(5 * velocidad * 288);
        }
    }

    public int generarNumeroEnteroAleatorio(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public boolean asignarPedidoPoblacionInicial(PedidoModel pedido, int opcion) {
        if (opcion == 0) {
            // Se asigna el pedido actual a la población inicial...
            boolean asignado;
            for (int a = 0; a < Mapa.rutasSimulacion.size(); a++) {
                // Se verifica si termino
                ZoneId zoneId = ZoneId.systemDefault();
                if (Mapa.rutasSimulacion.get(a).getHorasDeLlegada().get(Mapa.rutasSimulacion.get(a).getHorasDeLlegada().size() - 1) > Mapa.inicioSimulacion.atZone(zoneId).toEpochSecond()) {
                    Mapa.rutasSimulacion.get(a).setFlagTerminado(false);
                }
                // Se verifica si el pedido puede ser asignado a esa ruta
                asignado = asignarPedidoRutaVehiculo(pedido, Mapa.rutasSimulacion.get(a), opcion);
                if (asignado) {
                    // Fue asignado correctamente
                    return true;
                }
                // Si no puede ser asignado, se revisa la siguiente ruta...
            }
            // Si no puede ser asignado a alguna ruta... se crea una nueva y se inserta al arreglo de rutas
            boolean bool = kShortestPathRoutingPedido(pedido, 0, opcion);
            Region auxRegion = Mapa.oficinas.get(0).getRegion();
            if (!bool) {
                // No hay camiones para asignar más pedidos, pero... ¿Alcanza tiempo para que lleguen según lo establecido?
                for (int z = 0; z < Mapa.oficinas.size(); z++) {
                    if (Mapa.oficinas.get(z).getUbigeo() == pedido.getIdCiudadDestino()) {
                        auxRegion = Mapa.oficinas.get(z).getRegion();
                        break;
                    }
                }
                ZoneId zoneId = ZoneId.systemDefault();
                // Si el PEDIDO puede llegar aún a tiempo...
                if (Mapa.finSimulacion.isBefore(LocalDateTime.ofInstant(pedido.getFechaHoraCreacion().toInstant(), ZoneId.systemDefault()).plusDays(auxRegion.getCode() + 1))) {
                    // Local Principal más cercano al destino del Pedido
                    ArrayList<Path> rutasPath = YenTopKShortestPathsAlg.getKShortestPaths(0 + 1, pedido.getIdCiudadDestino(), null);
                    // Si alcanza tiempo, asignar a Vehiculo más cercano cuyo fin de ruta sea el inicio de la recien creada
                    int iMenor = -1;    // Inicialización
                    String seguimiento = rutasPath.get(0).getVertexList().toString();
                    List<Integer> listaSeg2 = new ArrayList<>();
                    boolean iPrimero = false;
                    for (int i = 0; i < Mapa.rutasSimulacion.size(); i++) {
                        List<Integer> listaSeg = new ArrayList<>();
                        try {
                            listaSeg = new ObjectMapper().reader(List.class).readValue(Mapa.rutasSimulacion.get(i).getSeguimiento());
                            listaSeg2 = new ObjectMapper().reader(List.class).readValue(seguimiento);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        // Si el último UBIGEO del seguimiento es igual al origen de la ruta del pedido y ... el Vehiculo puede ser asignado: NO ESTÁ RESERVADO
                        if (listaSeg.get(0).equals(listaSeg2.get(0)) && !(Mapa.vehiculosSimulacion.get(Math.toIntExact(Mapa.rutasSimulacion.get(i).getIdUnidadTransporte())).getEstado().getCode() == 1)) {
                            if (iPrimero == false) {
                                iMenor = i;
                            } else {
                                if (Mapa.rutasSimulacion.get(iMenor).getHorasDeLlegada().get(Mapa.rutasSimulacion.get(iMenor).getHorasDeLlegada().size() - 1) > Mapa.rutasSimulacion.get(i).getHorasDeLlegada().get(Mapa.rutasSimulacion.get(i).getHorasDeLlegada().size() - 1)) {
                                    iMenor = i;
                                }
                            }
                        }
                    }
                    if (iMenor == -1) {
                        // Ninguna fin de ruta coincide con la mejor ruta que puede tomar el pedido
                        System.out.println("Hay pedidos pendientes de entrega...");
                        return false;
                    }
                    // En este punto, tengo el vehiculo que voy a seleccionar y la ruta que se tomará
                    Long idVehiculoEscogido = Mapa.rutasSimulacion.get(iMenor).getIdUnidadTransporte();
                    // Se agrega una nueva ruta en rutasSimulacion
                    Long idRuta = Long.valueOf(Mapa.rutasSimulacion.size());
                    ArrayList<PedidoParcialModel> pedidosParciales = new ArrayList<>();
                    // CREACION DE PEDIDO PARCIAL
                    double fitness = rutasPath.get(0).getWeight();

                    ArrayList<Long> horasLlegadaLong = new ArrayList<>();
                    List<BaseVertex> oficinas = rutasPath.get(0).getVertexList();
                    ArrayList<LocalDateTime> horasLlegada = new ArrayList<>();

                    //Regreso
                    List<Path> regresoPath = YenTopKShortestPathsAlg.getShortestPathsReturn(rutasPath.get(0).getVertexList().get(rutasPath.get(0).getVertexList().size() - 1).getId());
                    int ganador = 1;
                    for (int p = 0; p < regresoPath.size(); p++) {
                        if (regresoPath.get(p).getWeight() < regresoPath.get(ganador).getWeight()) ganador = p;
                    }
                    regresoPath.get(ganador).getVertexList().remove(0);
                    //System.out.println("regreso");
                    //System.out.println(rutaRegreso);
                    //cambiar parametros(seguimiento, tramos, horasLlegadaLong);
                    List<BaseVertex> oficinasRegreso = regresoPath.get(ganador).getVertexList();
                    for (int or = 0; or < oficinasRegreso.size(); or++) {
                        oficinas.add(oficinasRegreso.get(or));
                    }
                    seguimiento = oficinas.toString();
                    ArrayList<TramoModel> tramos = Mapa.listarTramos(seguimiento);

                    for (int i = 0; i < oficinas.size(); i++) {
                        if (i == 0) {
                            horasLlegadaLong.add(Mapa.rutasSimulacion.get(iMenor).getHorasDeLlegada().get(Mapa.rutasSimulacion.get(iMenor).getHorasDeLlegada().size() - 1) + 3600);
                            horasLlegada.add(LocalDateTime.ofInstant(Instant.ofEpochSecond(Mapa.rutasSimulacion.get(iMenor).getHorasDeLlegada().get(Mapa.rutasSimulacion.get(iMenor).getHorasDeLlegada().size() - 1) + 3600), zoneId));
                        } else {
                            double tiempoViaje = findTiempoViaje(oficinas.get(i - 1).getId(), oficinas.get(i).getId());
                            int horas = (int) Math.floor(tiempoViaje);
                            int minutos = (int) Math.ceil((tiempoViaje - 1.0 * horas) * 60);
                            LocalDateTime horaLlegada = horasLlegada.get(i - 1);

                            if (i == 1) {
                                horaLlegada = horaLlegada.plusHours(horas);
                            } else {
                                horaLlegada = horaLlegada.plusHours(horas + 1);
                            }

                            horaLlegada = horaLlegada.plusMinutes(minutos);
                            horasLlegadaLong.add(horaLlegada.atZone(zoneId).toEpochSecond());
                            horasLlegada.add(horaLlegada);
                        }
                    }
                    Mapa.rutasSimulacion.get(iMenor).setFlagTerminado(true);
                    Mapa.vehiculosSimulacion.get(Math.toIntExact(Mapa.rutasSimulacion.get(iMenor).getIdUnidadTransporte())).setEstado(EstadoUnidadTransporte.RESERVADO);
                    ArrayList<Integer> auxAI = new ArrayList<>();
                    try {
                        auxAI = new ObjectMapper().reader(List.class).readValue(seguimiento);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    int indiceAux = -1;
                    for (int indice = 0; indice < auxAI.size(); indice++) {
                        if (auxAI.get(indice) == pedido.getIdCiudadDestino()) {
                            indiceAux = indice;
                        }
                    }
                    pedido.setEstado(EstadoPedido.EN_PROCESO);
                    PedidoParcialModel pedidoParcial = new PedidoParcialModel(0L, pedido.getId(), -1, pedido.getCantPaquetesNoAsignado(), horasLlegadaLong.get(indiceAux), idRuta);
                    pedidosParciales.add(pedidoParcial);
                    // SE CREA UNA NUEVA RUTA
                    Ruta rutaAux = new Ruta(idRuta, seguimiento, pedidosParciales, fitness, idVehiculoEscogido, tramos, horasLlegadaLong);
                    Mapa.rutasSimulacion.add(rutaAux);
                    return true;
                } else {
                    // Si no alcanza tiempo, es colapso logístico
                    System.out.println("¡Colapso Logistico!");
                    return false;
                }
            }
            // es falso, si ya no se puede crear más rutas
            return bool;
        } else {
            Region auxRegion = Mapa.oficinas.get(0).getRegion();
            // Se asigna el pedido actual a la población inicial...
            boolean asignado;
            for (int a = 0; a < Mapa.rutasDiaDia.size(); a++) {
                // Se verifica si termino
                //ZoneId zoneId = ZoneId.systemDefault();
                //if(Mapa.rutasSimulacion.get(a).getHorasDeLlegada().get(Mapa.rutasSimulacion.get(a).getHorasDeLlegada().size()-1) > Mapa.inicioSimulacion.atZone(zoneId).toEpochSecond()){
                //    Mapa.rutasSimulacion.get(a).setFlagTerminado(false);
                //}
                // Se verifica si el pedido puede ser asignado a esa ruta
                asignado = asignarPedidoRutaVehiculo(pedido, Mapa.rutasDiaDia.get(a), opcion);
                if (asignado) {
                    // Fue asignado correctamente
                    return true;
                }
                // Si no puede ser asignado, se revisa la siguiente ruta...
            }
            // Si no puede ser asignado a alguna ruta... se crea una nueva y se inserta al arreglo de rutas
            boolean bool = kShortestPathRoutingPedido(pedido, 0, opcion);
            if (!bool) {
                // LLENAR COLAPSO LOGISTICO
            }
            // es falso, si ya no se puede crear más rutas
            return bool;
        }
    }

    public Ruta kShortestPathRoutingRuta(Ruta rutaOriginal, int k, Pair<Integer, Integer> edge, int opcion) {
        int ubigeoDestino = rutaOriginal.getTramos().get(rutaOriginal.getTramos().size() - 1).getIdCiudadJ();
        // si k es 0, es la mejor ruta, si es 1, la segunda mejor ruta...
        ArrayList<Path> rutasPath = YenTopKShortestPathsAlg.getKShortestPaths(k, ubigeoDestino, edge);

        List<BaseVertex> oficinas = rutasPath.get(k).getVertexList();
        //ArrayList<Long> horasLlegada = new ArrayList<>();
        ArrayList<LocalDateTime> horasLlegada = new ArrayList<>();
        ArrayList<Long> horasLlegadaLong = new ArrayList<>();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime inicio;
        if (opcion == 0) inicio = Mapa.inicioSimulacion;
        else inicio = Mapa.finDiaDia;
        for (int i = 0; i < oficinas.size(); i++) {
            if (i == 0) {
                horasLlegadaLong.add(inicio.atZone(zoneId).toEpochSecond());
                horasLlegada.add(inicio);
            } else {
                double tiempoViaje = findTiempoViaje(oficinas.get(i - 1).getId(), oficinas.get(i).getId());
                int horas = (int) Math.floor(tiempoViaje);
                int minutos = (int) Math.ceil((tiempoViaje - 1.0 * horas) * 60);
                LocalDateTime horaLlegada = horasLlegada.get(i - 1);

                if (i == 1) {
                    horaLlegada = horaLlegada.plusHours(horas);
                } else {
                    horaLlegada = horaLlegada.plusHours(horas + 1);
                }

                horaLlegada = horaLlegada.plusMinutes(minutos);

                horasLlegadaLong.add(horaLlegada.atZone(zoneId).toEpochSecond());
                horasLlegada.add(horaLlegada);
            }
        }

        Ruta rutaEncontrada = null;

        for (int i = 0; i < horasLlegada.size() - 1; i++) {
            int oficinaI = oficinas.get(i).getId();
            int oficinaJ = oficinas.get(i + 1).getId();
            List<BloqueoModel> bloqueos;
            if (opcion == 0) {
                //bloqueos = Mapa.obtenerTramosBloqueadosSimulacion(oficinaI, oficinaJ, obtenerFecha(horasLlegada.get(i)), obtenerFecha(horasLlegada.get(i + 1)), "src/main/resources/static/bloqueo_model.csv");
                bloqueos = Mapa.obtenerTramosBloqueadosDiaDia(oficinaI, oficinaJ, obtenerFecha(horasLlegada.get(i)), obtenerFecha(horasLlegada.get(i + 1)));
            } else {
                bloqueos = Mapa.obtenerTramosBloqueadosDiaDia(oficinaI, oficinaJ, obtenerFecha(horasLlegada.get(i)), obtenerFecha(horasLlegada.get(i + 1)));
            }
            //List<BloqueoModel> bloqueos = Mapa.obtenerTramosBloqueados(oficinaI, oficinaJ, obtenerFecha(horasLlegada.get(i).minusHours(6)), obtenerFecha(horasLlegada.get(i + 1).minusHours(6)));
            if (bloqueos.size() > 0) {
                System.out.println("Bloqueo Encontrado");
                Pair tramoBloqueado = new Pair<Integer, Integer>(oficinaI, oficinaJ);
                YenTopKShortestPathsAlg.graph.deleteEdge(tramoBloqueado);
                Mapa.bloqueos.add(tramoBloqueado);
                rutaEncontrada = kShortestPathRoutingRuta(rutaOriginal, k, tramoBloqueado, opcion);
            }
        }

        if (rutaEncontrada != null) return rutaEncontrada;

        // String seguimiento
        String seguimiento = rutasPath.get(k).getVertexList().toString();
        Mapa.seguimiento = seguimiento;
        ArrayList<TramoModel> tramos = Mapa.listarTramos(seguimiento);
        // double fitness
        double fitness = rutasPath.get(k).getWeight();
        // Asignación
        Ruta ruta = new Ruta(rutaOriginal.getIdRuta(), seguimiento, rutaOriginal.getPedidosParciales(), fitness, rutaOriginal.getIdUnidadTransporte(), tramos, horasLlegadaLong);
        return ruta;
    }

    public boolean kShortestPathRoutingPedido(PedidoModel pedido, int k, int opcion) {
        if (opcion == 0) {
            // si k es 0, es la mejor ruta, si es 1, la segunda mejor ruta...
            ArrayList<Path> rutasPath = YenTopKShortestPathsAlg.getKShortestPaths(k + 1, pedido.getIdCiudadDestino(), null);
            Long idRuta = Long.valueOf(Mapa.rutasSimulacion.size());
            if (rutasPath.size() == 0) {
                System.out.println("pedido id: " + pedido.getId());
            }

            //Regreso
            List<Path> regresoPath = YenTopKShortestPathsAlg.getShortestPathsReturn(rutasPath.get(k).getVertexList().get(rutasPath.get(k).getVertexList().size() - 1).getId());
            int ganador = 1;
            for (int p = 0; p < regresoPath.size(); p++) {
                if (regresoPath.get(p).getWeight() < regresoPath.get(ganador).getWeight()) ganador = p;
            }
            regresoPath.get(ganador).getVertexList().remove(0);
            //System.out.println("regreso");
            //System.out.println(rutaRegreso);
            //cambiar parametros(seguimiento, tramos, horasLlegadaLong);
            List<BaseVertex> oficinasRegreso = regresoPath.get(ganador).getVertexList();

            // Parámetros
            List<BaseVertex> oficinas = rutasPath.get(k).getVertexList();
            for (int or = 0; or < oficinasRegreso.size(); or++) {
                oficinas.add(oficinasRegreso.get(or));
            }
            String seguimiento = oficinas.toString();
            ArrayList<PedidoParcialModel> pedidosParciales = new ArrayList<>();


            ArrayList<LocalDateTime> horasLlegada = new ArrayList<>();
            ArrayList<Long> horasLlegadaLong = new ArrayList<>();
            ZoneId zoneId = ZoneId.systemDefault();

            for (int i = 0; i < oficinas.size(); i++) {
                if (i == 0) {
                    horasLlegadaLong.add(Mapa.inicioSimulacion.atZone(zoneId).toEpochSecond());
                    horasLlegada.add(Mapa.inicioSimulacion);
                } else {
                    double tiempoViaje = findTiempoViaje(oficinas.get(i - 1).getId(), oficinas.get(i).getId());
                    int horas = (int) Math.floor(tiempoViaje);
                    int minutos = (int) Math.ceil((tiempoViaje - 1.0 * horas) * 60);
                    LocalDateTime horaLlegada = horasLlegada.get(i - 1);

                    if (i == 1) {
                        horaLlegada = horaLlegada.plusHours(horas);
                    } else {
                        horaLlegada = horaLlegada.plusHours(horas + 1);
                    }

                    horaLlegada = horaLlegada.plusMinutes(minutos);
                    horasLlegadaLong.add(horaLlegada.atZone(zoneId).toEpochSecond());
                    horasLlegada.add(horaLlegada);
                }
            }

            double fitness = rutasPath.get(k).getWeight();

            for (int i = 0; i < Mapa.vehiculosSimulacion.size(); i++) {
                // El vehículo está disponible
                if (Mapa.vehiculosSimulacion.get(i).getEstado() == EstadoUnidadTransporte.DISPONIBLE) {
                    // ¿Hay capacidad disponible suficiente?
                    if (Mapa.vehiculosSimulacion.get(i).getCapacidadDisponible() >= pedido.getCantPaquetesNoAsignado()) {
                        // Sí hay capacidad disponible suficiente
                        Mapa.vehiculosSimulacion.get(i).setEstado(EstadoUnidadTransporte.EN_TRANSITO);
                        Mapa.vehiculosSimulacion.get(i).setIdRuta(idRuta);
                        Mapa.vehiculosSimulacion.get(i).setCapacidadDisponible(Mapa.vehiculosSimulacion.get(i).getCapacidadDisponible() - pedido.getCantPaquetesNoAsignado());
                        // Asignación Ruta
                        Long idUnidadTransporte = Mapa.vehiculosSimulacion.get(i).getId();
                        ArrayList<Integer> auxAI = new ArrayList<>();
                        try {
                            auxAI = new ObjectMapper().reader(List.class).readValue(seguimiento);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        int indiceAux = -1;
                        for (int indice = 0; indice < auxAI.size(); indice++) {
                            if (auxAI.get(indice) == pedido.getIdCiudadDestino()) {
                                indiceAux = indice;
                            }
                        }
                        PedidoParcialModel pedidoParcial = new PedidoParcialModel(0L, pedido.getId(), -1, pedido.getCantPaquetesNoAsignado(), horasLlegadaLong.get(indiceAux), idRuta);
                        pedidosParciales.add(pedidoParcial);
                        // Actualización en Pedido
                        pedido.setCantPaquetesNoAsignado(0);
                        pedido.setEstado(EstadoPedido.EN_PROCESO);
                        // Asignación
                        ArrayList<TramoModel> tramos = Mapa.listarTramos(seguimiento);
                        for (int a = 0; a < tramos.size(); a++) {
                            tramos.get(a).setTiempoDeViaje(DatosUtil.calcularTiempoViajeEntreTramos(tramos.get(a).getIdCiudadI(), tramos.get(a).getIdCiudadJ()) * 3600);
                        }
                        Ruta auxRuta = new Ruta(idRuta, seguimiento, pedidosParciales, fitness, idUnidadTransporte, tramos, horasLlegadaLong);
                        Mapa.rutasSimulacion.add(auxRuta);
                        return true;
                    } else {
                        // Actualización en Pedido
                        int faltante = pedido.getCantPaquetesNoAsignado() - Mapa.vehiculosSimulacion.get(i).getCapacidadDisponible();
                        // No hay capacidad disponible suficiente
                        Mapa.vehiculosSimulacion.get(i).setEstado(EstadoUnidadTransporte.EN_TRANSITO);
                        Mapa.vehiculosSimulacion.get(i).setIdRuta(idRuta);
                        // Asignación Ruta
                        Long idUnidadTransporte = Mapa.vehiculosSimulacion.get(i).getId();
                        ArrayList<Integer> auxAI = new ArrayList<>();
                        try {
                            auxAI = new ObjectMapper().reader(List.class).readValue(seguimiento);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        int indiceAux = -1;
                        for (int indice = 0; indice < auxAI.size(); indice++) {
                            if (auxAI.get(indice) == pedido.getIdCiudadDestino()) {
                                indiceAux = indice;
                            }
                        }
                        PedidoParcialModel pedidoParcial = new PedidoParcialModel(0L, pedido.getId(), -1, Mapa.vehiculosSimulacion.get(i).getCapacidadDisponible(), horasLlegadaLong.get(indiceAux), idRuta);
                        pedidosParciales.add(pedidoParcial);
                        // Pedido
                        Mapa.vehiculosSimulacion.get(i).setCapacidadDisponible(0);
                        pedido.setCantPaquetesNoAsignado(faltante);
                        pedido.setEstado(EstadoPedido.EN_PROCESO);
                        // Asignación
                        ArrayList<TramoModel> tramos = Mapa.listarTramos(seguimiento);
                        for (int a = 0; a < tramos.size(); a++) {
                            tramos.get(a).setTiempoDeViaje(DatosUtil.calcularTiempoViajeEntreTramos(tramos.get(a).getIdCiudadI(), tramos.get(a).getIdCiudadJ()) * 3600);
                        }
                        Ruta auxRuta = new Ruta(idRuta, seguimiento, pedidosParciales, fitness, idUnidadTransporte, tramos, horasLlegadaLong);
                        Mapa.rutasSimulacion.add(auxRuta);
                        return kShortestPathRoutingPedido(pedido, 0, opcion);
                    }
                }
            }

            return false;
        } else {
            // si k es 0, es la mejor ruta, si es 1, la segunda mejor ruta...
            ArrayList<Path> rutasPath = YenTopKShortestPathsAlg.getKShortestPaths(k + 1, pedido.getIdCiudadDestino(), null);
            // Parámetros
            Long idRuta = Long.valueOf(Mapa.rutasDiaDia.size());
            if (rutasPath.size() == 0) {
                System.out.println("pedido id: " + pedido.getId());
            }
            //Regreso
            List<Path> regresoPath = YenTopKShortestPathsAlg.getShortestPathsReturn(rutasPath.get(k).getVertexList().get(rutasPath.get(k).getVertexList().size() - 1).getId());
            int ganador = 1;
            for (int p = 0; p < regresoPath.size(); p++) {
                if (regresoPath.get(p).getWeight() < regresoPath.get(ganador).getWeight()) ganador = p;
            }
            regresoPath.get(ganador).getVertexList().remove(0);
            //System.out.println("regreso");
            //System.out.println(rutaRegreso);
            //cambiar parametros(seguimiento, tramos, horasLlegadaLong);
            List<BaseVertex> oficinasRegreso = regresoPath.get(ganador).getVertexList();

            // Parámetros
            List<BaseVertex> oficinas = rutasPath.get(k).getVertexList();
            for (int or = 0; or < oficinasRegreso.size(); or++) {
                oficinas.add(oficinasRegreso.get(or));
            }
            String seguimiento = oficinas.toString();
            ArrayList<PedidoParcialModel> pedidosParciales = new ArrayList<>();

            ArrayList<LocalDateTime> horasLlegada = new ArrayList<>();
            ArrayList<Long> horasLlegadaLong = new ArrayList<>();
            ZoneId zoneId = ZoneId.systemDefault();

            for (int i = 0; i < oficinas.size(); i++) {
                if (i == 0) {
                    horasLlegadaLong.add(Mapa.finDiaDia.atZone(zoneId).toEpochSecond());
                    horasLlegada.add(Mapa.finDiaDia);
                } else {
                    double tiempoViaje = findTiempoViaje(oficinas.get(i - 1).getId(), oficinas.get(i).getId());
                    int horas = (int) Math.floor(tiempoViaje);
                    int minutos = (int) Math.ceil((tiempoViaje - 1.0 * horas) * 60);
                    LocalDateTime horaLlegada = horasLlegada.get(i - 1);

                    if (i == 1) {
                        horaLlegada = horaLlegada.plusHours(horas);
                    } else {
                        horaLlegada = horaLlegada.plusHours(horas + 1);
                    }

                    horaLlegada = horaLlegada.plusMinutes(minutos);
                    horasLlegadaLong.add(horaLlegada.atZone(zoneId).toEpochSecond());
                    horasLlegada.add(horaLlegada);
                }
            }

            double fitness = rutasPath.get(k).getWeight();

            for (int i = 0; i < Mapa.vehiculosDiaDia.size(); i++) {
                // El vehículo está disponible
                if (Mapa.vehiculosDiaDia.get(i).getEstado() == EstadoUnidadTransporte.DISPONIBLE) {
                    // ¿Hay capacidad disponible suficiente?
                    if (Mapa.vehiculosDiaDia.get(i).getCapacidadDisponible() >= pedido.getCantPaquetesNoAsignado()) {
                        // Sí hay capacidad disponible suficiente
                        Mapa.vehiculosDiaDia.get(i).setEstado(EstadoUnidadTransporte.EN_TRANSITO);
                        Mapa.vehiculosDiaDia.get(i).setIdRuta(idRuta);
                        Mapa.vehiculosDiaDia.get(i).setCapacidadDisponible(Mapa.vehiculosDiaDia.get(i).getCapacidadDisponible() - pedido.getCantPaquetesNoAsignado());
                        // Asignación Ruta
                        Long idUnidadTransporte = Mapa.vehiculosDiaDia.get(i).getId();
                        ArrayList<Integer> auxAI = new ArrayList<>();
                        try {
                            auxAI = new ObjectMapper().reader(List.class).readValue(seguimiento);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        int indiceAux = -1;
                        for (int indice = 0; indice < auxAI.size(); indice++) {
                            if (auxAI.get(indice) == pedido.getIdCiudadDestino()) {
                                indiceAux = indice;
                            }
                        }
                        PedidoParcialModel pedidoParcial = new PedidoParcialModel(0L, pedido.getId(), -1, pedido.getCantPaquetesNoAsignado(), horasLlegadaLong.get(indiceAux), idRuta);
                        pedidosParciales.add(pedidoParcial);
                        // Actualización en Pedido
                        pedido.setCantPaquetesNoAsignado(0);
                        pedido.setEstado(EstadoPedido.EN_PROCESO);
                        // Asignación
                        ArrayList<TramoModel> tramos = Mapa.listarTramos(seguimiento);
                        for (int a = 0; a < tramos.size(); a++) {
                            tramos.get(a).setTiempoDeViaje(DatosUtil.calcularTiempoViajeEntreTramos(tramos.get(a).getIdCiudadI(), tramos.get(a).getIdCiudadJ()) * 3600);
                        }
                        Ruta auxRuta = new Ruta(idRuta, seguimiento, pedidosParciales, fitness, idUnidadTransporte, tramos, horasLlegadaLong);
                        Mapa.rutasDiaDia.add(auxRuta);
                        return true;
                    } else {
                        // Actualización en Pedido
                        int faltante = pedido.getCantPaquetesNoAsignado() - Mapa.vehiculosDiaDia.get(i).getCapacidadDisponible();
                        // No hay capacidad disponible suficiente
                        Mapa.vehiculosDiaDia.get(i).setEstado(EstadoUnidadTransporte.EN_TRANSITO);
                        Mapa.vehiculosDiaDia.get(i).setIdRuta(idRuta);
                        // Asignación Ruta
                        Long idUnidadTransporte = Mapa.vehiculosDiaDia.get(i).getId();
                        ArrayList<Integer> auxAI = new ArrayList<>();
                        try {
                            auxAI = new ObjectMapper().reader(List.class).readValue(seguimiento);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        int indiceAux = -1;
                        for (int indice = 0; indice < auxAI.size(); indice++) {
                            if (auxAI.get(indice) == pedido.getIdCiudadDestino()) {
                                indiceAux = indice;
                            }
                        }
                        PedidoParcialModel pedidoParcial = new PedidoParcialModel(0L, pedido.getId(), -1, Mapa.vehiculosDiaDia.get(i).getCapacidadDisponible(), horasLlegadaLong.get(indiceAux), idRuta);
                        pedidosParciales.add(pedidoParcial);
                        // Pedido
                        Mapa.vehiculosDiaDia.get(i).setCapacidadDisponible(0);
                        pedido.setCantPaquetesNoAsignado(faltante);
                        pedido.setEstado(EstadoPedido.EN_PROCESO);
                        // Asignación
                        ArrayList<TramoModel> tramos = Mapa.listarTramos(seguimiento);
                        for (int a = 0; a < tramos.size(); a++) {
                            tramos.get(a).setTiempoDeViaje(DatosUtil.calcularTiempoViajeEntreTramos(tramos.get(a).getIdCiudadI(), tramos.get(a).getIdCiudadJ()) * 3600);
                        }
                        Ruta auxRuta = new Ruta(idRuta, seguimiento, pedidosParciales, fitness, idUnidadTransporte, tramos, horasLlegadaLong);
                        Mapa.rutasDiaDia.add(auxRuta);
                        return kShortestPathRoutingPedido(pedido, 0, opcion);
                    }
                }
            }

            return false;
        }
    }

    public boolean asignarPedidoRutaVehiculo(PedidoModel pedido, Ruta ruta, int opcion) {
        if (opcion == 0) {
            boolean encontrado = false;
            for (int i = 0; i < ruta.getTramos().size(); i++) {
                if ((ruta.getTramos().get(i).getIdCiudadJ() == pedido.getIdCiudadDestino()) && (Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() != 0)) {
                    encontrado = true;
                    break;
                }
            }
            // Se verifica si la ruta tiene la ciudad de destino deseado...
            if (!encontrado) {
                // Si la ruta no tiene el destino, no es asignado
                return false;
            } else {
                // Se verifica si hay capacidad disponible suficiente en el vehículo asignado a esa ruta y no esté en transito
                if ((Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() > pedido.getCantPaquetesNoAsignado()) && !(Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getEstado().getCode() == 2)) {
                    // Hay capacidad suficiente, se asigna el pedido a la ruta...
                    ArrayList<PedidoParcialModel> pedidosParciales = ruta.getPedidosParciales();
                    String seguimiento = ruta.getSeguimiento();
                    ArrayList<Long> horasLlegadaLong = ruta.getHorasDeLlegada();
                    ArrayList<Integer> auxAI = new ArrayList<>();
                    try {
                        auxAI = new ObjectMapper().reader(List.class).readValue(seguimiento);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    int indiceAux = -1;
                    for (int indice = 0; indice < auxAI.size(); indice++) {
                        if (auxAI.get(indice) == pedido.getIdCiudadDestino()) {
                            indiceAux = indice;
                        }
                    }
                    PedidoParcialModel pedidoParcial = new PedidoParcialModel((long) pedidosParciales.size(), pedido.getId(), -1, pedido.getCantPaquetesNoAsignado(), horasLlegadaLong.get(indiceAux), ruta.getIdRuta());
                    pedidosParciales.add(pedidoParcial);
                    ruta.setPedidosParciales(pedidosParciales);
                    // Se actualiza la capacidad disponible en el vehículo
                    Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).setCapacidadDisponible(Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() - pedido.getCantPaquetesNoAsignado());
                    // Se actualiza la cantidad de paquetes no asignados
                    pedido.setCantPaquetesNoAsignado(0);
                    pedido.setEstado(EstadoPedido.EN_PROCESO);
                    return true;
                } else if (!(Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getEstado().getCode() == 2)) {
                    // No hay capacidad suficiente, se asigna el pedido a la ruta y luego se busca otra para completar el pedido
                    int faltante = pedido.getCantPaquetesNoAsignado() - Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible();
                    // Asignación parcial
                    String seguimiento = ruta.getSeguimiento();
                    ArrayList<Long> horasLlegadaLong = ruta.getHorasDeLlegada();
                    ArrayList<Integer> auxAI = new ArrayList<>();
                    try {
                        auxAI = new ObjectMapper().reader(List.class).readValue(seguimiento);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    int indiceAux = -1;
                    for (int indice = 0; indice < auxAI.size(); indice++) {
                        if (auxAI.get(indice) == pedido.getIdCiudadDestino()) {
                            indiceAux = indice;
                        }
                    }
                    ArrayList<PedidoParcialModel> pedidosParciales = ruta.getPedidosParciales();
                    PedidoParcialModel pedidoParcial = new PedidoParcialModel((long) pedidosParciales.size(), pedido.getId(), -1, pedido.getCantPaquetes() - faltante, horasLlegadaLong.get(indiceAux), ruta.getIdRuta());
                    pedidosParciales.add(pedidoParcial);
                    ruta.setPedidosParciales(pedidosParciales);
                    // Se actualiza la capacidad disponible en el vehículo, o sea cero
                    Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).setCapacidadDisponible(0);
                    pedido.setCantPaquetesNoAsignado(faltante);
                    return false;
                } else {
                    return false;
                }
            }
        } else {
            boolean encontrado = false;
            for (int i = 0; i < ruta.getTramos().size(); i++) {
                if ((ruta.getTramos().get(i).getIdCiudadJ() == pedido.getIdCiudadDestino()) && (Mapa.vehiculosDiaDia.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() != 0)) {
                    encontrado = true;
                    break;
                }
            }
            // Se verifica si la ruta tiene la ciudad de destino deseado...
            if (!encontrado) {
                // Si la ruta no tiene el destino, no es asignado
                return false;
            } else {
                // Se verifica si hay capacidad disponible suficiente en el vehículo asignado a esa ruta
                if (Mapa.vehiculosDiaDia.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() > pedido.getCantPaquetesNoAsignado()) {
                    // Hay capacidad suficiente, se asigna el pedido a la ruta...
                    ArrayList<PedidoParcialModel> pedidosParciales = ruta.getPedidosParciales();
                    PedidoParcialModel pedidoParcial = new PedidoParcialModel((long) pedidosParciales.size(), pedido.getId(), -1, pedido.getCantPaquetesNoAsignado(), 0L, ruta.getIdRuta());
                    pedidosParciales.add(pedidoParcial);
                    ruta.setPedidosParciales(pedidosParciales);
                    // Se actualiza la capacidad disponible en el vehículo
                    Mapa.vehiculosDiaDia.get(Math.toIntExact(ruta.getIdUnidadTransporte())).setCapacidadDisponible(Mapa.vehiculosDiaDia.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() - pedido.getCantPaquetesNoAsignado());
                    // Se actualiza la cantidad de paquetes no asignados
                    pedido.setCantPaquetesNoAsignado(0);
                    pedido.setEstado(EstadoPedido.EN_PROCESO);
                    return true;
                } else {
                    // No hay capacidad suficiente, se asigna el pedido a la ruta y luego se busca otra para completar el pedido
                    int faltante = pedido.getCantPaquetesNoAsignado() - Mapa.vehiculosDiaDia.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible();
                    // Asignación parcial
                    ArrayList<PedidoParcialModel> pedidosParciales = ruta.getPedidosParciales();
                    PedidoParcialModel pedidoParcial = new PedidoParcialModel((long) pedidosParciales.size(), pedido.getId(), -1, pedido.getCantPaquetes() - faltante, 0L, ruta.getIdRuta());
                    pedidosParciales.add(pedidoParcial);
                    ruta.setPedidosParciales(pedidosParciales);
                    // Se actualiza la capacidad disponible en el vehículo, o sea cero
                    Mapa.vehiculosDiaDia.get(Math.toIntExact(ruta.getIdUnidadTransporte())).setCapacidadDisponible(0);
                    pedido.setCantPaquetesNoAsignado(faltante);
                    return false;
                }
            }
        }
    }

    double findTiempoViaje(int id_ciudadi, int id_ciudadj) {
        for (int i = 0; i < Mapa.tramos.size(); i++) {
            if ((Mapa.tramos.get(i).getIdCiudadI() == id_ciudadi && Mapa.tramos.get(i).getIdCiudadJ() == id_ciudadj) || (Mapa.tramos.get(i).getIdCiudadI() == id_ciudadj && Mapa.tramos.get(i).getIdCiudadJ() == id_ciudadi)) {
                return Mapa.tramos.get(i).getTiempoDeViaje();
            }
        }
        return -1;
    }

    Date obtenerFecha(LocalDateTime fecha) {
        Date nuevaFecha = Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());
        return nuevaFecha;
    }

    ArrayList<UnidadTransporteModel> actualizarMantenimiento(List<PedidoParcialModel> auxPedidos, int opcion) {
        if (opcion == 0) {
            ArrayList<UnidadTransporteModel> auxArrayVehiculos = new ArrayList<>();
            for (int i = 0; i < auxPedidos.size(); i++) {
                for (int j = 0; j < Mapa.vehiculosSimulacion.size(); j++) {
                    if (Mapa.vehiculosSimulacion.get(j).getEstado() == EstadoUnidadTransporte.DISPONIBLE || Mapa.vehiculosSimulacion.get(j).getEstado() == EstadoUnidadTransporte.EN_TRANSITO) {
                        Date d = new Date(auxPedidos.get(i).getFechaHoraEntrega() * 1000);
                        LocalDateTime auxd = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        LocalDateTime auxd2 = Mapa.vehiculosSimulacion.get(j).getFechaMantenimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        if (auxd.isBefore(auxd2.plusDays(1))) {
                            Mapa.vehiculosSimulacion.get(j).setEstado(EstadoUnidadTransporte.EN_MANTENIMIENTO);
                            auxArrayVehiculos.add(Mapa.vehiculosSimulacion.get(j));
                        }
                    }
                }
            }
            return auxArrayVehiculos;
        } else {
            ArrayList<UnidadTransporteModel> auxArrayVehiculos = new ArrayList<>();
            for (int i = 0; i < auxPedidos.size(); i++) {
                for (int j = 0; j < Mapa.vehiculosDiaDia.size(); j++) {
                    if (Mapa.vehiculosDiaDia.get(j).getEstado() == EstadoUnidadTransporte.DISPONIBLE || Mapa.vehiculosDiaDia.get(j).getEstado() == EstadoUnidadTransporte.EN_TRANSITO) {
                        Date d = new Date(auxPedidos.get(i).getFechaHoraEntrega() * 1000);
                        LocalDateTime auxd = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        LocalDateTime auxd2 = Mapa.vehiculosDiaDia.get(j).getFechaMantenimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                        if (auxd.isBefore(auxd2.plusDays(1))) {
                            Mapa.vehiculosDiaDia.get(j).setEstado(EstadoUnidadTransporte.EN_MANTENIMIENTO);
                            auxArrayVehiculos.add(Mapa.vehiculosDiaDia.get(j));
                        }
                    }
                }
            }
            return auxArrayVehiculos;
        }
    }
}

