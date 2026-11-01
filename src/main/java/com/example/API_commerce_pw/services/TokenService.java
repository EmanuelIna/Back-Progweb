package com.example.API_commerce_pw.services;
import com.example.API_commerce_pw.dtos.UsuarioResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import java.time.Instant;
@Service @RequiredArgsConstructor
public class TokenService {
 private final JwtEncoder encoder;
 public String gerar(UsuarioResponseDTO u) {
  Instant now=Instant.now();
  var claims=JwtClaimsSet.builder().issuer("commerce-api").subject(u.getIdUsuario()).issuedAt(now).expiresAt(now.plusSeconds(3600)).claim("role",u.getPerfil().name()).build();
  return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(),claims)).getTokenValue();
 }
}
