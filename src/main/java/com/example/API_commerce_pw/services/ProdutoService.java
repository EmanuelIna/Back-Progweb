package com.example.API_commerce_pw.services;

import com.example.API_commerce_pw.dtos.ProdutoRequestDTO;
import com.example.API_commerce_pw.models.Produto;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProdutoService {

    private List<Produto> listaDeProdutos = new ArrayList<>();

    public Produto salvarNovoProduto(ProdutoRequestDTO dadosDoProduto) {
        Produto novoItem = new Produto();
        novoItem.setIdProduto(UUID.randomUUID().toString()); // Gera um ID aleatório
        novoItem.setNomeDoProduto(dadosDoProduto.getNomeDoProduto());
        novoItem.setDescricaoDoProduto(dadosDoProduto.getDescricaoDoProduto());
        novoItem.setPrecoDoProduto(dadosDoProduto.getPrecoDoProduto());
        novoItem.setQuantidadeEmEstoque(dadosDoProduto.getQuantidadeEmEstoque());
        
        listaDeProdutos.add(novoItem);
        return novoItem;
    }

    public List<Produto> buscarTodos() {
        return listaDeProdutos;
    }
}