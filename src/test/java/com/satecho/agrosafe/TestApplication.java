package com.satecho.agrosafe;

import com.satecho.agrosafe.platform.apiservice.BackApiApplication;
import org.springframework.boot.SpringApplication;

public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.from(BackApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
