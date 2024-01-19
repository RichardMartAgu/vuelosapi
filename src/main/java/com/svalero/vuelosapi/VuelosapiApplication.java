package com.svalero.vuelosapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.svalero.vuelosapi")
public class VuelosapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VuelosapiApplication.class, args);
	}

}
