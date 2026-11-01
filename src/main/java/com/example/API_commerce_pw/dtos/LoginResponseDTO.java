package com.example.API_commerce_pw.dtos;
import com.example.API_commerce_pw.models.enums.PerfilUsuario;
public record LoginResponseDTO(String idUsuario,String nome,String email,PerfilUsuario perfil,String token,String tokenType,long expiresIn) {}
