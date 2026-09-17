package com.example.API_commerce_pw.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data // Gera getters, setters, toString, tudo automaticamente (lombok serve para isso)
@AllArgsConstructor
@NoArgsConstructor
public class Produto {
    private String idProduto; // Recebe a string aleatoria gerada
    private String nomeDoProduto;
    private String descricaoDoProduto;
    private BigDecimal precoDoProduto;
    private Integer quantidadeEmEstoque;
}