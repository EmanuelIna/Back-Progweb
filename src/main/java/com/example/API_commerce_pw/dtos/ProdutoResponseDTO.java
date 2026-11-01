package com.example.API_commerce_pw.dtos;
import java.math.BigDecimal;
import com.example.API_commerce_pw.models.Produto;
public record ProdutoResponseDTO(String idProduto, String nomeDoProduto, String descricaoDoProduto, BigDecimal precoDoProduto, Integer quantidadeEmEstoque) {
 public static ProdutoResponseDTO from(Produto p) { return new ProdutoResponseDTO(p.getIdProduto(),p.getNomeDoProduto(),p.getDescricaoDoProduto(),p.getPrecoDoProduto(),p.getQuantidadeEmEstoque()); }
}
