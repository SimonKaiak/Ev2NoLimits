package com.example.NoLimits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "API NoLimits",
        version = "1.0",
        description = "Documentación de la API del proyecto NoLimits"
    ),
    servers = {
        @Server(url = "http://localhost:8080"),  // Local
        @Server(url = "https://6a02-2803-c600-d208-8bd4-6cda-c6e8-d23c-951a.ngrok-free.app")  // Ngrok
    }
)
public class NoLimitsApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoLimitsApplication.class, args);
	}

}