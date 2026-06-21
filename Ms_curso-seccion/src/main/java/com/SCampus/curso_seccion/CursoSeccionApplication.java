package com.SCampus.curso_seccion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CursoSeccionApplication {

	public static void main(String[] args) {
		SpringApplication.run(CursoSeccionApplication.class, args);
	}
}