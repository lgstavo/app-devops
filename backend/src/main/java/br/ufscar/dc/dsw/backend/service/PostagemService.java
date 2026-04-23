package br.ufscar.dc.dsw.backend.service;

import br.ufscar.dc.dsw.backend.domain.*;
import br.ufscar.dc.dsw.backend.dto.*;
import br.ufscar.dc.dsw.backend.repository.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostagemService {

    private final UserRepository userRepository;
    private final PostagemRepository postagemRepository;
    private final FriendRequestRepository friendRequestRepository;

    public Postagem criarPostagem(PostagemCreateDTO dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User autor = userRepository.findByUsername(username).orElseThrow();

        Postagem novaPostagem = new Postagem();
        novaPostagem.setDescricao(dto.descricao());
        novaPostagem.setHorario(LocalDateTime.now());
        novaPostagem.setAutor(autor);
        return postagemRepository.save(novaPostagem);
    }

    @Transactional(readOnly = true)
    public List<Postagem> listarPostagem() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username).orElseThrow();
        List<User> friends = friendRequestRepository.findAllFriendsByUserId(currentUser.getId());
        return postagemRepository.findByAutorIn(friends);
    }
}