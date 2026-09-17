package com.example.API_commerce_pw.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoRequestDTO {

    @NotBlank
    private String nomeDoProduto;

    @NotBlank
    private String descricaoDoProduto;

    @NotNull
    @Min(value = 0)
    private BigDecimal precoDoProduto;

    @NotNull
    @Min(value = 0)
    private Integer quantidadeEmEstoque;
}