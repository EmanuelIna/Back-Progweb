package com.example.API_commerce_pw.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Data
public class ProdutoRequestDTO {

    @NotBlank
    @Size(max = 255)
    @Schema(example = "Livro Java")
    private String nomeDoProduto;

    @NotBlank
    @Size(max = 255)
    @Schema(example = "Introdução ao Spring Boot")
    private String descricaoDoProduto;

    @NotNull
    @Min(value = 0)
    @Digits(integer = 12, fraction = 2)
    @Schema(example = "49.90")
    private BigDecimal precoDoProduto;

    @NotNull
    @Min(value = 0)
    @Schema(example = "10")
    private Integer quantidadeEmEstoque;
}
