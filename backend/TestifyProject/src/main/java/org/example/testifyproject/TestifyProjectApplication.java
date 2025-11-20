package org.example.testifyproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TestifyProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestifyProjectApplication.class, args);
	}

}
