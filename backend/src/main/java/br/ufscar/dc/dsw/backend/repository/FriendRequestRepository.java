package br.ufscar.dc.dsw.backend.repository;

import br.ufscar.dc.dsw.backend.domain.FriendRequest;
import br.ufscar.dc.dsw.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    // Query para verificar se já existe um pedido (aceito ou pendente) entre dois usuários
    @Query("SELECT fr FROM FriendRequest fr WHERE " +
            "((fr.fromUser.id = :user1Id AND fr.toUser.id = :user2Id) OR " +
            "(fr.fromUser.id = :user2Id AND fr.toUser.id = :user1Id)) " +
            "AND fr.status IN ('pendente', 'aceita')")
    Optional<FriendRequest> findExistingRequest(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    // Query para buscar todos os pedidos pendentes recebidos por um usuário
    @Query("SELECT fr FROM FriendRequest fr JOIN FETCH fr.fromUser WHERE fr.toUser.id = :currentUserId AND fr.status = 'pendente'")
    List<FriendRequest> findPendingRequestsForUser(@Param("currentUserId") Long currentUserId);

    // Query para buscar todos os amigos de um usuário
    @Query("SELECT u FROM User u " +
            "WHERE u.id IN (" +
            "    SELECT fr.fromUser.id FROM FriendRequest fr WHERE fr.toUser.id = :userId AND fr.status = 'aceita'" +
            ") OR u.id IN (" +
            "    SELECT fr.toUser.id FROM FriendRequest fr WHERE fr.fromUser.id = :userId AND fr.status = 'aceita'" +
            ")")
    List<User> findAllFriendsByUserId(@Param("userId") Long userId);
}