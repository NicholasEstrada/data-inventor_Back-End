package com.nicholastcc.datainventor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class SensetiveDataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo") // pdf, jpeg jpg ...
    private String tipo;

    @Column(name = "sensitive") // o dado em si
    private String sensitive;

    @Column(name = "pathlocation") // url completa do arquivo
    private String pathLocation;

    @ManyToOne
    @JoinColumn(name = "dominio")
    private DominioModel dominio;


}
