package com.nicholastcc.datainventor.repository;

import com.nicholastcc.datainventor.model.PathLocationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PathLocationRepository extends JpaRepository<PathLocationModel, Long> {

    Optional<PathLocationModel> findByPathLocation(String pathLocation);

}
