package com.example.API_commerce_pw.controllers;

import com.example.API_commerce_pw.dtos.LoginRequestDTO;
import com.example.API_commerce_pw.dtos.UsuarioRequestDTO;
import com.example.API_commerce_pw.dtos.UsuarioResponseDTO;
import com.example.API_commerce_pw.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioRequestDTO dadosRecebidos) {
        UsuarioResponseDTO usuarioCriado = usuarioService.cadastrarUsuario(dadosRecebidos);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.buscarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/login")
    public ResponseEntity<?> autenticarUsuario(@Valid @RequestBody LoginRequestDTO dadosLogin) {
        try {
            UsuarioResponseDTO usuarioLogado = usuarioService.fazerLogin(dadosLogin);
            return ResponseEntity.ok(usuarioLogado); // Retorna 200 OK com os dados
        } catch (RuntimeException e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
} 