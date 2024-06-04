package com.nicholastcc.datainventor.controller;

import com.nicholastcc.datainventor.model.DominioModel;
import com.nicholastcc.datainventor.model.PathLocationModel;
import com.nicholastcc.datainventor.model.SensetiveDataModel;
import com.nicholastcc.datainventor.model.Usuarios.UsuarioModel;
import com.nicholastcc.datainventor.repository.DominioRepository;
import com.nicholastcc.datainventor.repository.PathLocationRepository;
import com.nicholastcc.datainventor.repository.SensetiveDataRepository;
import com.nicholastcc.datainventor.repository.UsuarioRepository;
import com.nicholastcc.datainventor.service.InventorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    private PathLocationRepository pathLocationRepository;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioModel usuario = (UsuarioModel) authentication.getPrincipal();

        List<SensetiveDataModel> sensetiveDataModels = sensetiveDataRepository.findByDominioIdAndUsuario(dominioId, usuario);

        return ResponseEntity.ok(sensetiveDataModels);
    }

    @GetMapping("/dadosSensiveisPorPathLocation")
    public ResponseEntity<List<SensetiveDataModel>> dadosSensiveisPorPathLocation(@RequestParam Long pathLocationId) {
        Optional<PathLocationModel> pathIdExist = pathLocationRepository.findById(pathLocationId);
        if (pathIdExist.isPresent()) {
            List<SensetiveDataModel> sensetiveDataModels = sensetiveDataRepository.findByPathLocation(pathIdExist.get());
            return ResponseEntity.ok(sensetiveDataModels);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/deletesensetivedatas/{id}")
    public ResponseEntity<String> deleteDominio(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsuarioModel usuario = (UsuarioModel) authentication.getPrincipal();

        Optional<DominioModel> dominioOptional = dominioRepository.findById(id);

        if (dominioOptional.isPresent()) {
            sensetiveDataRepository.deleteByDominioIdAndUsuarioId(id, Long.valueOf(usuario.getId()));
            Map<String, String> response = new HashMap<>();
            response.put("message", "OK");
            return ResponseEntity.ok(response.toString());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Domínio não encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.toString());
        }
    }
}
