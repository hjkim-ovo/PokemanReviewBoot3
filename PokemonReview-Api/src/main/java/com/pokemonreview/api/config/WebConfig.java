package com.pokemonreview.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				//.allowedOrigins("*")
				.allowedOriginPatterns("*")
				.allowedHeaders("*")
				.allowedMethods("*")
				.allowCredentials(true)
				.exposedHeaders("*")
				.maxAge(3600);
	}

}