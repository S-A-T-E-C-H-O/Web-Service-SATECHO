package com.satecho.agrosafe;

import com.satecho.agrosafe.platform.Application;
import org.springframework.boot.SpringApplication;

public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
