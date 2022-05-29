package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.Repositories.ClienteRepository;
import com.pucp.odiparpackappback.models.BloqueoModel;
import com.pucp.odiparpackappback.models.ClienteModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClienteController {
    private final ClienteRepository clienteRepository;
    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/Cliente/")
    List<ClienteModel> listarClientes(){
        return (List<ClienteModel>) clienteRepository.findAll();
    }
}
