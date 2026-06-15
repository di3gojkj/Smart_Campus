package com.SCampus.curso_seccion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Cursos y Secciones (SCampus)")
                        .version("1.0.0")
                        .description("Microservicio encargado de administrar las aulas, asignaturas, secciones y la distribución de cursos.")
                        .contact(new Contact()
                                .name("Soporte SCampus")
                                .email("soporte@scampus.com")));
    }
}
