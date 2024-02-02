package com.nicholastcc.datainventor.repository;

import com.nicholastcc.datainventor.model.Usuarios.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {

    //Optional<UsuarioModel> findByUsername(String username);

    UserDetails findByUsername(String username);

    boolean existsByUsername(String username);
}
