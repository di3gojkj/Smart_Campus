package com.diego.MS_Gestion_Usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsGestionUsuarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsGestionUsuarioApplication.class, args);
	}

}
