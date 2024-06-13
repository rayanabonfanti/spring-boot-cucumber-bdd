package com.desafio.serasa.experian.documentacao;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Desafio Serasa Experian",
                        email = "rayanabonfanti@gmail.com",
                        url = "https://github.com/rayanabonfanti"
                ),
                description = "OpenApi documentation for Desafio Serasa Experian",
                title = "OpenApi specification - Desafio Serasa Experian",
                version = "1.0",
                license = @License(
                        name = "Licence Desafio",
                        url = "https://github.com/rayanabonfanti"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local TEST",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Local PROD",
                        url = "https://localhost:8443"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }

)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
