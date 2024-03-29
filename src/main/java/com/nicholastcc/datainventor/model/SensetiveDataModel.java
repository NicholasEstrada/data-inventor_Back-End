package com.nicholastcc.datainventor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nicholastcc.datainventor.model.Usuarios.UsuarioModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

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

    @Column(name = "sensitive" /*, unique = true*/ ) // o dado em si, cpf, e-mail
    private String sensitive;

    @Column(name = "pathlocation") // url completa do arquivo
    private String pathLocation;

    @ManyToOne
    @JoinColumn(name = "dominio")
    private DominioModel dominio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioModel usuario;

    @Column(name = "data_criacao")
    @CreationTimestamp // Adicione esta anotação para preencher automaticamente com a data e hora de criação
    private Date dataCriacao;

}
