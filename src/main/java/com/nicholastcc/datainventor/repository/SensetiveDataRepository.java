package com.nicholastcc.datainventor.repository;

import com.nicholastcc.datainventor.model.SensitiveDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensetiveDataRepository extends JpaRepository<SensitiveDataModel, Long> {
}
