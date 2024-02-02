package com.nicholastcc.datainventor.repository;

import com.nicholastcc.datainventor.model.SensetiveDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensetiveDataRepository extends JpaRepository<SensetiveDataModel, Long> {
    List<SensetiveDataModel> findByDominioId(Long dominioId);
}
