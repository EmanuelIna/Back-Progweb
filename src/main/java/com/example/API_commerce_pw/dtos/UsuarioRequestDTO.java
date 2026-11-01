package com.example.API_commerce_pw.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class UsuarioRequestDTO {

    @NotBlank
    @Schema(example = "Aluno")
    private String nome;

    @NotBlank 
    @Email
    @Schema(example = "aluno@example.com")
    private String email;

    @NotBlank
    @Schema(example = "SenhaDeTeste2026!")
    private String senha;
}
