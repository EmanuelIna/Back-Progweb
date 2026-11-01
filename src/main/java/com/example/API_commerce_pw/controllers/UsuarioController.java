package com.example.API_commerce_pw.controllers;
import com.example.API_commerce_pw.dtos.*;
import com.example.API_commerce_pw.services.*;
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
@Tag(name = "Usuários e autenticação")
@RestController @RequestMapping("/api/usuarios") @RequiredArgsConstructor
public class UsuarioController {
 private final UsuarioService service;
 private final TokenService tokens;
 @Operation(summary = "Cadastrar usuário", description = "Cadastro público. Cria perfil CLIENTE. Senha aceita até 72 bytes UTF-8 e é armazenada com BCrypt.")
 @ApiResponse(responseCode = "201", description = "Usuário criado")
 @ApiResponse(responseCode = "400", description = "Dados inválidos")
 @ApiResponse(responseCode = "409", description = "Email já cadastrado")
 @PostMapping public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO dto) {
  var u=service.cadastrarUsuario(dto); return ResponseEntity.created(URI.create("/api/usuarios/"+u.getIdUsuario())).body(u);
 }
 @Operation(summary = "Listar usuários", description = "Somente ADMIN. Retorna DTOs sem senhas.")
 @SecurityRequirement(name = "bearerAuth")
 @ApiResponse(responseCode = "200", description = "Lista de usuários")
 @ApiResponse(responseCode = "401", description = "JWT ausente ou inválido")
 @ApiResponse(responseCode = "403", description = "Requer ADMIN")
 @GetMapping public ResponseEntity<List<UsuarioResponseDTO>> listar() { return ResponseEntity.ok(service.buscarTodos()); }
 @Operation(summary = "Autenticar usuário", description = "Retorna dados do usuário, token JWT, tokenType Bearer e expiresIn 3600. Copie token para Authorize.")
 @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida")
 @ApiResponse(responseCode = "400", description = "Dados inválidos")
 @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
 @PostMapping("/login") public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
  var u=service.fazerLogin(dto); return ResponseEntity.ok(new LoginResponseDTO(u.getIdUsuario(),u.getNome(),u.getEmail(),u.getPerfil(),tokens.gerar(u),"Bearer",3600));
 }
}
