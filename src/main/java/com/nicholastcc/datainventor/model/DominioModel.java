package com.nicholastcc.datainventor.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class DominioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "dominio", unique = true)
    private String dominio;

    @OneToMany(mappedBy = "dominio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensitiveDataModel> dadosList = new ArrayList<>();

}