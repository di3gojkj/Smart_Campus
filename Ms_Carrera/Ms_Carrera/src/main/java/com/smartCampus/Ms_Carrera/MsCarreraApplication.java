package com.smartCampus.Ms_Carrera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsCarreraApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCarreraApplication.class, args);
	}

}
