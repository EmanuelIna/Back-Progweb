package com.example.API_commerce_pw.models;

import com.example.API_commerce_pw.models.enums.PerfilUsuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor 
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String idUsuario;
    private String nome;
    @Column(nullable = false, unique = true)
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    private PerfilUsuario perfil;
}
