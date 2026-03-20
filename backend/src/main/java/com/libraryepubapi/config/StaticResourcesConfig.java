package com.libraryepubapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourcesConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Sirve las portadas generadas como ficheros estáticos bajo /covers/**
        // Ajusta la ruta al directorio montado en el servidor/contenedor.
        registry.addResourceHandler("/covers/**")
                .addResourceLocations("file:/srv/library/covers/")
                .setCachePeriod(604800); // 7 días en segundos
    }
}

