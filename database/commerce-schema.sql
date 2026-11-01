create schema commerce;
revoke all on schema commerce from public, anon, authenticated;
create table commerce.produtos (
 id_produto varchar(255) primary key,
 nome_do_produto varchar(255) not null,
 descricao_do_produto varchar(255) not null,
 preco_do_produto numeric(14,2) not null check (preco_do_produto >= 0),
 quantidade_em_estoque integer not null check (quantidade_em_estoque >= 0)
);
create table commerce.usuarios (
 id_usuario varchar(255) primary key,
 nome varchar(255) not null,
 email varchar(255) not null unique,
 senha varchar(255) not null,
 perfil varchar(255) not null check (perfil in ('ADMIN','CLIENTE'))
);
alter table commerce.produtos enable row level security;
alter table commerce.usuarios enable row level security;
revoke all on all tables in schema commerce from public, anon, authenticated;
comment on schema commerce is 'Private Spring Boot e-commerce data. Access through server-side JDBC only.';
