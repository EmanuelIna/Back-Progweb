package com.example.API_commerce_pw.controllers;

import com.example.API_commerce_pw.dtos.ProdutoRequestDTO;
import com.example.API_commerce_pw.models.Produto;
import com.example.API_commerce_pw.services.ProdutoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@Tag(name = "Catálogo de Produtos", description = "Rotas para gerenciamento dos produtos do e-commerce")
public class ProdutoController {

    @Autowired
    private ProdutoService servicoDeProduto;

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@Valid @RequestBody ProdutoRequestDTO dadosRecebidos) {
        Produto produtoCriado = servicoDeProduto.salvarNovoProduto(dadosRecebidos);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoCriado);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = servicoDeProduto.buscarTodos();
        return ResponseEntity.ok(produtos);
    }
}