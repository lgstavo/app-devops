package br.ufscar.dc.dsw.backend.dto;

public record AuthenticationResponse (String token, Long userId, String username){
}
