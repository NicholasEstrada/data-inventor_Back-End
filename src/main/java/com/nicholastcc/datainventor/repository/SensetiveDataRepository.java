package com.nicholastcc.datainventor.repository;

import com.nicholastcc.datainventor.model.PathLocationModel;
import com.nicholastcc.datainventor.model.SensetiveDataModel;
import com.nicholastcc.datainventor.model.Usuarios.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensetiveDataRepository extends JpaRepository<SensetiveDataModel, Long> {
    List<SensetiveDataModel> findByDominioId(Long dominioId);

    List<SensetiveDataModel> findByUsuarioId(Long usuarioId);

    List<SensetiveDataModel> findByDominioIdAndUsuario(Long dominioId, UsuarioModel usuario);

    List<SensetiveDataModel> findByPathLocation(PathLocationModel pathLocation);

    List<SensetiveDataModel> findByPathLocationAndSensitiveAndUsuario(PathLocationModel pathLocation, String sensitive, UsuarioModel usuario);
}
