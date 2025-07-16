package br.ufscar.dc.dsw.backend.service;

import br.ufscar.dc.dsw.backend.domain.FriendRequest;
import br.ufscar.dc.dsw.backend.domain.User;
import br.ufscar.dc.dsw.backend.dto.ReceivedRequestDTO;
import br.ufscar.dc.dsw.backend.dto.UserResponseDTO;
import br.ufscar.dc.dsw.backend.repository.FriendRequestRepository;
import br.ufscar.dc.dsw.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional // Métodos de escrita precisam de transação
public class FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    // Lógica para enviar um pedido de amizade
    public void sendFriendRequest(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Você não pode enviar um pedido de amizade para si mesmo.");
        }

        friendRequestRepository.findExistingRequest(fromUserId, toUserId).ifPresent(fr -> {
            throw new IllegalStateException("Um pedido de amizade já existe ou vocês já são amigos.");
        });

        User fromUser = userRepository.findById(fromUserId).orElseThrow(() -> new IllegalArgumentException("Usuário remetente não encontrado."));
        User toUser = userRepository.findById(toUserId).orElseThrow(() -> new IllegalArgumentException("Usuário destinatário não encontrado."));

        FriendRequest newRequest = new FriendRequest();
        newRequest.setFromUser(fromUser);
        newRequest.setToUser(toUser);
        newRequest.setStatus("pendente");

        friendRequestRepository.save(newRequest);
    }

    // Lógica para responder a um pedido
    public void respondToRequest(Long currentUserId, Long requestId, String status) {
        if (!status.equals("aceita") && !status.equals("recusada")) {
            throw new IllegalArgumentException("Status inválido. Use 'aceita' ou 'recusada'.");
        }

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido de amizade não encontrado."));

        if (!request.getToUser().getId().equals(currentUserId)) {
            throw new IllegalStateException("Você não tem permissão para responder a este pedido.");
        }

        if (!request.getStatus().equals("pendente")) {
            throw new IllegalStateException("Este pedido não está mais pendente.");
        }

        request.setStatus(status);
        friendRequestRepository.save(request);
    }

    // Lógica para buscar pedidos recebidos
    @Transactional(readOnly = true)
    public List<ReceivedRequestDTO> getReceivedRequests(Long currentUserId) {
        return friendRequestRepository.findPendingRequestsForUser(currentUserId)
                .stream()
                .map(this::convertToReceivedRequestDTO)
                .collect(Collectors.toList());
    }

    // Lógica para buscar amigos
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getFriends(Long currentUserId) {
        return friendRequestRepository.findAllFriendsByUserId(currentUserId)
                .stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos Auxiliares de Conversão para DTO ---

    private ReceivedRequestDTO convertToReceivedRequestDTO(FriendRequest friendRequest) {
        return new ReceivedRequestDTO(
                friendRequest.getId(),
                friendRequest.getFromUser().getId(),
                friendRequest.getFromUser().getUsername()
        );
    }

    private UserResponseDTO convertToUserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername());
    }
}