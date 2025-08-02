package br.ufscar.dc.dsw.backend.dto;

import java.time.LocalDateTime;

public record PostagemDTO(
    String descricao,
    LocalDateTime horario
) {
} 
    

