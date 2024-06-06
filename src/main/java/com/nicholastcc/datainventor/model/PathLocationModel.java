package com.nicholastcc.datainventor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "path_location")
public class PathLocationModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path_location")
    private String pathLocation;

    @Column(name = "path_parent")
    private String pathParent;

    @Column(name = "tipo_de_arquivo")
    private String tipoDeArquivo;

    @Column(name = "processamento")
    private String processamento;

    @OneToMany(mappedBy = "pathLocation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<SensetiveDataModel> sensetiveDataModelList = new ArrayList<>();
}
