package com.diego.Ms_Gestion_Lista;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsGestionListaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsGestionListaApplication.class, args);
	}

}
