package com.pucp.odiparpackappback;

import com.pucp.odiparpackappback.Repositories.PedidoRepository;
import com.pucp.odiparpackappback.controllers.PedidoController;
import com.pucp.odiparpackappback.models.PedidoModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class OdiparPackAppBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(OdiparPackAppBackApplication.class, args);
    }
}
