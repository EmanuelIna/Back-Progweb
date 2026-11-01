# API Commerce PW

API REST de e-commerce: Java 25, Spring Boot 4.1.1, Maven, Spring Data JPA, PostgreSQL/Supabase, Spring Security/JWT, Lombok e Validation.
Arquitetura Controller → Service → Repository, entradas/saídas em DTOs.
Front-end não modificado; há diferenças de contrato descritas ao final.

## 1. Pré-requisitos
- JDK 25 instalado. Nesta máquina: C:\Program Files\Java\jdk-25.0.4.1.
- Internet para o Maven resolver dependências e conectar ao Supabase.
- PowerShell na raiz do back-end. Não é necessário instalar Maven globalmente nem PostgreSQL local para usar o Supabase.
- O arquivo application-local.properties já foi configurado nesta máquina e é ignorado pelo Git. Ele contém as credenciais JDBC e segredos JWT/carrinho. Não compartilhar nem versionar.

## 2. Iniciar a API com o Supabase
Abra um terminal PowerShell:
```powershell
Set-Location 'C:\Users\manau\OneDrive\Documentos\Projetos\ProgWeb-trab\API-commerce-pw'
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.4.1'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
java -version
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.profiles=supabase'
```
Mantenha o terminal aberto. Aguarde a mensagem Started ApiCommercePwApplication.
API disponível em http://localhost:8080.
Para encerrar, pressione Ctrl+C no terminal.
O comando deve rodar na raiz do projeto para importar application-local.properties.

Se a porta 8080 estiver ocupada:
```powershell
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.profiles=supabase' '-Dspring-boot.run.arguments=--server.port=8081'
```
Nesse caso, troque 8080 por 8081 em todos os links e comandos abaixo.

## 3. Acessar a documentação Swagger/OpenAPI
Com a API em execução:
- Swagger UI interativo: http://localhost:8080/swagger-ui/index.html
- Atalho para o Swagger: http://localhost:8080/swagger-ui.html
- Especificação OpenAPI JSON: http://localhost:8080/v3/api-docs
- Especificação OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml

A documentação abre sem login. Endpoints protegidos continuam exigindo JWT/perfil.
Os 12 endpoints estão organizados em Produtos, Usuários e autenticação, e Carrinho.
O Swagger apresenta DTOs, validações, exemplos de entrada, descrições, status HTTP e requisitos de autorização.
Abra uma operação, clique Try it out, preencha o corpo e clique Execute.
Para confirmar a conexão com o banco, execute GET /api/produtos: 200 com [] é válido quando o catálogo está vazio.

## 4. Fazer login e usar Authorize no Swagger
1. Em POST /api/usuarios, crie sua conta com nome, email e senha.
2. Em POST /api/usuarios/login, informe o mesmo email e senha.
3. Copie apenas o campo token da resposta. JWT dura 3600 segundos.
4. Clique Authorize no topo do Swagger, cole o token em bearerAuth e confirme. Não digite Bearer: o Swagger acrescenta esse prefixo.
5. Execute as rotas protegidas. Ao expirar, faça login novamente e atualize Authorize.
6. Use Logout na janela Authorize para remover o token do Swagger.

Exemplo de cadastro (credenciais fictícias da API, sem relação com a senha do banco):
```json
{"nome":"Aluno","email":"aluno@example.com","senha":"SenhaDeTeste2026!"}
```
Exemplo de login:
```json
{"email":"aluno@example.com","senha":"SenhaDeTeste2026!"}
```
Cadastro sempre cria CLIENTE. Senhas são codificadas com BCrypt e limitadas a 72 bytes UTF-8.
O login retorna idUsuario, nome, email, perfil, token, tokenType e expiresIn.
Não há refresh token ou revogação; mudanças de perfil não revogam tokens já emitidos.

## 5. Obter perfil ADMIN para testar o CRUD
Cadastre uma conta e, no SQL Editor do projeto api-commerce-pw do Supabase, execute:
```sql
UPDATE commerce.usuarios
SET perfil = 'ADMIN'
WHERE email = 'aluno@example.com';
```
Use o email da conta que deseja promover. Faça novo login e substitua o token em Authorize.
Não há administrador com senha fixa nem endpoint público para escolher o perfil.
ADMIN é necessário para criar/alterar/excluir produtos e listar usuários.

Exemplo de produto:
```json
{
  "nomeDoProduto":"Livro Java",
  "descricaoDoProduto":"Introdução ao Spring Boot",
  "precoDoProduto":49.90,
  "quantidadeEmEstoque":10
}
```
POST retorna 201, DTO do produto e cabeçalho Location. Copie idProduto para consultas, alterações e carrinho.
PUT exige todos os campos. DELETE bem-sucedido retorna 204 sem corpo.

## 6. Testar carrinho e cookies
Com token de CLIENTE ou ADMIN:
1. Consulte GET /api/carrinho: sem cookie, retorna carrinho vazio.
2. Use POST /api/carrinho/itens com idProduto existente e quantidade:
```json
{"idProduto":"UUID-RETORNADO-PELO-CADASTRO-DO-PRODUTO","quantidade":2}
```
3. Consulte GET /api/carrinho novamente para conferir quantidade, subtotal e total.
4. Use DELETE /api/carrinho/itens/{id} para remover o item ou DELETE /api/carrinho para limpar.

