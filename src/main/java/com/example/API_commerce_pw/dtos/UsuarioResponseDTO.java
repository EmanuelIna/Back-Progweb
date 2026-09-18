package com.example.API_commerce_pw.dtos;

import com.example.API_commerce_pw.models.enums.PerfilUsuario;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private String idUsuario;
    private String nome;
    private String email;
    private PerfilUsuario perfil;
}