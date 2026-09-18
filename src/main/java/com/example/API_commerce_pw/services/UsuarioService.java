package com.example.API_commerce_pw.services;

import com.example.API_commerce_pw.dtos.UsuarioRequestDTO;
import com.example.API_commerce_pw.dtos.UsuarioResponseDTO;
import com.example.API_commerce_pw.models.Usuario;
import com.example.API_commerce_pw.models.enums.PerfilUsuario;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private List<Usuario> listaDeUsuarios = new ArrayList<>();

    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO dadosRecebidos) {
        // 1. Transforma o DTO em Model
        Usuario novoUsuario = new Usuario();
        novoUsuario.setIdUsuario(UUID.randomUUID().toString());
        novoUsuario.setNome(dadosRecebidos.getNome());
        novoUsuario.setEmail(dadosRecebidos.getEmail());
        novoUsuario.setSenha(dadosRecebidos.getSenha());
        
        
        novoUsuario.setPerfil(PerfilUsuario.CLIENTE);

    
        listaDeUsuarios.add(novoUsuario);


        return converterParaResponseDTO(novoUsuario);
    }

    public List<UsuarioResponseDTO> buscarTodos() {
        return listaDeUsuarios.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    private UsuarioResponseDTO converterParaResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setPerfil(usuario.getPerfil());
        return dto;
    }

    //Perfil de admin criado para testes
    @PostConstruct
    public void criarAdminPadrao() {
        Usuario admin = new Usuario();
        admin.setIdUsuario(UUID.randomUUID().toString());
        admin.setNome("Administrador do Sistema");
        admin.setEmail("admin@commerce.com");
        admin.setSenha("senha-admin-123");
        admin.setPerfil(PerfilUsuario.ADMIN); 

        listaDeUsuarios.add(admin);
        
        System.out.println("Usuário ADMIN criado com sucesso para testes!");
    }
}