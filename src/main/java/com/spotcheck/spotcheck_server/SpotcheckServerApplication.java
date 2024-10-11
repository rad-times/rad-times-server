package com.spotcheck.spotcheck_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@SpringBootApplication
public class SpotcheckServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotcheckServerApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");

			}
		};
	}

	@Configuration
	public class WebConfig implements WebMvcConfigurer {

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/static/**")
					.addResourceLocations("classpath:/static/")
					.setCachePeriod(3900)
					.resourceChain(true)
					.addResolver(new PathResourceResolver());
		}
	}
}
