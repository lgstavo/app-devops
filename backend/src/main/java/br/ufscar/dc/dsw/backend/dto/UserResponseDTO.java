package br.ufscar.dc.dsw.backend.dto;

/**
 * DTO (Data Transfer Object) para enviar informações de um usuário para o cliente.
 * Contém apenas os dados públicos e seguros.
 *
 * Usando 'record' do Java 17+ para uma definição concisa e imutável.
 *
 * @param id o ID único do usuário.
 * @param username o nome de usuário.
 */
public record UserResponseDTO(
        Long id,
        String username
) {
}