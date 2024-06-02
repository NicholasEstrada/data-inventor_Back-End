package com.nicholastcc.datainventor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nicholastcc.datainventor.model.Usuarios.UsuarioModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "dominio", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "dominio"})
})
public class DominioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "dominio")
    private String dominio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private UsuarioModel usuario;

    @OneToMany(mappedBy = "dominio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<SensetiveDataModel> dadosList = new ArrayList<>();
}
