package com.nicholastcc.datainventor.controller;

import com.nicholastcc.datainventor.model.DominioModel;
import com.nicholastcc.datainventor.model.SensetiveDataModel;
import com.nicholastcc.datainventor.model.Usuarios.UsuarioModel;
import com.nicholastcc.datainventor.repository.DominioRepository;
import com.nicholastcc.datainventor.repository.SensetiveDataRepository;
import com.nicholastcc.datainventor.repository.UsuarioRepository;
import com.nicholastcc.datainventor.service.InventorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventor")
public class DominioController {

    @Autowired
    private InventorService inventorService;

    @Autowired
    private SensetiveDataRepository sensetiveDataRepository;

    @Autowired
    private DominioRepository dominioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/buscaPorDominio")
    public ResponseEntity<String> buscaPorDominio(@RequestBody Map<String, String> requestBody){
        String domain = requestBody.get("dominio");

        inventorService.inventor(domain);
        return ResponseEntity.ok("Processado o valor de " + domain);
    }

    @GetMapping("/resultado/{id}")
    public ResponseEntity<List<SensetiveDataModel>> buscaSensetivePorDominioId(@PathVariable Long id) {
        List<SensetiveDataModel> resultado = sensetiveDataRepository.findByDominioId(id);

        return resultado != null && !resultado.isEmpty()
                ? ResponseEntity.ok(resultado)
                : ResponseEntity.notFound().build();
    }


    @GetMapping("/dominios")
    public ResponseEntity<List<DominioModel>> todosDominios(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UsuarioModel usuario = usuarioRepository.findByUsername(username);
        try {
            List<DominioModel> dominioModels = dominioRepository.findAll();
            return dominioModels != null ? ResponseEntity.ok(dominioModels) : ResponseEntity.notFound().build();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/dadosSensiveis")
    public ResponseEntity<List<SensetiveDataModel>> todosSensetive(){
        List<SensetiveDataModel> sensetiveDataModels = sensetiveDataRepository.findAll();

        return ResponseEntity.ok(sensetiveDataModels);
    }
}
