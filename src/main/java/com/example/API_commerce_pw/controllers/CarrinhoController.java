package com.example.API_commerce_pw.controllers;
import com.example.API_commerce_pw.dtos.*;
import com.example.API_commerce_pw.services.CarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@Tag(name = "Carrinho")
@RestController @RequestMapping("/api/carrinho") @RequiredArgsConstructor
public class CarrinhoController {
 private final CarrinhoService service;
 @Operation(summary = "Consultar carrinho", description = "Requer JWT. Lê o cookie carrinho automaticamente, consulta preços e estoque no banco e calcula o total. Sem cookie retorna carrinho vazio. Não renova a validade.")
 @SecurityRequirement(name = "bearerAuth")
 @ApiResponse(responseCode = "200", description = "Resumo do carrinho")
 @ApiResponse(responseCode = "400", description = "Cookie inválido")
 @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido")
 @ApiResponse(responseCode = "404", description = "Produto removido do catálogo")
 @ApiResponse(responseCode = "409", description = "Estoque insuficiente")
 @GetMapping public ResponseEntity<CarrinhoResponseDTO> buscar(@Parameter(hidden = true) @CookieValue(name=CarrinhoService.COOKIE,required=false) String cookie) {
  return ResponseEntity.ok(service.resumo(service.ler(cookie)));
 }
 @Operation(summary = "Adicionar item ao carrinho", description = "Soma a quantidade ao item. Máximo 20 produtos distintos e 999 unidades por item. Envia Set-Cookie com Max-Age=259200, HttpOnly e SameSite=Lax. Não reserva estoque.")
 @SecurityRequirement(name = "bearerAuth")
 @ApiResponse(responseCode = "200", description = "Carrinho atualizado e cookie enviado")
 @ApiResponse(responseCode = "400", description = "Dados, cookie ou limite inválidos")
 @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido")
 @ApiResponse(responseCode = "404", description = "Produto não encontrado")
 @ApiResponse(responseCode = "409", description = "Estoque insuficiente")
 @PostMapping("/itens") public ResponseEntity<CarrinhoResponseDTO> adicionar(@Parameter(hidden = true) @CookieValue(name=CarrinhoService.COOKIE,required=false) String cookie,@Valid @RequestBody CarrinhoItemRequestDTO dto) {
  var itens=service.ler(cookie); service.adicionar(itens,dto); return resposta(itens);
 }
 @Operation(summary = "Remover item do carrinho", description = "Remove o item pelo idProduto e envia o cookie atualizado. O carrinho pertence ao navegador.")
 @SecurityRequirement(name = "bearerAuth")
 @ApiResponse(responseCode = "200", description = "Carrinho atualizado e cookie enviado")
 @ApiResponse(responseCode = "400", description = "Cookie inválido")
 @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido")
 @ApiResponse(responseCode = "404", description = "Outro produto do carrinho não existe")
 @ApiResponse(responseCode = "409", description = "Estoque insuficiente em outro item")
 @DeleteMapping("/itens/{id}") public ResponseEntity<CarrinhoResponseDTO> remover(@Parameter(hidden = true) @CookieValue(name=CarrinhoService.COOKIE,required=false) String cookie,@PathVariable String id) {
  var itens=service.ler(cookie); itens.remove(id); return resposta(itens);
 }
 @Operation(summary = "Limpar carrinho", description = "Remove o cookie com Max-Age=0, inclusive quando seu conteúdo é inválido.")
 @SecurityRequirement(name = "bearerAuth")
 @ApiResponse(responseCode = "204", description = "Cookie removido, sem corpo")
 @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido")
 @DeleteMapping public ResponseEntity<Void> limpar() { return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE,service.limpar().toString()).build(); }
 private ResponseEntity<CarrinhoResponseDTO> resposta(Map<String,Integer> itens) {
  var dto=service.resumo(itens); return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,service.cookie(itens).toString()).body(dto);
 }
}
