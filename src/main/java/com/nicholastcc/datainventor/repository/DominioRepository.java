package com.nicholastcc.datainventor.repository;

import com.nicholastcc.datainventor.model.DominioModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DominioRepository extends JpaRepository <DominioModel, Long>{

    Optional<DominioModel> findById(Long id);

    @EntityGraph(attributePaths = "dadosList")
    Optional<DominioModel> findWithSensetiveDataListById(Long id);

    Optional<DominioModel> findByDominio(String dominio);
}
