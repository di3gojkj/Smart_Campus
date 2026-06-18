package com.cur_eva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CurEvaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CurEvaApplication.class, args);
	}

}
