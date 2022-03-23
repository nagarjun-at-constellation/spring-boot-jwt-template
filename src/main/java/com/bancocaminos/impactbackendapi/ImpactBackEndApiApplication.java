package com.bancocaminos.impactbackendapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableAutoConfiguration
@PropertySource("classpath:messages.properties")
public class ImpactBackEndApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImpactBackEndApiApplication.class, args);
	}

}
