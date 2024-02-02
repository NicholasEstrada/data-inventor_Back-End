package com.nicholastcc.datainventor.controller;

import com.nicholastcc.datainventor.controller.exception.UsuarioCadastradoException;
import com.nicholastcc.datainventor.model.UsuarioModel;
import com.nicholastcc.datainventor.repository.UsuarioRepository;
import com.nicholastcc.datainventor.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository repository;
    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody @Validated UsuarioModel usuarioModel){
        try {
            usuarioModel.setPassword(encoder.encode(usuarioModel.getPassword()));
            usuarioService.salvar(usuarioModel);
        }catch (UsuarioCadastradoException e){
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/validarSenha")
    public ResponseEntity<Boolean> validarSenha(@RequestParam String username,
                                                @RequestParam String password){
        Optional<UsuarioModel> optUsuario = repository.findByUsername(username);
        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }

        UsuarioModel usuario = optUsuario.get();
        boolean valid = encoder.matches(password, usuario.getPassword());

        HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;

        return ResponseEntity.status(status).body(valid);
    }
}
