package com.example.NoLimits.Multimedia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

// Definimos la clase como clase de Configuration.
// Esta clase va a tener métodos o configuraciones que quiero que Spring cargue al iniciar el proyecto.
@Configuration
public class SwaggerConfig {

    // Método que se usa en una clase Configuration para que el objeto pueda ser instanciado desde cualquier parte con @Autowired.
    @Bean
    // Objeto que usa Springdoc para generar el JSON que alimenta el Swagger UI.
    public OpenAPI customOpenAPI(){
        // Crea una instancia y configura sus métodos con info.
        return new OpenAPI().info(new Info().title("Controllers 'NoLimits' ")
                                            .version("Versión: ")
                                            .description("Controllers de 'NoLimits' y Swagger."));
    }

}
