package br.ufscar.dc.dsw.backend.dto;

public record LoginRequest (String username, String email, String password) {
}
