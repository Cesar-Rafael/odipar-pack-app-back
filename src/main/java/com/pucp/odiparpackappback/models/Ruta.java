package com.pucp.odiparpackappback.models;

import java.util.ArrayList;

public class Ruta {
    private static Long id = Long.valueOf(0);
    private Long idRuta;
    private OficinaModel ciudadActual;
    private ArrayList<Tramo> tramos;// [tramo1 (ciudadI, ciudadJ), tramo2(ciudadJ, ciudadK)]
    private ArrayList<Double> tiemposTramos; // [hora1, hora2]
    private ArrayList<Integer> ubigeoOficinas;
    private ArrayList<PedidoParcial> pedidosParciales;
    private Long horaInicio;
    private ArrayList<Long> horasDeLlegada;
    private String seguimiento;
    private Long idUnidadTransporte;
    private UnidadTransporteModel vehiculo;
    private double fitness;

    public Ruta(Long idRuta, String seguimiento, ArrayList<PedidoParcial> pedidosParciales, double fitness, Long idUnidadTransporte, ArrayList<Tramo> tramos) {
        this.idRuta = idRuta;                               // 3
        this.seguimiento = seguimiento;                     // "[150101, 150201, 21801, 130701, 140301]"
        this.pedidosParciales = pedidosParciales;           // [3, 15, 21, 130]
        this.fitness = fitness;                             // 13.8765
        this.idUnidadTransporte = idUnidadTransporte;       // 12
        this.tramos = tramos;
    }

    public Ruta(ArrayList<Integer> ubigeoOficinas, UnidadTransporteModel vehiculo, ArrayList<Double> tiemposTramos, double fitness, ArrayList<Tramo> tramos) {
        this.idRuta = id++;
        this.ubigeoOficinas = ubigeoOficinas;
        this.pedidosParciales = new ArrayList<>();
        this.vehiculo = vehiculo;
        this.tramos =  tramos;
        this.tiemposTramos =  tiemposTramos;
        this.fitness =  fitness;
    }

    public void calcularHorasDeLlegada() {
        horasDeLlegada = new ArrayList<>();
        long tiempoTramo = horaInicio;
        for (int i = 0; i < tiemposTramos.size(); i++) {
            if (i != 0) tiempoTramo += (long) (tiemposTramos.get(i) * 3600);
            horasDeLlegada.add(tiempoTramo);
        }
    }
    public void agregarPedidoParcial(PedidoParcial pedidoParcial) {
        pedidosParciales.add(pedidoParcial);
    }

    public static Long getId() {
        return id;
    }
    public static void setId(Long id) {
        Ruta.id = id;
    }

    public Long getIdRuta() {
        return idRuta;
    }
    public void setIdRuta(Long idRuta) {
        this.idRuta = idRuta;
    }
    public OficinaModel getCiudadActual() {
        return ciudadActual;
    }
    public void setCiudadActual(OficinaModel ciudadActual) {
        this.ciudadActual = ciudadActual;
    }
    public ArrayList<Tramo> getTramos() {
        return tramos;
    }
    public void setTramos(ArrayList<Tramo> tramos) {
        this.tramos = tramos;
    }
    public ArrayList<Double> getTiemposTramos() {
        return tiemposTramos;
    }
    public void setTiemposTramos(ArrayList<Double> tiemposTramos) {
        this.tiemposTramos = tiemposTramos;
    }
    public ArrayList<Integer> getUbigeoOficinas() {
        return ubigeoOficinas;
    }
    public void setUbigeoOficinas(ArrayList<Integer> ubigeoOficinas) {
        this.ubigeoOficinas = ubigeoOficinas;
    }
    public ArrayList<PedidoParcial> getPedidosParciales() {
        return pedidosParciales;
    }
    public void setPedidosParciales(ArrayList<PedidoParcial> pedidosParciales) {
        this.pedidosParciales = pedidosParciales;
    }
    public Long getHoraInicio() {
        return horaInicio;
    }
    public void setHoraInicio(Long horaInicio) {
        this.horaInicio = horaInicio;
    }
    public ArrayList<Long> getHorasDeLlegada() {
        return horasDeLlegada;
    }
    public void setHorasDeLlegada(ArrayList<Long> horasDeLlegada) {
        this.horasDeLlegada = horasDeLlegada;
    }
    public String getSeguimiento() {
        return seguimiento;
    }
    public void setSeguimiento(String seguimiento) {
        this.seguimiento = seguimiento;
    }
    public Long getIdUnidadTransporte() {
        return idUnidadTransporte;
    }
    public void setIdUnidadTransporte(Long idUnidadTransporte) {
        this.idUnidadTransporte = idUnidadTransporte;
    }
    public UnidadTransporteModel getVehiculo() {
        return vehiculo;
    }
    public void setVehiculo(UnidadTransporteModel vehiculo) {
        this.vehiculo = vehiculo;
    }
    public double getFitness() {
        return fitness;
    }
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}
