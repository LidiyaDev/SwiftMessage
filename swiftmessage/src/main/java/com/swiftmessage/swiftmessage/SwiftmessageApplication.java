package com.swiftmessage.swiftmessage;

import java.util.Arrays;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;

import org.springframework.context.annotation.Bean;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import lombok.RequiredArgsConstructor;

@SpringBootApplication 
@RequiredArgsConstructor
public class SwiftmessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SwiftmessageApplication.class, args);
	}
 
	/* 
	 * @Bean
	 * ServletRegistrationBean jsfServletRegistration (ServletContext
	 * servletContext) {
	 * //spring boot only works if this is set
	 * servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration",
	 * Boolean.TRUE.toString());
	 * 
	 * //registration
	 * ServletRegistrationBean srb = new ServletRegistrationBean();
	 * srb.setServlet(new FacesServlet());
	 * srb.setUrlMappings(Arrays.asList("*.xhtml"));
	 * srb.setLoadOnStartup(1);
	 * return srb;
	 * }
	 */

	@Bean
	ServletRegistrationBean<FacesServlet> jsfServletRegistration(ServletContext servletContext) {
		// spring boot only works if this is set
		servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());

		// registration
		ServletRegistrationBean<FacesServlet> srb = new ServletRegistrationBean<>();
		srb.setServlet(new FacesServlet());
		srb.setUrlMappings(Arrays.asList("*.xhtml"));

		srb.setLoadOnStartup(1);
		return srb;
	}

	@Bean
	public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
		return new KeycloakSpringBootConfigResolver();
	}

}
