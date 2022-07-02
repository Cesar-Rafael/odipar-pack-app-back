package com.pucp.odiparpackappback.dto;

import com.pucp.odiparpackappback.models.PedidoModel;

import java.util.ArrayList;
import java.util.Date;

public class Simulation {
    public ArrayList<PedidoModel> pedidos;
    public Date inicioSimulacion;
    public boolean primero;

    public boolean finalizado;
}