package br.ufscar.dc.dsw.backend.repository;

import br.ufscar.dc.dsw.backend.domain.Postagem;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostagemRepository extends JpaRepository<Postagem, Long>{

}
