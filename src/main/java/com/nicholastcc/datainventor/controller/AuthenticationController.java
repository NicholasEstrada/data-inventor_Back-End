package com.nicholastcc.datainventor.controller;

import com.nicholastcc.datainventor.config.TokenService;
import com.nicholastcc.datainventor.controller.dto.AuthenticationDTO;
import com.nicholastcc.datainventor.controller.dto.RegisterDTO;
import com.nicholastcc.datainventor.controller.dto.UsuarioResponseDTO;
import com.nicholastcc.datainventor.controller.exception.UsuarioCadastradoException;
import com.nicholastcc.datainventor.model.Usuarios.UsuarioModel;
import com.nicholastcc.datainventor.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        var token = tokenService.generateToken(userDetails);

        return ResponseEntity.ok(new UsuarioResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        boolean exist = usuarioRepository.existsByUsername(data.username());
        if(exist) throw new UsuarioCadastradoException(data.username());

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UsuarioModel usuarioModel = new UsuarioModel(data.nome(), data.username(), encryptedPassword, data.role());

        this.usuarioRepository.save(usuarioModel);

        return ResponseEntity.ok().build();
    }
}
