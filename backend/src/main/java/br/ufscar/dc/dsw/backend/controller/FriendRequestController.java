package br.ufscar.dc.dsw.backend.controller;

import br.ufscar.dc.dsw.backend.domain.User;
import br.ufscar.dc.dsw.backend.dto.FriendRequestDTO;
import br.ufscar.dc.dsw.backend.dto.FriendRequestResponseDTO;
import br.ufscar.dc.dsw.backend.dto.ReceivedRequestDTO;
import br.ufscar.dc.dsw.backend.dto.UserResponseDTO;
import br.ufscar.dc.dsw.backend.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend-requests")
@RequiredArgsConstructor
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    // Endpoint para enviar um pedido de amizade
    @PostMapping
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequestDTO requestDTO, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = (User) userDetails;
        friendRequestService.sendFriendRequest(currentUser.getId(), requestDTO.toUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Pedido de amizade enviado com sucesso.");
    }

    // Endpoint para responder a um pedido de amizade
    @PostMapping("/respond")
    public ResponseEntity<String> respondToFriendRequest(@RequestBody FriendRequestResponseDTO responseDTO, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = (User) userDetails;
        friendRequestService.respondToRequest(currentUser.getId(), responseDTO.requestId(), responseDTO.status());
        return ResponseEntity.ok("Pedido respondido com sucesso.");
    }

    // Endpoint para listar os pedidos de amizade recebidos e pendentes
    @GetMapping("/received")
    public ResponseEntity<List<ReceivedRequestDTO>> getReceivedRequests(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = (User) userDetails;
        List<ReceivedRequestDTO> requests = friendRequestService.getReceivedRequests(currentUser.getId());
        return ResponseEntity.ok(requests);
    }

    // Endpoint para listar todos os amigos
    @GetMapping("/friends")
    public ResponseEntity<List<UserResponseDTO>> getFriends(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = (User) userDetails;
        List<UserResponseDTO> friends = friendRequestService.getFriends(currentUser.getId());
        return ResponseEntity.ok(friends);
    }
}