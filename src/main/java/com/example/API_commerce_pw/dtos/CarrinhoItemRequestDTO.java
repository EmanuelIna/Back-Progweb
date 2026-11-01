package com.example.API_commerce_pw.dtos;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
public record CarrinhoItemRequestDTO(
 @NotBlank @Schema(description="idProduto de um produto existente",example="550e8400-e29b-41d4-a716-446655440000") String idProduto,
 @NotNull @Min(1) @Max(999) @Schema(description="Quantidade a somar ao item",example="2") Integer quantidade) {}
