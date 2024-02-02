package com.nicholastcc.datainventor.repository;

import com.nicholastcc.datainventor.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {

    Optional<UsuarioModel> findByUsername(String username);

    boolean existsByUsername(String username);
}
