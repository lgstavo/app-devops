package br.ufscar.dc.dsw.backend.controller;

import br.ufscar.dc.dsw.backend.domain.User;
import br.ufscar.dc.dsw.backend.dto.UserResponseDTO;
import br.ufscar.dc.dsw.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/users") // Todas as rotas neste controller começarão com /api/users
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Endpoint para listar todos os usuários com quem o usuário logado pode fazer amizade.
     * Este endpoint é protegido e requer um token JWT válido.
     *
     * @param userDetails Detalhes do usuário autenticado, injetado pelo Spring Security.
     * @return Uma lista de usuários em formato DTO.
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getPotentialFriends(@AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = (User) userDetails;
        Long currentUserId = currentUser.getId();

        List<UserResponseDTO> potentialFriends = userService.getPotentialFriends(currentUserId);

        return ResponseEntity.ok(potentialFriends);
    }
}
