package com.example.API_commerce_pw.dtos;
import java.math.BigDecimal;
import java.util.List;
public record CarrinhoResponseDTO(List<Item> itens,BigDecimal total) {
 public record Item(ProdutoResponseDTO produto,int quantidade,BigDecimal subtotal) {}
}
