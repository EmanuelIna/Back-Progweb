package com.example.API_commerce_pw.services;
import com.example.API_commerce_pw.dtos.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.*;
import java.util.*;
@Service
public class CarrinhoService {
 public static final String COOKIE="carrinho";
 private final ProdutoService produtos;
 private final byte[] secret;
 private final boolean secure;
 public CarrinhoService(ProdutoService produtos,@Value("${app.cart.secret}") String secret,@Value("${app.cookie.secure:false}") boolean secure) {
  this.produtos=produtos; this.secret=secret.getBytes(StandardCharsets.UTF_8); this.secure=secure;
  if(this.secret.length<32) throw new IllegalArgumentException("CART_SECRET deve conter pelo menos 32 bytes");
 }
 public Map<String,Integer> ler(String cookie) {
  var itens=new LinkedHashMap<String,Integer>();
  if(cookie==null || cookie.isBlank()) return itens;
  try {
   if(cookie.length()>3800) throw new IllegalArgumentException();
   String[] parts=cookie.split("\\.",-1);
   if(parts.length!=2 || !MessageDigest.isEqual(assinar(parts[0]),Base64.getUrlDecoder().decode(parts[1]))) throw new IllegalArgumentException();
   String[] data=new String(Base64.getUrlDecoder().decode(parts[0]),StandardCharsets.UTF_8).split(";",-1);
   if(Long.parseLong(data[0])<=Instant.now().getEpochSecond()) return itens;
   for(int i=1;i<data.length;i++) {
    String[] entry=data[i].split(":");
    UUID.fromString(entry[0]); int q=Integer.parseInt(entry[1]);
    if(q<1 || q>999 || itens.put(entry[0],q)!=null) throw new IllegalArgumentException();
   }
   if(itens.size()>20) throw new IllegalArgumentException();
   return itens;
  } catch(RuntimeException e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cookie do carrinho inválido; limpe o carrinho"); }
 }
 public void adicionar(Map<String,Integer> itens,CarrinhoItemRequestDTO dto) {
  int quantidade=itens.getOrDefault(dto.idProduto(),0)+dto.quantidade();
  if(quantidade>999 || (!itens.containsKey(dto.idProduto()) && itens.size()>=20)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Limite do carrinho excedido");
  validar(dto.idProduto(),quantidade); itens.put(dto.idProduto(),quantidade);
 }
 private ProdutoResponseDTO validar(String id,int q) {
  var p=produtos.buscar(id);
  if(q>p.quantidadeEmEstoque()) throw new ResponseStatusException(HttpStatus.CONFLICT,"Estoque insuficiente");
  return p;
 }
 public CarrinhoResponseDTO resumo(Map<String,Integer> itens) {
  var result=new ArrayList<CarrinhoResponseDTO.Item>(); var total=java.math.BigDecimal.ZERO;
  for(var e:itens.entrySet()) { var p=validar(e.getKey(),e.getValue()); var subtotal=p.precoDoProduto().multiply(java.math.BigDecimal.valueOf(e.getValue())); total=total.add(subtotal); result.add(new CarrinhoResponseDTO.Item(p,e.getValue(),subtotal)); }
  return new CarrinhoResponseDTO(result,total);
 }
 public ResponseCookie cookie(Map<String,Integer> itens) {
  StringBuilder raw=new StringBuilder().append(Instant.now().plus(Duration.ofDays(3)).getEpochSecond());
  itens.forEach((id,q)->raw.append(";").append(id).append(":").append(q));
  String payload=Base64.getUrlEncoder().withoutPadding().encodeToString(raw.toString().getBytes(StandardCharsets.UTF_8));
  String value=payload+"."+Base64.getUrlEncoder().withoutPadding().encodeToString(assinar(payload));
  return construir(value,Duration.ofDays(3));
 }
 public ResponseCookie limpar() { return construir("",Duration.ZERO); }
 private ResponseCookie construir(String value,Duration age) { return ResponseCookie.from(COOKIE,value).httpOnly(true).secure(secure).sameSite("Lax").path("/api/carrinho").maxAge(age).build(); }
 private byte[] assinar(String payload) {
  try { Mac mac=Mac.getInstance("HmacSHA256"); mac.init(new SecretKeySpec(secret,"HmacSHA256")); return mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)); }
  catch(java.security.GeneralSecurityException e) { throw new IllegalStateException(e); }
 }
}
