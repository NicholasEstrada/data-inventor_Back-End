package com.nicholastcc.datainventor.controller.dto;

import com.nicholastcc.datainventor.model.Usuarios.UsuarioRole;

public record RegisterDTO(String nome, String username, String password, UsuarioRole role) {
}
