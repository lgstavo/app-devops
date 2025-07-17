USE amizade_db;

-- Cria a tabela de usuários
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
    );

-- Cria a tabela de pedidos de amizade
CREATE TABLE IF NOT EXISTS friend_requests (
                                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                               from_user_id BIGINT NOT NULL,
                                               to_user_id BIGINT NOT NULL,
                                               status VARCHAR(50) NOT NULL, -- ex: 'pendente', 'aceita', 'recusada'
    FOREIGN KEY (from_user_id) REFERENCES users(id),
    FOREIGN KEY (to_user_id) REFERENCES users(id),
    -- Garante que não haja pedidos duplicados entre as mesmas duas pessoas
    UNIQUE KEY unique_friend_request (from_user_id, to_user_id)
    );
