package com.example.NoLimits.Multimedia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Indica que en esta clase se usarán Beans.
@Configuration
// Permitie que otros dominios (por ejemplo, una app en React o un frontend distinto al backend) 
// puedan hacer peticiones a tu API sin que el navegador las bloquee por temas de seguridad.
public class WebConfig {

    // Para crear y configurar objetos a mano que normalmente Spring no crea automáticamente.
    // Es útil cuando querís controlar tú mismo cómo se construye un componente, como el WebMvcConfigurer.
    @Bean
    // Es una interfaz de Spring que te permite personalizar el comportamiento del módulo Web (Spring MVC).
    public WebMvcConfigurer corsConfigurer() {
        // Crea un objeto anónimo que implementa WebMvcConfigurer.
        return new WebMvcConfigurer() {
            // reescribe una función ya definida para cambiar o personalizar su comportamiento.
            @Override
            // Este método forma parte de la interfaz WebMvcConfigurer, y se usa para configurar reglas CORS en Spring Boot.
            public void addCorsMappings(CorsRegistry registry) {
                // Aplica las reglas CORS a todas las URL del backend, sin importar qué ruta sea.
                registry.addMapping("/**")
                        // Permite peticiones CORS desde cualquier dominio.
                        .allowedOriginPatterns("*")  // "*": Todos los dominios pueden acceder.
                        // Indica a Spring qué métodos HTTP están permitidos cuando una aplicación externa hace peticiones al backend. (Get. Post, Put, Patch, Delete)
                        .allowedMethods("*")
                        // Soluciona error con conexión HTTP y HTTPS para Ngrok.
                        .allowedHeaders("*");
            }
        };
    }
}