O navegador recebe e envia o cookie automaticamente. Não preencha Cookie nem copie seu valor no Swagger: navegadores controlam esse cabeçalho.
Use o Swagger da mesma origem da API para os testes. Cookie HttpOnly não é acessível ao JavaScript.
ResponseCookie e @CookieValue mantêm o carrinho por até 3 dias após uma alteração: Max-Age=259200, SameSite=Lax, Path=/api/carrinho.
O conteúdo é assinado com HMAC-SHA256 e tem expiração interna. Guarda IDs/quantidades; preços e estoque vêm do banco.
Máximo 20 produtos distintos e 999 unidades por item. Adicionar soma a quantidade. GET não renova a validade.
O carrinho pertence ao navegador e pode ser compartilhado por contas no mesmo navegador. Não reserva estoque.

## 7. Rotas e permissões

| Método | Rota | Acesso | Sucesso |
| --- | --- | --- | --- |
| POST | /api/usuarios | Público | 201 |
| POST | /api/usuarios/login | Público | 200 |
| GET | /api/usuarios | ADMIN | 200 |
| GET | /api/produtos | Público | 200 |
| GET | /api/produtos/{id} | Público | 200 |
| POST | /api/produtos | ADMIN | 201 |
| PUT | /api/produtos/{id} | ADMIN | 200 |
| DELETE | /api/produtos/{id} | ADMIN | 204 |
| GET | /api/carrinho | Autenticado | 200 |
| POST | /api/carrinho/itens | Autenticado | 200 |
| DELETE | /api/carrinho/itens/{id} | Autenticado | 200 |
| DELETE | /api/carrinho | Autenticado | 204 |

400: validação/cookie/limite inválido; 401: credenciais ou JWT inválidos/ausentes; 403: perfil insuficiente; 404: produto inexistente; 409: email duplicado ou estoque insuficiente.
Nas rotas protegidas, clientes externos enviam Authorization: Bearer <token>.
Para cookies via fetch, também use credentials: include.

## 8. Configurar em outra máquina
O arquivo local não acompanha o Git. Crie application-local.properties na raiz com:
```properties
SUPABASE_DB_URL=jdbc:postgresql://aws-1-sa-east-1.pooler.supabase.com:5432/postgres?sslmode=require
SUPABASE_DB_USERNAME=postgres.fpanfjskolzvjjvjpvvk
SUPABASE_DB_PASSWORD=<senha-do-banco>
JWT_SECRET=<segredo-aleatorio-com-pelo-menos-32-bytes>
CART_SECRET=<outro-segredo-aleatorio-com-pelo-menos-32-bytes>
```
Essas propriedades também podem ser fornecidas como variáveis de ambiente.
.env.example é apenas referência; Spring não carrega .env automaticamente.
O profile supabase importa application-local.properties e usa schema commerce com ddl-auto=validate.
Não execute novamente database/commerce-schema.sql no banco já configurado: ele é uma cópia da estrutura inicial.
CORS_ORIGINS permite origens separadas por vírgula; padrão http://localhost:5173,http://localhost:3000.
Em HTTPS, COOKIE_SECURE=true. SameSite=Lax pressupõe implantação apropriada ao mesmo site.

## 9. Testes e diagnóstico
```powershell
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.4.1'
.\mvnw.cmd test
```
Testes usam H2, sem alterar o Supabase. Cobrem CRUD, login, permissões, JWT, validação, estoque, cookies e acesso à documentação.
Mockito usa javaagent no Surefire para funcionar com Java 25.
- invalid target release 25: conferir JAVA_HOME e java -version.
- Porta em uso: encerrar outra instância ou usar --server.port=8081.
- password authentication failed: conferir credenciais locais e projeto.
- Missing schema/table ou Schema-validation: confirmar schema commerce e migração do banco.
- Swagger 401: atualizar para este código e reiniciar a aplicação.
- Swagger não conecta: a API precisa estar em execução; os links são locais.
- JWT expirado: fazer novo login e atualizar Authorize.
- 403 ao criar produto: promover a conta para ADMIN e fazer novo login.
- Não use chaves anon/publishable/service_role como senha JDBC.

## 10. Banco e integração
Projeto Supabase: api-commerce-pw, Emanuel Org, sa-east-1 (São Paulo).
Dashboard: https://supabase.com/dashboard/project/fpanfjskolzvjjvjpvvk
Custo informado na criação: US$ 0/mês. Banco independente do Montadorimetro.
Schema commerce privado, tabelas produtos/usuarios, RLS ativo e acesso anon/authenticated revogado.
Sem políticas públicas intencionalmente; o acesso atual ocorre pelo back-end JDBC.
Informação do advisor sobre ausência de políticas: https://supabase.com/docs/guides/database/database-linter?lint=0008_rls_enabled_no_policy
Conexão real validada em 2026-11-01 com PostgreSQL 17.11 e GET /api/produtos HTTP 200.

O front existente usa /api/products, /api/cart, Firebase e IDs numéricos.
Esta API mantém contratos em português e UUIDs. A integração completa, Firebase e pagamentos estão pendentes.
Nenhum arquivo do front-end foi alterado.

## Referências
- https://springdoc.org/
- https://supabase.com/docs/guides/getting-started/quickstarts/spring-boot
- https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
