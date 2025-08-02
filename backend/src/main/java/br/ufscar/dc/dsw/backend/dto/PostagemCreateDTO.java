package br.ufscar.dc.dsw.backend.dto;

import jakarta.validation.constraints.NotBlank;


public record PostagemCreateDTO(
    
    @NotBlank(message="Obrigatório inserir uma descrição")
    String descricao

) {
}
