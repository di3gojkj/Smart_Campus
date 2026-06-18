package com.smartCampus.Ms_Evaluacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Component
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("Ms_Evaluaciones API")
                    .version("1.0.0")
                    .description("API REST para la gestión de Evaluaciones Académicas y sus correspondientes Tipos de Evaluación")
                    .contact(new Contact()
                        .name("SmartCampus Team")
                        .email("tu-email@gmail.com")))
                .addServersItem(new Server()
                    .url("http://localhost:8090") 
                    .description("Servidor local de desarrollo"));
    }

}
