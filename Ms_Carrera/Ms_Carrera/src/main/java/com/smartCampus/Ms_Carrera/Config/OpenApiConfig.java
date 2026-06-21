package com.smartCampus.Ms_Carrera.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig  {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
            .title("Ms_Carreras API")
            .version("1.0.0")
            .description("API REST para gestion de carrera y asignaturas")
            .contact(new Contact()
                    .name("SmartCampus Team")
                    .email("tu-email@gmail.com")))
            .addServersItem(new Server()
                .url("http://localhost:8060")
                .description("Servidor de desarrollo Local"));

    }

}
