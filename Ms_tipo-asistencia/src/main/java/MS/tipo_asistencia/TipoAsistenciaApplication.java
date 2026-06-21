package MS.tipo_asistencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TipoAsistenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TipoAsistenciaApplication.class, args);
	}

}
