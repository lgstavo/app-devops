package br.ufscar.dc.dsw.backend.dto;

public record FriendRequestResponseDTO(
        Long requestId,
        String status // Espera-se "aceita" ou "recusada"
) {
}