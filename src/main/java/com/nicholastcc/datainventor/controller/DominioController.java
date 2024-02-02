package com.nicholastcc.datainventor.controller;

import com.nicholastcc.datainventor.model.DominioModel;
import com.nicholastcc.datainventor.service.InventorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/inventor")
public class DominioController {

    @Autowired
    private InventorService inventorService;

    @PostMapping("/buscaPorDominio")
    public ResponseEntity<String> buscaPorDominio(@RequestBody Map<String, String> requestBody){
        String domain = requestBody.get("dominio");

        inventorService.inventor(domain);
        return ResponseEntity.ok("Processado o valor de " + domain);
    }
}
