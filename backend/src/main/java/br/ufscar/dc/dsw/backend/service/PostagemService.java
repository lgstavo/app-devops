package br.ufscar.dc.dsw.backend.service;

import br.ufscar.dc.dsw.backend.domain.*;
import br.ufscar.dc.dsw.backend.dto.*;
import br.ufscar.dc.dsw.backend.repository.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.*;


@Service
@RequiredArgsConstructor
@Transactional
public class PostagemService{
    private final UserRepository userRepository;
    private final PostagemRepository postagemRepository;
    
    public Postagem criarPostagem(PostagemCreateDTO dto){
        Postagem novaPostagem = new Postagem();
        
        novaPostagem.setDescricao(dto.descricao());
        novaPostagem.setHorario(LocalDateTime.now());
        
        return postagemRepository.save(novaPostagem);
    }



    @Transactional(readOnly=true)
    public List<Postagem> listarPostagem(){
        return postagemRepository.findAll();
    }

    
}