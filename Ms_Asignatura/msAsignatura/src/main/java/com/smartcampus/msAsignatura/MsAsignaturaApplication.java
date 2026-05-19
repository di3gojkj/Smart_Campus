package com.smartcampus.msAsignatura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsAsignaturaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAsignaturaApplication.class, args);
	}

}
