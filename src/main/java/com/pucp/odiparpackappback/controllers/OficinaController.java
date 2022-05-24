package com.pucp.odiparpackappback.controllers;

import com.pucp.odiparpackappback.models.OficinaModel;
import com.pucp.odiparpackappback.services.OficinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/Oficina")
public class OficinaController {
    private final OficinaService oficinaService;

    @Autowired
    public OficinaController(OficinaService oficinaService){
        this.oficinaService = oficinaService;
    }
    @GetMapping
    public List<OficinaModel> getOficinas(){
        ArrayList<OficinaModel> oficinasPrincipales = new ArrayList<>();
        return oficinaService.getOficinas(oficinasPrincipales);
    }
}