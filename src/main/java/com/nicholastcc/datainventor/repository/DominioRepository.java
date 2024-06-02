package com.nicholastcc.datainventor.repository;

import com.nicholastcc.datainventor.model.DominioModel;
import com.nicholastcc.datainventor.model.Usuarios.UsuarioModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DominioRepository extends JpaRepository <DominioModel, Long>{

    Optional<DominioModel> findById(Long id);

    @EntityGraph(attributePaths = "dadosList")
    Optional<DominioModel> findWithSensetiveDataListById(Long id);

    Optional<DominioModel> findByDominio(String dominio);

    @Query("SELECT DISTINCT d FROM DominioModel d " +
            "JOIN SensetiveDataModel s ON s.dominio = d " +
            "JOIN UsuarioModel u ON s.usuario = u " +
            "WHERE u.username = :username")
    List<DominioModel> findDominioByUsuarioUsername(@Param("username") String username);

    Optional<DominioModel> findByDominioAndUsuario(String finalDomain, UsuarioModel usuarioModel);
}
