package com.diego.Ms_Gestion_Lista.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
            .title("Ms_Gestion_Lista API")
            .version("1.0.0")
            .description("API REST para la gestión de Inscripciones y Calificaciones académicas de los estudiantes.")
            .contact(new Contact()
                .name("Diego - SmartCampus Team")
                .email("diego.rivas@duocuc.cl")))
            .addServersItem(new Server()
                .url("http://localhost:8085")
                .description("Servidor local de desarrollo"));
    }
}