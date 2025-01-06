package org.example.findmateapi.Security.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Wszystkie endpointy
                .allowedOrigins("*") // Pozwala na dostęp z dowolnego origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Dopuszczalne metody HTTP
                .allowedHeaders("*") // Dopuszczalne nagłówki
                .allowCredentials(false);
    }
}
