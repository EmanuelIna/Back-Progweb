package com.example.API_commerce_pw.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data // Gera getters, setters, toString, tudo automaticamente (lombok serve para isso)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String idProduto; // Recebe a string aleatoria gerada
    private String nomeDoProduto;
    private String descricaoDoProduto;
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal precoDoProduto;
    private Integer quantidadeEmEstoque;
}
