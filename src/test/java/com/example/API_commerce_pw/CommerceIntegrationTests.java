package com.example.API_commerce_pw;
import com.example.API_commerce_pw.models.*;
import com.example.API_commerce_pw.models.enums.PerfilUsuario;
import com.example.API_commerce_pw.repositories.*;
import com.example.API_commerce_pw.services.*;
import com.example.API_commerce_pw.dtos.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import jakarta.servlet.http.Cookie;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc
class CommerceIntegrationTests {
 @Autowired MockMvc mvc;
 @Autowired ProdutoRepository produtos;
 @Autowired UsuarioRepository usuarios;
 @Autowired PasswordEncoder passwords;
 @Autowired TokenService tokens;
 @Autowired JwtDecoder decoder;
 @Autowired JwtEncoder encoder;
 @Autowired CarrinhoService carrinho;
 String admin,cliente,id;
 @BeforeEach void setup() {
  produtos.deleteAll(); usuarios.deleteAll();
  var u=usuarios.save(new Usuario(null,"Admin","admin@test.com",passwords.encode("senha"),PerfilUsuario.ADMIN));
  var dto=new UsuarioResponseDTO(); dto.setIdUsuario(u.getIdUsuario()); dto.setPerfil(u.getPerfil()); admin=tokens.gerar(dto);
  dto.setPerfil(PerfilUsuario.CLIENTE); cliente=tokens.gerar(dto);
  id=produtos.save(new Produto(null,"Livro","Java",new BigDecimal("10.00"),5)).getIdProduto();
 }
 @Test void swaggerDocumentsPublicAndProtectedRoutes() throws Exception {
  mvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
  mvc.perform(get("/v3/api-docs/swagger-config")).andExpect(status().isOk());
  mvc.perform(get("/v3/api-docs"))
   .andExpect(status().isOk())
   .andExpect(jsonPath("$.info.title").value("API Commerce PW"))
   .andExpect(jsonPath("$.components.securitySchemes.bearerAuth.scheme").value("bearer"))
   .andExpect(jsonPath("$.paths['/api/produtos'].get.summary").value("Listar produtos"))
   .andExpect(jsonPath("$.paths['/api/produtos'].post.responses['201']").exists())
   .andExpect(jsonPath("$.paths['/api/produtos'].post.security[0].bearerAuth").exists())
   .andExpect(jsonPath("$.paths['/api/usuarios/login'].post.responses['401']").exists())
   .andExpect(jsonPath("$.paths['/api/carrinho'].delete.responses['204']").exists());
 }
 @Test void securityAndCrud() throws Exception {
  String json="{\"nomeDoProduto\":\"Novo\",\"descricaoDoProduto\":\"Teste\",\"precoDoProduto\":12.5,\"quantidadeEmEstoque\":3}";
  mvc.perform(get("/api/produtos")).andExpect(status().isOk());
  mvc.perform(post("/api/produtos").contentType("application/json").content(json)).andExpect(status().isUnauthorized());
  mvc.perform(post("/api/produtos").header("Authorization","Bearer "+cliente).contentType("application/json").content(json)).andExpect(status().isForbidden());
  mvc.perform(post("/api/produtos").header("Authorization","Bearer "+admin).contentType("application/json").content(json)).andExpect(status().isCreated()).andExpect(jsonPath("$.idProduto").exists());
  mvc.perform(put("/api/produtos/"+id).header("Authorization","Bearer "+admin).contentType("application/json").content(json)).andExpect(status().isOk()).andExpect(jsonPath("$.nomeDoProduto").value("Novo"));
  mvc.perform(delete("/api/produtos/"+id).header("Authorization","Bearer "+admin)).andExpect(status().isNoContent());
  mvc.perform(get("/api/produtos/"+id)).andExpect(status().isNotFound());
 }
 @Test void registrationAndLogin() throws Exception {
  String body="{\"nome\":\"Cliente\",\"email\":\"cliente@test.com\",\"senha\":\"segredo\"}";
  mvc.perform(post("/api/usuarios").contentType("application/json").content(body)).andExpect(status().isCreated()).andExpect(jsonPath("$.senha").doesNotExist()).andExpect(jsonPath("$.perfil").value("CLIENTE"));
  assertTrue(passwords.matches("segredo",usuarios.findByEmail("cliente@test.com").orElseThrow().getSenha()));
  mvc.perform(post("/api/usuarios").contentType("application/json").content(body)).andExpect(status().isConflict());
  mvc.perform(post("/api/usuarios/login").contentType("application/json").content("{\"email\":\"cliente@test.com\",\"senha\":\"segredo\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.token").isString()).andExpect(jsonPath("$.nome").value("Cliente"));
  mvc.perform(post("/api/usuarios/login").contentType("application/json").content("{\"email\":\"cliente@test.com\",\"senha\":\"errada\"}")).andExpect(status().isUnauthorized());
 }
 @Test void cookiesRoundTripAndTampering() throws Exception {
  var response=mvc.perform(post("/api/carrinho/itens").header("Authorization","Bearer "+cliente).contentType("application/json").content("{\"idProduto\":\""+id+"\",\"quantidade\":2}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.total").value(20)).andReturn().getResponse();
  String header=response.getHeader("Set-Cookie"); assertNotNull(header); assertTrue(header.contains("Max-Age=259200")); assertTrue(header.contains("HttpOnly")); assertTrue(header.contains("SameSite=Lax"));
  String value=header.substring("carrinho=".length(),header.indexOf(';'));
  mvc.perform(get("/api/carrinho").header("Authorization","Bearer "+cliente).cookie(new Cookie("carrinho",value))).andExpect(status().isOk()).andExpect(jsonPath("$.itens[0].quantidade").value(2));
  mvc.perform(get("/api/carrinho").header("Authorization","Bearer "+cliente).cookie(new Cookie("carrinho","x"+value))).andExpect(status().isBadRequest());
  mvc.perform(delete("/api/carrinho").header("Authorization","Bearer "+cliente)).andExpect(status().isNoContent()).andExpect(header().string("Set-Cookie",org.hamcrest.Matchers.containsString("Max-Age=0")));
 }
 @Test void stockAndValidation() throws Exception {
  mvc.perform(post("/api/carrinho/itens").header("Authorization","Bearer "+cliente).contentType("application/json").content("{\"idProduto\":\""+id+"\",\"quantidade\":6}")).andExpect(status().isConflict());
  mvc.perform(post("/api/carrinho/itens").header("Authorization","Bearer "+cliente).contentType("application/json").content("{\"idProduto\":\""+id+"\",\"quantidade\":0}")).andExpect(status().isBadRequest());
  mvc.perform(post("/api/produtos").header("Authorization","Bearer "+admin).contentType("application/json").content("{}")).andExpect(status().isBadRequest());
 }

 @Test void expiredJwtIsRejected() throws Exception {
  var now=java.time.Instant.now();
  var claims=JwtClaimsSet.builder().issuer("commerce-api").subject("test").issuedAt(now.minusSeconds(600)).expiresAt(now.minusSeconds(300)).build();
  String expired=encoder.encode(JwtEncoderParameters.from(JwsHeader.with(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256).build(),claims)).getTokenValue();
  mvc.perform(get("/api/carrinho").header("Authorization","Bearer "+expired)).andExpect(status().isUnauthorized());
 }
 @Test void jwtSignatureAndIssuer() {
  assertEquals("commerce-api",decoder.decode(admin).getClaimAsString("iss"));
  assertThrows(JwtException.class,()->decoder.decode("x"+admin));
 }
}
