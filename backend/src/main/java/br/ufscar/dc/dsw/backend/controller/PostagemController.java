package br.ufscar.dc.dsw.backend.controller;


import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.ufscar.dc.dsw.backend.domain.Postagem;
import br.ufscar.dc.dsw.backend.dto.PostagemDTO;
import br.ufscar.dc.dsw.backend.dto.PostagemResponseDTO;
import br.ufscar.dc.dsw.backend.dto.PostagemCreateDTO;
import br.ufscar.dc.dsw.backend.service.PostagemService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/postagens")
@RequiredArgsConstructor
public class PostagemController {
    private final PostagemService postagemService;

    @PostMapping("/publicar")
    public ResponseEntity<PostagemResponseDTO> realizarPostagem(@RequestBody PostagemCreateDTO dto){
        Postagem novaPostagem = postagemService.criarPostagem(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(novaPostagem.getId())
                        .toUri();
        return ResponseEntity.created(location).body(new PostagemResponseDTO(novaPostagem));
    }

    @GetMapping
    public ResponseEntity<List<PostagemResponseDTO>> listarPostagens(){
        List<Postagem> postagens = postagemService.listarPostagem();
        List<PostagemResponseDTO> dtos = postagens.stream().map(PostagemResponseDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
}
