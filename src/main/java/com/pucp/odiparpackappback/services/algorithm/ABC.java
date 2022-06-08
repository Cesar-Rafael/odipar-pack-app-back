package com.pucp.odiparpackappback.services.algorithm;

import com.pucp.odiparpackappback.models.*;
import com.pucp.odiparpackappback.services.utils.DatosUtil;
import com.pucp.odiparpackappback.topKshortestpaths.graph.Path;
import com.pucp.odiparpackappback.topKshortestpaths.graph.abstraction.BaseVertex;
import com.pucp.odiparpackappback.topKshortestpaths.graph.shortestpaths.YenTopKShortestPathsAlg;
import com.pucp.odiparpackappback.topKshortestpaths.utils.Pair;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ABC {

    public void algoritmoAbejasVPRTW(int numAbejasObr, int numAbejasObs, int numGen) {
        //System.out.println(Mapa.inicioSimulacion);
        //System.out.println(Mapa.finSimulacion);

        Mapa.cargarPedidos(obtenerFecha(Mapa.inicioSimulacion), obtenerFecha(Mapa.finSimulacion));

        Mapa.inicioSimulacion = Mapa.inicioSimulacion.plusMinutes(90);
        Mapa.finSimulacion = Mapa.finSimulacion.plusMinutes(90);

        if (Mapa.pedidos.size() == 0) {
            System.out.println("No hay pedidos que asignar");
            return;
        }

        int contador = 0;
        // Etapa: Generación de la Población Inicial
        while (true) {
            int i = generarNumeroEnteroAleatorio(Mapa.pedidos.size());
            // En caso el pedido escogido al azar no esté asignado...
            if (Mapa.pedidos.get(i).getEstado() == EstadoPedido.NO_ASIGNADO) {
                boolean estado = asignarPedidoPoblacionInicial(Mapa.pedidos.get(i));
                if (!estado) {
                    break;
                }
                contador++;
                if (contador == Mapa.pedidos.size()) {
                    break;
                }
            }
        }

        // Etapa: Mejora de la Población Inicial de acuerdo al número de generaciones
        for (int a = 0; a < numGen; a++) {
            // Abeja Obrera
            for (int b = 0; b < numAbejasObr; b++) {
                int i = generarNumeroEnteroAleatorio(Mapa.rutas.size());
                // Abeja Observadora
                for (int c = 0; b < numAbejasObs; b++) {
                    //Se buscará numAbejasObs vecinas a la ruta i, en caso algún vecino tenga mejor fitness, este lo reemplazará en el arreglo de rutas
                    Ruta auxRuta = kShortestPathRoutingRuta(Mapa.rutas.get(i), c + 1, null);
                    //Si la ruta vecina tiene un mejor fitness, lo reemplazará, si no pasamos al siguiente
                    if (auxRuta.getFitness() > Mapa.rutas.get(i).getFitness()) {
                        Mapa.rutas.set(i, auxRuta);
                    }
                }
            }
        }

        Mapa.bloqueos = new ArrayList<>();

        // Llamado a InsertarListaRutas
        ArrayList<RutaModel> rutasAux = new ArrayList<>();
        for (int rm = 0; rm < Mapa.rutas.size(); rm++) {
            RutaModel rutaAux = new RutaModel();
            rutaAux.setIdRuta(Mapa.rutas.get(rm).getIdRuta());
            rutaAux.setSeguimiento(Mapa.rutas.get(rm).getSeguimiento());
            rutaAux.setIdUnidadTransporte(Mapa.rutas.get(rm).getIdUnidadTransporte());

            ArrayList<Long> list = Mapa.rutas.get(rm).getHorasDeLlegada();
            System.out.println(list);

            StringBuilder listString = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                listString.append(list.get(i));
                if (i != list.size() - 1) listString.append(",");
            }

            rutaAux.setArrayHorasLlegada(listString.toString());
            rutasAux.add(rutaAux);
        }
        Mapa.cargarRutas(rutasAux);

    }

    public int generarNumeroEnteroAleatorio(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public boolean asignarPedidoPoblacionInicial(PedidoModel pedido) {
        // Se asigna el pedido actual a la población inicial...
        boolean asignado;
        for (int a = 0; a < Mapa.rutas.size(); a++) {

            // Se verifica si el pedido puede ser asignado a esa ruta
            asignado = asignarPedidoRutaVehiculo(pedido, Mapa.rutas.get(a));
            if (asignado) {
                // Fue asignado correctamente
                return true;
            }
            // Si no puede ser asignado, se revisa la siguiente ruta...
        }
        // Si no puede ser asignado a alguna ruta... se crea una nueva y se inserta al arreglo de rutas
        boolean bool = kShortestPathRoutingPedido(pedido, 0);
        if (!bool) {
            System.out.println("¡No hay camiones para asignar más pedidos!");
        }
        // es falso, si ya no se puede crear más rutas
        return bool;
    }

    public Ruta kShortestPathRoutingRuta(Ruta rutaOriginal, int k, Pair<Integer, Integer> edge) {
        int ubigeoDestino = rutaOriginal.getTramos().get(rutaOriginal.getTramos().size() - 1).getIdCiudadJ();
        // si k es 0, es la mejor ruta, si es 1, la segunda mejor ruta...
        ArrayList<Path> rutasPath = YenTopKShortestPathsAlg.getKShortestPaths(k, ubigeoDestino, edge);

        List<BaseVertex> oficinas = rutasPath.get(k).getVertexList();
        //ArrayList<Long> horasLlegada = new ArrayList<>();
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
                int minutos = (int) (tiempoViaje - 1.0 * horas) * 60;
                LocalDateTime horaLlegada;
                horaLlegada = Mapa.inicioSimulacion.plusHours(horas);

                /*
                if (i == 1) {
                    horaLlegada = Mapa.inicioSimulacion.plusHours(horas);
                } else {
                    horaLlegada = Mapa.inicioSimulacion.plusHours(horas + 1);
                }*/

                horaLlegada = horaLlegada.plusMinutes(minutos);
                horasLlegadaLong.add(horaLlegada.atZone(zoneId).toEpochSecond());
                horasLlegada.add(horaLlegada);
            }
        }

        Ruta rutaEncontrada = null;

        for (int i = 0; i < horasLlegada.size() - 1; i++) {
            int oficinaI = oficinas.get(i).getId();
            int oficinaJ = oficinas.get(i + 1).getId();
            List<BloqueoModel> bloqueos = Mapa.obtenerTramosBloqueados(oficinaI, oficinaJ, obtenerFecha(horasLlegada.get(i)), obtenerFecha(horasLlegada.get(i + 1)));
            //List<BloqueoModel> bloqueos = Mapa.obtenerTramosBloqueados(oficinaI, oficinaJ, obtenerFecha(horasLlegada.get(i).minusHours(6)), obtenerFecha(horasLlegada.get(i + 1).minusHours(6)));
            if (bloqueos.size() > 0) {
                System.out.println("Bloqueo Encontrado");
                Pair tramoBloqueado = new Pair<Integer, Integer>(oficinaI, oficinaJ);
                YenTopKShortestPathsAlg.graph.deleteEdge(tramoBloqueado);
                Mapa.bloqueos.add(tramoBloqueado);
                rutaEncontrada = kShortestPathRoutingRuta(rutaOriginal, k, tramoBloqueado);
                //YenTopKShortestPathsAlg.graph.deleteEdge(tramoBloqueado);
                //YenTopKShortestPathsAlg.graph.recoverDeletedEdge(tramoBloqueado);
            }
        }

        if (rutaEncontrada != null) return rutaEncontrada;

        // String seguimiento
        String seguimiento = rutasPath.get(k).getVertexList().toString();
        ArrayList<TramoModel> tramos = Mapa.listarTramos(seguimiento);
        // double fitness
        double fitness = rutasPath.get(k).getWeight();
        // Asignación
        Ruta ruta = new Ruta(rutaOriginal.getIdRuta(), seguimiento, rutaOriginal.getPedidosParciales(), fitness, rutaOriginal.getIdUnidadTransporte(), tramos, horasLlegadaLong);
        return ruta;
    }

    public boolean kShortestPathRoutingPedido(PedidoModel pedido, int k) {
        // si k es 0, es la mejor ruta, si es 1, la segunda mejor ruta...
        ArrayList<Path> rutasPath = YenTopKShortestPathsAlg.getKShortestPaths(k + 1, pedido.getIdCiudadDestino(), null);
        // Parámetros
        Long idRuta = Long.valueOf(Mapa.rutas.size());
        String seguimiento = rutasPath.get(k).getVertexList().toString();
        ArrayList<PedidoParcialModel> pedidosParciales = new ArrayList<>();

        List<BaseVertex> oficinas = rutasPath.get(k).getVertexList();
        //ArrayList<Long> horasLlegada = new ArrayList<>();
        ArrayList<Long> horasLlegadaLong = new ArrayList<>();
        ZoneId zoneId = ZoneId.systemDefault();

        for (int i = 0; i < oficinas.size(); i++) {
            if (i == 0) {
                horasLlegadaLong.add(Mapa.inicioSimulacion.atZone(zoneId).toEpochSecond());
            } else {
                double tiempoViaje = findTiempoViaje(oficinas.get(i - 1).getId(), oficinas.get(i).getId());
                int horas = (int) Math.floor(tiempoViaje);
                int minutos = (int) (tiempoViaje - 1.0 * horas) * 60;
                LocalDateTime horaLlegada;
                horaLlegada = Mapa.inicioSimulacion.plusHours(horas);

                /*
                if (i == 1) {
                    horaLlegada = Mapa.inicioSimulacion.plusHours(horas);
                } else {
                    horaLlegada = Mapa.inicioSimulacion.plusHours(horas + 1);
                }*/

                horaLlegada = horaLlegada.plusMinutes(minutos);
                horasLlegadaLong.add(horaLlegada.atZone(zoneId).toEpochSecond());
            }
        }

        double fitness = rutasPath.get(k).getWeight();
        // Asignación Vehículo
        for (int i = 0; i < Mapa.vehiculos.size(); i++) {
            // El vehículo está disponible
            if (Mapa.vehiculos.get(i).getEstado() == EstadoUnidadTransporte.DISPONIBLE) {
                // ¿Hay capacidad disponible suficiente?
                if (Mapa.vehiculos.get(i).getCapacidadDisponible() >= pedido.getCantPaquetesNoAsignado()) {
                    // Sí hay capacidad disponible suficiente
                    Mapa.vehiculos.get(i).setEstado(EstadoUnidadTransporte.EN_TRANSITO);
                    Mapa.vehiculos.get(i).setIdRuta(idRuta);
                    Mapa.vehiculos.get(i).setCapacidadDisponible(Mapa.vehiculos.get(i).getCapacidadDisponible() - pedido.getCantPaquetesNoAsignado());
                    // Asignación Ruta
                    Long idUnidadTransporte = Mapa.vehiculos.get(i).getId();
                    PedidoParcialModel pedidoParcial = new PedidoParcialModel(pedido.getId(), -1, pedido.getCantPaquetesNoAsignado(), 0L, idRuta);
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
                    Mapa.rutas.add(auxRuta);
                    return true;
                } else {
                    // Actualización en Pedido
                    int faltante = pedido.getCantPaquetesNoAsignado() - Mapa.vehiculos.get(i).getCapacidadDisponible();
                    // No hay capacidad disponible suficiente
                    Mapa.vehiculos.get(i).setEstado(EstadoUnidadTransporte.EN_TRANSITO);
                    Mapa.vehiculos.get(i).setIdRuta(idRuta);
                    // Asignación Ruta
                    Long idUnidadTransporte = Mapa.vehiculos.get(i).getId();
                    PedidoParcialModel pedidoParcial = new PedidoParcialModel(pedido.getId(), -1, Mapa.vehiculos.get(i).getCapacidadDisponible(), 0L, idRuta);
                    pedidosParciales.add(pedidoParcial);
                    // Pedido
                    Mapa.vehiculos.get(i).setCapacidadDisponible(0);
                    pedido.setCantPaquetesNoAsignado(faltante);
                    pedido.setEstado(EstadoPedido.EN_PROCESO);
                    // Asignación
                    ArrayList<TramoModel> tramos = Mapa.listarTramos(seguimiento);
                    for (int a = 0; a < tramos.size(); a++) {
                        tramos.get(a).setTiempoDeViaje(DatosUtil.calcularTiempoViajeEntreTramos(tramos.get(a).getIdCiudadI(), tramos.get(a).getIdCiudadJ()) * 3600);
                    }
                    Ruta auxRuta = new Ruta(idRuta, seguimiento, pedidosParciales, fitness, idUnidadTransporte, tramos, horasLlegadaLong);
                    Mapa.rutas.add(auxRuta);
                    return kShortestPathRoutingPedido(pedido, 0);
                }
            }
        }
        // No pudo ser asignado a ningún vehículo
        return false;
    }

    public boolean asignarPedidoRutaVehiculo(PedidoModel pedido, Ruta ruta) {
        boolean encontrado = false;
        for (int i = 0; i < ruta.getTramos().size(); i++) {
            if ((ruta.getTramos().get(i).getIdCiudadJ() == pedido.getIdCiudadDestino()) && (Mapa.vehiculos.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() != 0)) {
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
            if (Mapa.vehiculos.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() > pedido.getCantPaquetesNoAsignado()) {
                // Hay capacidad suficiente, se asigna el pedido a la ruta...
                ArrayList<PedidoParcialModel> pedidosParciales = ruta.getPedidosParciales();
                PedidoParcialModel pedidoParcial = new PedidoParcialModel(pedido.getId(), -1, pedido.getCantPaquetesNoAsignado(), 0L, ruta.getIdRuta());
                pedidosParciales.add(pedidoParcial);
                ruta.setPedidosParciales(pedidosParciales);
                // Se actualiza la capacidad disponible en el vehículo
                Mapa.vehiculos.get(Math.toIntExact(ruta.getIdUnidadTransporte())).setCapacidadDisponible(Mapa.vehiculos.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() - pedido.getCantPaquetesNoAsignado());
                // Se actualiza la cantidad de paquetes no asignados
                pedido.setCantPaquetesNoAsignado(0);
                pedido.setEstado(EstadoPedido.EN_PROCESO);
                return true;
            } else {
                // No hay capacidad suficiente, se asigna el pedido a la ruta y luego se busca otra para completar el pedido
                int faltante = pedido.getCantPaquetesNoAsignado() - Mapa.vehiculos.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible();
                // Asignación parcial
                ArrayList<PedidoParcialModel> pedidosParciales = ruta.getPedidosParciales();
                PedidoParcialModel pedidoParcial = new PedidoParcialModel(pedido.getId(), -1, pedido.getCantPaquetes() - faltante, 0L, ruta.getIdRuta());
                pedidosParciales.add(pedidoParcial);
                ruta.setPedidosParciales(pedidosParciales);
                // Se actualiza la capacidad disponible en el vehículo, o sea cero
                Mapa.vehiculos.get(Math.toIntExact(ruta.getIdUnidadTransporte())).setCapacidadDisponible(0);
                pedido.setCantPaquetesNoAsignado(faltante);
                return false;
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
        //System.out.println(fecha);
        Date nuevaFecha = Date.from(fecha.atZone(ZoneId.systemDefault()).toInstant());
        //System.out.println(nuevaFecha);
        return nuevaFecha;
    }
}
