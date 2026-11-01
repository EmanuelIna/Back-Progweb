package com.example.API_commerce_pw.services;
import com.example.API_commerce_pw.dtos.*;
import com.example.API_commerce_pw.models.Produto;
import com.example.API_commerce_pw.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class ProdutoService {
 private final ProdutoRepository repository;
 public ProdutoResponseDTO salvarNovoProduto(ProdutoRequestDTO dto) { return persistir(new Produto(),dto); }
 public ProdutoResponseDTO atualizar(String id,ProdutoRequestDTO dto) { return persistir(entidade(id),dto); }
 private ProdutoResponseDTO persistir(Produto p,ProdutoRequestDTO dto) {
  p.setNomeDoProduto(dto.getNomeDoProduto()); p.setDescricaoDoProduto(dto.getDescricaoDoProduto());
  p.setPrecoDoProduto(dto.getPrecoDoProduto()); p.setQuantidadeEmEstoque(dto.getQuantidadeEmEstoque());
  return ProdutoResponseDTO.from(repository.save(p));
 }
 @Transactional(readOnly=true) public List<ProdutoResponseDTO> buscarTodos() { return repository.findAll().stream().map(ProdutoResponseDTO::from).toList(); }
 @Transactional(readOnly=true) public ProdutoResponseDTO buscar(String id) { return ProdutoResponseDTO.from(entidade(id)); }
 private Produto entidade(String id) { return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Produto não encontrado")); }
 public void excluir(String id) { repository.delete(entidade(id)); }
}
