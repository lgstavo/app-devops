package br.ufscar.dc.dsw.backend.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "friend_requests")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Muitos pedidos podem ser de um usuário
    @JoinColumn(name = "from_user_id", nullable = false)
    private User fromUser;

    @ManyToOne // Muitos pedidos podem ser para um usuário
    @JoinColumn(name = "to_user_id", nullable = false)
    private User toUser;

    @Column(nullable = false)
    private String status; // "pendente", "aceita", "recusada"
}
