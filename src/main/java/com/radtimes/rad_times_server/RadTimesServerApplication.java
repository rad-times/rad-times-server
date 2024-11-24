package com.radtimes.rad_times_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class})
public class RadTimesServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RadTimesServerApplication.class, args);
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
