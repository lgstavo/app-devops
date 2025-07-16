package br.ufscar.dc.dsw.backend.dto;

public record ReceivedRequestDTO(
        Long requestId,
        Long fromUserId,
        String fromUsername
) {
}