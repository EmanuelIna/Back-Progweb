package com.example.API_commerce_pw.controllers;
import com.example.API_commerce_pw.dtos.*;
import com.example.API_commerce_pw.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@Tag(name = "Produtos")
@RestController @RequestMapping("/api/produtos") @RequiredArgsConstructor
public class ProdutoController {
 private final ProdutoService service;
 @Operation(summary = "Cadastrar produto", description = "Somente ADMIN. Retorna o produto criado e o cabeçalho Location.")
 @SecurityRequirement(name = "bearerAuth")
 @ApiResponse(responseCode = "201", description = "Produto criado")
 @ApiResponse(responseCode = "400", description = "Dados inválidos")
 @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido")
 @ApiResponse(responseCode = "403", description = "Requer ADMIN")
 @PostMapping public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoRequestDTO dto) {
  var p=service.salvarNovoProduto(dto); return ResponseEntity.created(URI.create("/api/produtos/"+p.idProduto())).body(p);
 }
 @Operation(summary = "Listar produtos", description = "Consulta pública do catálogo.")
 @ApiResponse(responseCode = "200", description = "Lista de produtos")
 @GetMapping public ResponseEntity<List<ProdutoResponseDTO>> listar() { return ResponseEntity.ok(service.buscarTodos()); }
 @Operation(summary = "Consultar produto", description = "Consulta pública por idProduto (UUID).")
 @ApiResponse(responseCode = "200", description = "Produto encontrado")
 @ApiResponse(responseCode = "404", description = "Produto não encontrado")
 @GetMapping("/{id}") public ResponseEntity<ProdutoResponseDTO> buscar(@PathVariable String id) { return ResponseEntity.ok(service.buscar(id)); }
 @Operation(summary = "Atualizar produto", description = "Somente ADMIN. Substitui todos os campos do produto.")
 @SecurityRequirement(name = "bearerAuth")
 @ApiResponse(responseCode = "200", description = "Produto atualizado")
 @ApiResponse(responseCode = "400", description = "Dados inválidos")
 @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido")
 @ApiResponse(responseCode = "403", description = "Requer ADMIN")
 @ApiResponse(responseCode = "404", description = "Produto não encontrado")
 @PutMapping("/{id}") public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable String id,@Valid @RequestBody ProdutoRequestDTO dto) { return ResponseEntity.ok(service.atualizar(id,dto)); }
 @Operation(summary = "Excluir produto", description = "Somente ADMIN. Remove o produto do catálogo.")
 @SecurityRequirement(name = "bearerAuth")
 @ApiResponse(responseCode = "204", description = "Produto excluído, sem corpo")
 @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido")
 @ApiResponse(responseCode = "403", description = "Requer ADMIN")
 @ApiResponse(responseCode = "404", description = "Produto não encontrado")
 @DeleteMapping("/{id}") public ResponseEntity<Void> excluir(@PathVariable String id) { service.excluir(id); return ResponseEntity.noContent().build(); }
}
