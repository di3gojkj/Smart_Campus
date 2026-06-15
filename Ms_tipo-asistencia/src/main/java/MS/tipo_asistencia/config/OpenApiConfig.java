package MS.tipo_asistencia.config;

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
                        .title("API de Control de Asistencias (MS)")
                        .version("1.0.0")
                        .description("Microservicio encargado del registro diario de asistencias, inasistencias y parametrización de catálogos de estados de asistencia (Presente, Ausente, Justificado).")
                        .contact(new Contact()
                                .name("Control Académico")
                                .email("asistencias@scampus.com")));
    }
}
