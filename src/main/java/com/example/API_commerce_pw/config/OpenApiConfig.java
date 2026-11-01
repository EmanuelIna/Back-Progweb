package com.example.API_commerce_pw.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI commerceOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("API Commerce PW").version("1.0")
                .description("""
                    API de e-commerce com PostgreSQL, JWT e carrinho em cookie.
                    Cadastre uma conta em POST /api/usuarios e faça login em POST /api/usuarios/login.
                    Copie o campo token para Authorize (somente o token, sem o prefixo Bearer).
                    JWT expira em 3600 segundos. Cadastro sempre cria CLIENTE; alterações de produtos e listagem de usuários exigem ADMIN.
                    O carrinho usa cookie HttpOnly assinado por até 3 dias. O navegador recebe e envia esse cookie automaticamente.
                    Use o Swagger na mesma origem da API. Cookies HttpOnly não devem ser preenchidos manualmente.
                    """))
            .components(new Components().addSecuritySchemes("bearerAuth",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
