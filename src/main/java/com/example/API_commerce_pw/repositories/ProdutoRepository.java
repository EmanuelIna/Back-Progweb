package com.example.API_commerce_pw.repositories;
import com.example.API_commerce_pw.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProdutoRepository extends JpaRepository<Produto,String> {}
