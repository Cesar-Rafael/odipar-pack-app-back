package com.pucp.odiparpackappback.dto;

import com.pucp.odiparpackappback.models.BloqueoModel;
import com.pucp.odiparpackappback.models.PedidoModel;

import java.util.ArrayList;
import java.util.Date;

public class Simulation {
    public ArrayList<PedidoModel> pedidos;
    //public ArrayList<BloqueoModel> bloqueos;
    public Date inicioSimulacion;
    public int velocidad;
}