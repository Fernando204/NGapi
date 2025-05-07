package com.nginvest.neogenesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.nginvest.neogenesis")
public class NeogenesisApplication {

	public  static void main(String[] args) {
		SpringApplication.run(NeogenesisApplication.class, args);
		System.out.println("Servidor Iniciado");
	}

}
