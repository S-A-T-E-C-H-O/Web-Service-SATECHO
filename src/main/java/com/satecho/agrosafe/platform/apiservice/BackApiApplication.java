package com.satecho.agrosafe.platform.apiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
public class BackApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackApiApplication.class, args);
    }

}
