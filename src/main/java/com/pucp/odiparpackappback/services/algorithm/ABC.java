package com.pucp.odiparpackappback.services.algorithm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pucp.odiparpackappback.Repositories.PedidoRepository;
import com.pucp.odiparpackappback.controllers.PedidoController;
import com.pucp.odiparpackappback.models.*;
import com.pucp.odiparpackappback.services.utils.DatosUtil;
import com.pucp.odiparpackappback.topKshortestpaths.graph.Path;
import com.pucp.odiparpackappback.topKshortestpaths.graph.abstraction.BaseVertex;
import com.pucp.odiparpackappback.topKshortestpaths.graph.shortestpaths.YenTopKShortestPathsAlg;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class ABC {
    Timer timer = new Timer();
    class task extends TimerTask {
        private final PedidoModel pedido;

        task ( PedidoModel pedido )
        {
            this.pedido = pedido;
        }

        public void run() {
            pedido.setEstado(EstadoPedido.ENTREGADO);
        }
    }
    class task2 extends TimerTask {
        private final UnidadTransporteModel vehiculo;

        task2 ( UnidadTransporteModel vehiculo )
        {
            this.vehiculo = vehiculo;
        }

        public void run() {
            vehiculo.setEstado(EstadoUnidadTransporte.DISPONIBLE);
        }
    }
    public void algoritmoAbejasVPRTW(int opcion) {
        // Opcion 0 - Simulacion
        // Opcion 1 - DiaDia

        ArrayList<PedidoModel> pedidos;
        if (opcion == 0) {
            // "pedidos" se utilizará para el algoritmo
            pedidos = Mapa.pedidosSimulacion;
        } else {
            // Para las operaciones Día a Día los Pedidos se leen desde la BD
            Mapa.cargarPedidosDiaDia();
            // "pedidos" se utilizará para el algoritmo
            pedidos = Mapa.pedidosDiaDia;
        }

        // En caso no haya pedidos...
        if (pedidos.size() == 0) {
            System.out.println("No hay pedidos que asignar...");
            return;
        }

        // Asignación de Pedidos
        for (int z = 0; z < pedidos.size(); z++) {
            if (pedidos.get(z).getEstado() == EstadoPedido.NO_ASIGNADO) {
                asignarPedidoPoblacionInicial(pedidos.get(z), opcion);
            }
        }

        // Se guardan las rutas en BD y se actualizan los Pedidos
        if (opcion == 0) {
            Mapa.pedidosSimulacion = pedidos;
            for(int www = 0; www < Mapa.vehiculosSimulacion.size(); www++){
                if(Mapa.vehiculosSimulacion.get(www).getEstado() == EstadoUnidadTransporte.RESERVADO){
                    Mapa.vehiculosSimulacion.get(www).setEstado(EstadoUnidadTransporte.EN_TRANSITO);
                }
            }
        } else {
            ArrayList<RutaModel> rutasAux = new ArrayList<>();
            for (int rm = 0; rm < Mapa.rutasDiaDia.size(); rm++) {
                RutaModel rutaAux = new RutaModel();
                rutaAux.setIdRuta(Mapa.rutasDiaDia.get(rm).getIdRuta());
                rutaAux.setSeguimiento(Mapa.rutasDiaDia.get(rm).getSeguimiento());
                rutaAux.setIdUnidadTransporte(Mapa.rutasDiaDia.get(rm).getIdUnidadTransporte());
                rutasAux.add(rutaAux);
            }
            Mapa.cargarRutas(rutasAux);
            Mapa.pedidosDiaDia = pedidos;
        }
    }

    public boolean asignarPedidoPoblacionInicial(PedidoModel pedido, int opcion) {
        ArrayList<Ruta> rutas;
        ArrayList<UnidadTransporteModel> vehiculos;
        LocalDateTime fin;

        // División DIADIA - Simulación
        if (opcion == 0) {
            rutas = Mapa.rutasSimulacion;
            vehiculos = Mapa.vehiculosSimulacion;
            fin = Mapa.inicioSimulacion;
        } else {
            rutas = Mapa.rutasDiaDia;
            vehiculos = Mapa.vehiculosDiaDia;
            fin = Mapa.finDiaDia;
        }

        boolean asignado;
        ZoneId zoneId = ZoneId.systemDefault();

        // Para cada Ruta ya creada...
        for (int a = 0; a < rutas.size(); a++) {
            // Actualización de Estado de Rutas
            if (rutas.get(a).getHorasDeLlegada().get(rutas.get(a).getHorasDeLlegada().size() - 1) < fin.atZone(zoneId).toEpochSecond()) {
                rutas.get(a).setFlagTerminado(true);
                vehiculos.get(Math.toIntExact(rutas.get(a).getIdUnidadTransporte())).setEstado(EstadoUnidadTransporte.DISPONIBLE);
            }

            // Se verifica si el Pedido puede ser asignado a esa Ruta...
            asignado = asignarPedidoRutaVehiculo(pedido, rutas.get(a), opcion);
            if (asignado) {
                // Fue asignado correctamente
                if (opcion == 0) {
                    Mapa.rutasSimulacion = rutas;
                    Mapa.vehiculosSimulacion = vehiculos;
                    Mapa.inicioSimulacion = fin;
                } else {
                    Mapa.rutasDiaDia = rutas;
                    Mapa.vehiculosDiaDia = vehiculos;
                    Mapa.finDiaDia = fin;
                }
                return true;
            }
            // Si no puede ser asignado, se revisa la siguiente Ruta...
        }

        // Si no puede ser asignado a alguna Ruta... se crea una nueva y se inserta al arreglo de rutas
        boolean bool = kShortestPathRoutingPedido(pedido, 0, opcion);
        Region auxRegion = Mapa.oficinas.get(0).getRegion();
        if (!bool) {
            // Si el PEDIDO puede llegar aún a tiempo...
            if (fin.isBefore(LocalDateTime.ofInstant(pedido.getFechaHoraCreacion().toInstant(), zoneId).plusDays(auxRegion.getCode() + 1))) {
                // Se obtiene el nombre de la región
                for (int z = 0; z < Mapa.oficinas.size(); z++) {
                    if (Mapa.oficinas.get(z).getUbigeo() == pedido.getIdCiudadDestino()) {
                        auxRegion = Mapa.oficinas.get(z).getRegion();
                        break;
                    }
                }

                // Local Principal más cercano al destino del Pedido
                ArrayList<Path> rutasPath = YenTopKShortestPathsAlg.getKShortestPaths(0 + 1, pedido.getIdCiudadDestino(), null);

                // Si alcanza tiempo, asignar a Vehículo más cercano cuyo fin de ruta sea el inicio de la recién creada
                int iMenor = -1;    // Inicialización
                String seguimiento = rutasPath.get(0).getVertexList().toString();
                List<Integer> listaSeg2 = new ArrayList<>();
                boolean iPrimero = false;
                for (int i = 0; i < rutas.size(); i++) {
                    List<Integer> listaSeg = new ArrayList<>();
                    try {
                        listaSeg = new ObjectMapper().reader(List.class).readValue(rutas.get(i).getSeguimiento());
                        listaSeg2 = new ObjectMapper().reader(List.class).readValue(seguimiento);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }

                    // Si el último UBIGEO del seguimiento es igual al origen de la ruta del pedido y ... el Vehiculo puede ser asignado: NO ESTÁ RESERVADO
                    if (listaSeg.get(0).equals(listaSeg2.get(0)) && !(vehiculos.get(Math.toIntExact(rutas.get(i).getIdUnidadTransporte())).getEstado().getCode() == 1)) {
                        if (iPrimero == false) {
                            iMenor = i;
                        } else {
                            if (rutas.get(iMenor).getHorasDeLlegada().get(rutas.get(iMenor).getHorasDeLlegada().size() - 1) > rutas.get(i).getHorasDeLlegada().get(rutas.get(i).getHorasDeLlegada().size() - 1)) {
                                iMenor = i;
                            }
                        }
                    }
                }

                //
                if (iMenor == -1) {
                    System.out.println();
                    System.out.println("HAY PEDIDOS PENDIENTES DE ENTREGA..." + pedido.getId());
                    System.out.println();
                    if (opcion == 0) {
                        Mapa.rutasSimulacion = rutas;
                        Mapa.vehiculosSimulacion = vehiculos;
                        Mapa.inicioSimulacion = fin;
                    } else {
                        Mapa.rutasDiaDia = rutas;
                        Mapa.vehiculosDiaDia = vehiculos;
                        Mapa.finDiaDia = fin;
                    }
                    return true;
                }

                // En este punto, tengo el vehiculo que voy a seleccionar y la ruta que se tomará
                Long idVehiculoEscogido = rutas.get(iMenor).getIdUnidadTransporte();

                // Se agrega una nueva ruta en rutasSimulacion
                Long idRuta = Long.valueOf(rutas.size());
                ArrayList<PedidoParcialModel> pedidosParciales = new ArrayList<>();

                // CREACION DE PEDIDO PARCIAL
                double fitness = rutasPath.get(0).getWeight();
                ArrayList<Long> horasLlegadaLong = new ArrayList<>();
                List<BaseVertex> oficinas = rutasPath.get(0).getVertexList();
                ArrayList<LocalDateTime> horasLlegada = new ArrayList<>();

                // REGRESO
                List<Path> regresoPath = YenTopKShortestPathsAlg.getShortestPathsReturn(rutasPath.get(0).getVertexList().get(rutasPath.get(0).getVertexList().size() - 1).getId());
                int ganador = 1;
                for (int p = 0; p < regresoPath.size(); p++) {
                    if (regresoPath.get(p).getWeight() < regresoPath.get(ganador).getWeight()) ganador = p;
                }
                regresoPath.get(ganador).getVertexList().remove(0);
                List<BaseVertex> oficinasRegreso = regresoPath.get(ganador).getVertexList();
                for (int or = 0; or < oficinasRegreso.size(); or++) {
                    oficinas.add(oficinasRegreso.get(or));
                }
                seguimiento = oficinas.toString();
                ArrayList<TramoModel> tramos = Mapa.listarTramos(seguimiento);

                for (int i = 0; i < oficinas.size(); i++) {
                    if (i == 0) {
                        horasLlegadaLong.add(rutas.get(iMenor).getHorasDeLlegada().get(rutas.get(iMenor).getHorasDeLlegada().size() - 1) + 3600);
                        horasLlegada.add(LocalDateTime.ofInstant(Instant.ofEpochSecond(rutas.get(iMenor).getHorasDeLlegada().get(rutas.get(iMenor).getHorasDeLlegada().size() - 1) + 3600), zoneId));
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
                if (opcion == 0) {
                    Mapa.rutasSimulacion.get(iMenor).setFlagTerminado(true);
                    Mapa.vehiculosSimulacion.get(Math.toIntExact(rutas.get(iMenor).getIdUnidadTransporte())).setEstado(EstadoUnidadTransporte.RESERVADO);
                    //timer.schedule(new task2(Mapa.vehiculosSimulacion.get(Math.toIntExact(rutas.get(iMenor).getIdUnidadTransporte()))), horasLlegadaLong.get(horasLlegada.size()-1));
                } else {
                    Mapa.rutasDiaDia.get(iMenor).setFlagTerminado(true);
                    Mapa.vehiculosDiaDia.get(Math.toIntExact(rutas.get(iMenor).getIdUnidadTransporte())).setEstado(EstadoUnidadTransporte.RESERVADO);
                    timer.schedule(new task2(Mapa.vehiculosDiaDia.get(Math.toIntExact(rutas.get(iMenor).getIdUnidadTransporte()))), horasLlegadaLong.get(horasLlegada.size()-1) - Mapa.finDiaDia.atZone(zoneId).toEpochSecond() );
                }

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
                if(Mapa.vehiculosSimulacion.get(Math.toIntExact(rutas.get(iMenor).getIdUnidadTransporte())).getCapacidadTotal() < pedido.getCantPaquetesNoAsignado()){
                    PedidoParcialModel pedidoParcial = new PedidoParcialModel(0L, pedido.getId(), -1, Mapa.vehiculosSimulacion.get(Math.toIntExact(rutas.get(iMenor).getIdUnidadTransporte())).getCapacidadTotal(), horasLlegadaLong.get(indiceAux), idRuta);
                    pedido.setCantPaquetesNoAsignado(0);
                    pedidosParciales.add(pedidoParcial);
                }
                else{
                    PedidoParcialModel pedidoParcial = new PedidoParcialModel(0L, pedido.getId(), -1, pedido.getCantPaquetesNoAsignado(), horasLlegadaLong.get(indiceAux), idRuta);
                    pedido.setCantPaquetesNoAsignado(0);
                    pedidosParciales.add(pedidoParcial);
                }
                if(opcion == 1)timer.schedule(new task(pedido), horasLlegadaLong.get(indiceAux));

                // SE CREA UNA NUEVA RUTA
                Ruta rutaAux = new Ruta(idRuta, seguimiento, pedidosParciales, fitness, idVehiculoEscogido, tramos, horasLlegadaLong);
                if (opcion == 0 && (pedidosParciales.get(0).getCantPaquetes()!=0)){
                    Mapa.rutasSimulacion.add(rutaAux);
                }
                else{
                    if(pedidosParciales.get(0).getCantPaquetes()!=0){
                        Mapa.rutasDiaDia.add(rutaAux);
                    }
                }

                // Actualización
                if (opcion == 0) {
                    Mapa.rutasSimulacion = rutas;
                    Mapa.vehiculosSimulacion.get(Math.toIntExact(idVehiculoEscogido)).setEstado(EstadoUnidadTransporte.EN_TRANSITO);
                    Mapa.vehiculosSimulacion = vehiculos;
                    Mapa.inicioSimulacion = fin;
                } else {
                    Mapa.rutasDiaDia = rutas;
                    Mapa.vehiculosDiaDia.get(Math.toIntExact(idVehiculoEscogido)).setEstado(EstadoUnidadTransporte.EN_TRANSITO);
                    Mapa.vehiculosDiaDia = vehiculos;
                    Mapa.finDiaDia = fin;
                }
                return true;
            } else {
                // Si no alcanza tiempo, es colapso logístico
                System.out.println("¡Colapso Logistico!");
                Mapa.flagColapso = true;
                return false;
            }
        }
        // es falso, si ya no se puede crear más rutas
        return bool;
    }

    public boolean kShortestPathRoutingPedido(PedidoModel pedido, int k, int opcion) {
        // AQUÍ SE CREA LA PRIMERA RUTA, VEHÍCULOS DEBEN ENTRAR A ESTADO RESERVADO = 1
        ArrayList<Ruta> rutas;
        ArrayList<UnidadTransporteModel> vehiculos;
        LocalDateTime inicio;

        // Carga de datos...
        if (opcion == 0) {
            rutas = Mapa.rutasSimulacion;
            inicio = Mapa.inicioSimulacion;
            vehiculos = Mapa.vehiculosSimulacion;
        } else {
            rutas = Mapa.rutasDiaDia;
            inicio = Mapa.finDiaDia;
            vehiculos = Mapa.vehiculosDiaDia;
        }

        // si k es 0, es la mejor ruta, si es 1, la segunda mejor ruta...
        ArrayList<Path> rutasPath = YenTopKShortestPathsAlg.getKShortestPaths(k + 1, pedido.getIdCiudadDestino(), null);
        Long idRuta = Long.valueOf(rutas.size());

        // rutasPath es la Ruta creada
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
        double fitness = rutasPath.get(k).getWeight();

        // Para cada Oficina...
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

        // Para cada Vehículo...
        for (int i = 0; i < vehiculos.size(); i++) {
            // Sí el vehículo está disponible...
            if (vehiculos.get(i).getEstado() == EstadoUnidadTransporte.DISPONIBLE && vehiculos.get(i).getOficinaActual() == rutasPath.get(k).getVertexList().get(0).getId()) {
                // ¿Hay capacidad disponible suficiente?
                if (vehiculos.get(i).getCapacidadDisponible() >= pedido.getCantPaquetesNoAsignado()) {
                    // Sí hay capacidad disponible suficiente
                    if (opcion == 0) {
                        Mapa.vehiculosSimulacion.get(i).setEstado(EstadoUnidadTransporte.RESERVADO);
                        Mapa.vehiculosSimulacion.get(i).setIdRuta(idRuta);
                        Mapa.vehiculosSimulacion.get(i).setCapacidadDisponible(vehiculos.get(i).getCapacidadDisponible() - pedido.getCantPaquetesNoAsignado());
                    } else {
                        Mapa.vehiculosDiaDia.get(i).setEstado(EstadoUnidadTransporte.RESERVADO);
                        Mapa.vehiculosDiaDia.get(i).setIdRuta(idRuta);
                        Mapa.vehiculosDiaDia.get(i).setCapacidadDisponible(vehiculos.get(i).getCapacidadDisponible() - pedido.getCantPaquetesNoAsignado());
                        timer.schedule(new task2(Mapa.vehiculosDiaDia.get(i)), horasLlegadaLong.get(horasLlegada.size()-1) - Mapa.finDiaDia.atZone(zoneId).toEpochSecond());
                    }
                    // Asignación Ruta
                    Long idUnidadTransporte = vehiculos.get(i).getId();
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
                    if(opcion ==1) timer.schedule(new task(pedido), horasLlegadaLong.get(indiceAux));

                    // Asignación
                    ArrayList<TramoModel> tramos = Mapa.listarTramos(seguimiento);
                    for (int a = 0; a < tramos.size(); a++) {
                        tramos.get(a).setTiempoDeViaje(DatosUtil.calcularTiempoViajeEntreTramos(tramos.get(a).getIdCiudadI(), tramos.get(a).getIdCiudadJ()) * 3600);
                    }
                    Ruta auxRuta = new Ruta(idRuta, seguimiento, pedidosParciales, fitness, idUnidadTransporte, tramos, horasLlegadaLong);
                    if (opcion == 0 && (pedidosParciales.get(0).getCantPaquetes()!=0)){
                        Mapa.rutasSimulacion.add(auxRuta);
                    }
                    else{
                        if(pedidosParciales.get(0).getCantPaquetes()!=0){
                            Mapa.rutasDiaDia.add(auxRuta);
                        }
                    }
                    return true;
                } else {
                    // Actualización en Pedido
                    int faltante = pedido.getCantPaquetesNoAsignado() - vehiculos.get(i).getCapacidadDisponible();
                    // No hay capacidad disponible suficiente
                    if (opcion == 0) {
                        Mapa.vehiculosSimulacion.get(i).setEstado(EstadoUnidadTransporte.RESERVADO);
                        Mapa.vehiculosSimulacion.get(i).setIdRuta(idRuta);
                    } else {
                        Mapa.vehiculosDiaDia.get(i).setEstado(EstadoUnidadTransporte.RESERVADO);
                        Mapa.vehiculosDiaDia.get(i).setIdRuta(idRuta);
                        timer.schedule(new task2(Mapa.vehiculosDiaDia.get(i)), horasLlegadaLong.get(horasLlegada.size()-1) - Mapa.finDiaDia.atZone(zoneId).toEpochSecond());
                    }

                    // Asignación Ruta
                    Long idUnidadTransporte = vehiculos.get(i).getId();
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
                    PedidoParcialModel pedidoParcial = new PedidoParcialModel(0L, pedido.getId(), -1, vehiculos.get(i).getCapacidadDisponible(), horasLlegadaLong.get(indiceAux), idRuta);
                    pedidosParciales.add(pedidoParcial);

                    // Pedido
                    if (opcion == 0) Mapa.vehiculosSimulacion.get(i).setCapacidadDisponible(0);
                    else Mapa.vehiculosDiaDia.get(i).setCapacidadDisponible(0);
                    pedido.setCantPaquetesNoAsignado(faltante);
                    pedido.setEstado(EstadoPedido.EN_PROCESO);
                    if(opcion == 1) timer.schedule(new task(pedido), horasLlegadaLong.get(indiceAux));
                    // Asignación
                    ArrayList<TramoModel> tramos = Mapa.listarTramos(seguimiento);
                    for (int a = 0; a < tramos.size(); a++) {
                        tramos.get(a).setTiempoDeViaje(DatosUtil.calcularTiempoViajeEntreTramos(tramos.get(a).getIdCiudadI(), tramos.get(a).getIdCiudadJ()) * 3600);
                    }
                    Ruta auxRuta = new Ruta(idRuta, seguimiento, pedidosParciales, fitness, idUnidadTransporte, tramos, horasLlegadaLong);
                    if (opcion == 0 && (pedidosParciales.get(0).getCantPaquetes()!=0)){
                        Mapa.rutasSimulacion.add(auxRuta);
                    }
                    else{
                        if(pedidosParciales.get(0).getCantPaquetes()!=0){
                            Mapa.rutasDiaDia.add(auxRuta);
                        }
                    }
                    return kShortestPathRoutingPedido(pedido, 0, opcion);
                }
            }
        }
        return false;
    }

    public boolean asignarPedidoRutaVehiculo(PedidoModel pedido, Ruta ruta, int opcion) {
        // SE ASIGNA PEDIDO A RUTA YA CREADA, EL VEHÍCULO DE ESA RUTA DEBE ESTAR RESERVADO
        ArrayList<UnidadTransporteModel> vehiculos;
        if (opcion == 0) {
            vehiculos = Mapa.vehiculosSimulacion;
        } else {
            vehiculos = Mapa.vehiculosDiaDia;
        }

        // Se verifica si la Ruta tiene la ciudad de destino deseado...
        boolean encontrado = false;
        for (int i = 0; i < ruta.getTramos().size(); i++) {
            if ((ruta.getTramos().get(i).getIdCiudadJ() == pedido.getIdCiudadDestino()) && (vehiculos.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() != 0)) {
                encontrado = true;
                break;
            }
        }

        // ¿La Ruta tiene la misma Ciudad de Destino?
        if (!encontrado) {
            // Si la ruta no tiene el destino, no es asignado
            return false;
        } else {
            // Se verifica si hay capacidad disponible suficiente en el Vehículo asignado a esa ruta y está RESERVADO
            if ((vehiculos.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() > pedido.getCantPaquetesNoAsignado()) && (vehiculos.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getEstado().getCode() == 1) ) {
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
                if (opcion == 0)
                    Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).setCapacidadDisponible(Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() - pedido.getCantPaquetesNoAsignado());
                else
                    Mapa.vehiculosDiaDia.get(Math.toIntExact(ruta.getIdUnidadTransporte())).setCapacidadDisponible(Mapa.vehiculosDiaDia.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible() - pedido.getCantPaquetesNoAsignado());
                // Se actualiza la cantidad de paquetes no asignados
                pedido.setCantPaquetesNoAsignado(0);
                pedido.setEstado(EstadoPedido.EN_PROCESO);
                if(opcion == 1) timer.schedule(new task(pedido), horasLlegadaLong.get(indiceAux));
                return true;
            } else if ((Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getEstado().getCode() == 1)) {
                // No hay capacidad suficiente, se asigna el pedido a la ruta y luego se busca otra para completar el pedido
                int faltante = pedido.getCantPaquetesNoAsignado() - vehiculos.get(Math.toIntExact(ruta.getIdUnidadTransporte())).getCapacidadDisponible();
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
                if (opcion == 0)
                    Mapa.vehiculosSimulacion.get(Math.toIntExact(ruta.getIdUnidadTransporte())).setCapacidadDisponible(0);
                else Mapa.vehiculosDiaDia.get(Math.toIntExact(ruta.getIdUnidadTransporte())).setCapacidadDisponible(0);
                pedido.setCantPaquetesNoAsignado(faltante);
                return false;
            } else {
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
}