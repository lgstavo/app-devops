package br.ufscar.dc.dsw.backend.dto;

import br.ufscar.dc.dsw.backend.domain.Postagem;
import java.time.LocalDateTime;

public record PostagemResponseDTO(Long id, String username, String descricao, LocalDateTime horario) {
    public PostagemResponseDTO(Postagem p) {
        this(p.getId(), p.getAutor().getUsername(), p.getDescricao(), p.getHorario());
    }
}