package com.pucp.odiparpackappback;

import com.pucp.odiparpackappback.controllers.PedidoController;
import com.pucp.odiparpackappback.models.Mapa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableScheduling
public class OdiparPackAppBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdiparPackAppBackApplication.class, args);
        // Inicio de la simulación Dia a Dia
        PedidoController pedidoController = new PedidoController(Mapa.pedidoRepository);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        String formattedDateTime = now.format(formatter);
        //pedidoController.ejecutarABCDiaDia(formattedDateTime);

        Mapa.cargarOficinasSimulacion("src/main/resources/static/oficina_model.csv");
        Mapa.cargarTramosSimulacion("src/main/resources/static/tramo_model.csv");
        Mapa.cargarVehiculosSimulacion("src/main/resources/static/unidad_transporte_model.csv");
        Mapa.cargarBloqueosSimulacion("src/main/resources/static/bloqueo_model.csv");
    }

    @Scheduled(cron = "0 0 0,6,12,18 * * *")
    public void diadia(){
        // Inicio de la simulación Dia a Dia
        PedidoController pedidoController = new PedidoController(Mapa.pedidoRepository);
        pedidoController.ejecutarABCDiaDia();
    }
}
