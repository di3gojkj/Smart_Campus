package com.smartcampus.msAsignatura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
            .title("Ms_Asignaturas API")
            .version("1.0.0")
            .description("API REST para gestion de Asignaturas")
            .contact(new Contact()
                .name("Tu Nombre/Equipo")
                .email("tu-email@gmail.com")))
            .addServersItem(new Server()
                .url("http://localhost:8070")
                .description("Servidor local de desarrollo"));
    }
}
