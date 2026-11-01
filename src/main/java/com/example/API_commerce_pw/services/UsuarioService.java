package com.example.API_commerce_pw.services;
import com.example.API_commerce_pw.dtos.*;
import com.example.API_commerce_pw.models.*;
import com.example.API_commerce_pw.models.enums.PerfilUsuario;
import com.example.API_commerce_pw.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import java.util.*;
@Service @RequiredArgsConstructor @Transactional
public class UsuarioService {
 private final UsuarioRepository repository;
 private final PasswordEncoder passwords;
 public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO d) {
  if(d.getSenha().getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Senha deve conter no máximo 72 bytes UTF-8");
  String email=d.getEmail().trim().toLowerCase(Locale.ROOT);
  if(repository.existsByEmail(email)) throw new ResponseStatusException(HttpStatus.CONFLICT,"Email já cadastrado");
  Usuario u=new Usuario(null,d.getNome(),email,passwords.encode(d.getSenha()),PerfilUsuario.CLIENTE);
  return dto(repository.save(u));
 }
 @Transactional(readOnly=true) public List<UsuarioResponseDTO> buscarTodos() { return repository.findAll().stream().map(this::dto).toList(); }
 @Transactional(readOnly=true) public UsuarioResponseDTO fazerLogin(LoginRequestDTO d) {
  if(d.getSenha().getBytes(java.nio.charset.StandardCharsets.UTF_8).length>72) throw invalid();
  Usuario u=repository.findByEmail(d.getEmail().trim().toLowerCase(Locale.ROOT)).orElseThrow(this::invalid);
  if(!passwords.matches(d.getSenha(),u.getSenha())) throw invalid();
  return dto(u);
 }
 private ResponseStatusException invalid() { return new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciais inválidas"); }
 private UsuarioResponseDTO dto(Usuario u) { var d=new UsuarioResponseDTO(); d.setIdUsuario(u.getIdUsuario()); d.setNome(u.getNome()); d.setEmail(u.getEmail()); d.setPerfil(u.getPerfil()); return d; }
}
