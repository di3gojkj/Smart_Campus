package com.smartCampus.Ms_Evaluacion;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;IGNORECASE=TRUE",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.cloud.openfeign.enabled=false",
    "eureka.client.enabled=false",
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false"
})
class MsEvaluacionApplicationTests {

	@Test
	void contextLoads() {
	}

}
