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
        return ResponseEntity.ok("");
    }


    @GetMapping("/dominios")
    public ResponseEntity<List<DominioModel>> todosDominios(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<DominioModel> dominioModels = dominioRepository.findDominioByUsuarioUsername(username);
        return dominioModels != null ? ResponseEntity.ok(dominioModels) : ResponseEntity.notFound().build();
    }

    @GetMapping("/dadosSensiveis")
    public ResponseEntity<List<SensetiveDataModel>> dadosSensiveisPorUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Long userId = Long.valueOf(usuarioRepository.findByUsername(username).getId());

        List<SensetiveDataModel> sensetiveDataModels = sensetiveDataRepository.findByUsuarioId(userId);
        return ResponseEntity.ok(sensetiveDataModels);
    }

    @GetMapping("/dadosSensiveisDomain")
    public ResponseEntity<List<SensetiveDataModel>> dadosSensiveisPorDominioEUsuario(@RequestParam Long dominioId) {
        // Obter o usuário atualmente autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioModel usuario = (UsuarioModel) authentication.getPrincipal();

        // Buscar os SensetiveDataModel relacionados ao domínio e ao usuário
        List<SensetiveDataModel> sensetiveDataModels = sensetiveDataRepository.findByDominioIdAndUsuario(dominioId, usuario);

        return ResponseEntity.ok(sensetiveDataModels);
    }
}
