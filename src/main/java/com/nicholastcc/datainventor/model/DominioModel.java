package com.nicholastcc.datainventor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nicholastcc.datainventor.model.Usuarios.UsuarioModel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
//@Table(name = "dominio", uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"usuario_id", "dominio"})
//})
public class DominioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "dominio")
    private String dominio;

    @Column(name = "qtd_pag_visitadas")
    private int qtd_pag_visitadas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private UsuarioModel usuario;

    @Column(name = "data_criacao")
    @CreationTimestamp // anotação para preencher automaticamente com a data e hora de criação
    private Date dataCriacao;

    @OneToMany(mappedBy = "dominio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<SensetiveDataModel> dadosList = new ArrayList<>();
}
