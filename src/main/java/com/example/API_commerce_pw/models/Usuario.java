package com.example.API_commerce_pw.models;

import com.example.API_commerce_pw.models.enums.PerfilUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
public class Usuario {
    private String idUsuario;
    private String nome;
    private String email;
    private String senha;
    private PerfilUsuario perfil;
}
