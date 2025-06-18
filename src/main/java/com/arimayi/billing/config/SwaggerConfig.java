package com.arimayi.billing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Swagger/OpenAPI
 */
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Module de Facturation AriMayi")
                .version("1.0.0")
                .description("API REST pour la gestion des clients et factures")
                .contact(new Contact()
                    .name("Équipe AriMayi")
                    .email("contact@arimayi.com")))
            .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("basicAuth", 
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic")));
    }
}
