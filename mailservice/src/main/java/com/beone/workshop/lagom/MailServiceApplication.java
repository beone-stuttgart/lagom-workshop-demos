package com.beone.workshop.lagom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class MailServiceApplication {

	public static void main(final String[] args) {
		SpringApplication.run(MailServiceApplication.class, args);
	}
}
