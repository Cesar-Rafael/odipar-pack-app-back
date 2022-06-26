package com.pucp.odiparpackappback;

import com.pucp.odiparpackappback.controllers.PedidoController;
import com.pucp.odiparpackappback.models.Mapa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class OdiparPackAppBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdiparPackAppBackApplication.class, args);
        // Inicio de la simulación Dia a Dia
        PedidoController pedidoController = new PedidoController(Mapa.pedidoRepository);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedDateTime = now.format(formatter);
        pedidoController.ejecutarABCDiaDia(formattedDateTime);
    }
}
