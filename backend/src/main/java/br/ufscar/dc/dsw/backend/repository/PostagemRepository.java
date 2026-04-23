package br.ufscar.dc.dsw.backend.repository;

import br.ufscar.dc.dsw.backend.domain.Postagem;
import br.ufscar.dc.dsw.backend.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {

    @Query("SELECT p FROM Postagem p WHERE p.autor IN :amigos ORDER BY p.horario DESC")
    List<Postagem> findByAutorIn(@Param("amigos") List<User> amigos);
}