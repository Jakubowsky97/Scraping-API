package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Zezwól na żądania dla ścieżek zaczynających się od /api/
                        .allowedOrigins("http://localhost:3000", "https://scraping-web-jade.vercel.app") // Domena, z której zezwalamy na żądania
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Dozwolone metody
                        .allowedHeaders("*") // Dozwolone nagłówki
                        .allowCredentials(true); // Zezwalaj na ciasteczka/autoryzację
            }
        };
    }
}