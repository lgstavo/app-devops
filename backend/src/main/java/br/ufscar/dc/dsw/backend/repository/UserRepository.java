package br.ufscar.dc.dsw.backend.repository;

import br.ufscar.dc.dsw.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(
            "SELECT u FROM User u WHERE u.id <> :currentUserId " +
                    "AND NOT EXISTS (" +
                    "    SELECT fr FROM FriendRequest fr " +
                    "    WHERE ((fr.fromUser.id = :currentUserId AND fr.toUser.id = u.id) OR " +
                    "           (fr.fromUser.id = u.id AND fr.toUser.id = :currentUserId)) " +
                    "    AND fr.status IN ('aceita', 'pendente')" +
                    ")"
    )
    List<User> findPotentialFriends(@Param("currentUserId") Long currentUserId);

}
