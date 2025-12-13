package org.example.testifyproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TestifyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestifyServiceApplication.class, args);
	}

}
