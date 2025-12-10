package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // Appliquer CORS à toutes les routes sous /api/
                        .allowedOrigins(
                            "http://localhost:3000", // React
                            "http://localhost:4200", // Angular
                            "http://localhost:5173", // Vite
                            "http://127.0.0.1:5500"  // Live Server (VS Code)
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                                                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With")
                        .allowCredentials(true);
            }
        };
    }
}

