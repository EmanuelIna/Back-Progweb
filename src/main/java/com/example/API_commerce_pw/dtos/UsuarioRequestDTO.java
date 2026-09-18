package com.example.API_commerce_pw.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank 
    @Email
    private String email;

    @NotBlank
    private String senha;
}