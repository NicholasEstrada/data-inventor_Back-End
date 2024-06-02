package com.nicholastcc.datainventor.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "sensetive_data", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"path_location_id", "sensitive", "usuario_id"})
})
public class SensetiveDataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo") // CPF, Email, opinião politica, identidade de genero...
    private String tipo;

    @Column(name = "sensitive") // o dado em si, cpf, e-mail
    private String sensitive;

    @ManyToOne
    @JoinColumn(name = "path_location_id")
    private PathLocationModel pathLocation;

    @ManyToOne
    @JoinColumn(name = "dominio")
    private DominioModel dominio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private UsuarioModel usuario;

    @Column(name = "data_criacao")
    @CreationTimestamp // Adicione esta anotação para preencher automaticamente com a data e hora de criação
    private Date dataCriacao;
}
