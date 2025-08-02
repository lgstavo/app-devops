package br.ufscar.dc.dsw.backend.domain;


import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;


@Table(name="Postagem")
@Entity
@Data
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String descricao;

    @Column
    private LocalDateTime horario;
}
