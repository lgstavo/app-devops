package br.ufscar.dc.dsw.backend.dto;

import br.ufscar.dc.dsw.backend.domain.Postagem;
import java.time.LocalDateTime;

public record PostagemResponseDTO(
    String descricao,
    LocalDateTime horario
) {
    public PostagemResponseDTO(Postagem postagem){
        this(
            postagem.getDescricao(),
            postagem.getHorario()
        );
    }
}
