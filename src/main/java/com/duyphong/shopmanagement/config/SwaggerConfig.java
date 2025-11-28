package com.duyphong.shopmanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Value("${server.port:8080}")
    private String serverPort;

    private static final String SECURITY_SCHEME_NAME = "Bearer Authentication";

    @Bean
    public OpenAPI customOpenAPI() {
        Server localServer = new Server()
                .url("http://localhost:" + serverPort)
                .description("Local Development Server");

        Contact contact = new Contact()
                .name("Duy Phong")
                .email("duyphong@example.com")
                .url("https://github.com/duyphong");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("Employee Management API")
                .description("RESTful API for managing employee information using Spring Boot, JPA, and Hibernate")
                .version("1.0.0")
                .contact(contact)
                .license(license);
        SecurityScheme securityScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP) // Loại bảo mật là HTTP
                .scheme("bearer")               // Scheme là bearer
                .bearerFormat("JWT")            // Định dạng token là JWT
                .description("Enter JWT Bearer token **_without_** the prefix 'Bearer '.");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(SECURITY_SCHEME_NAME);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer))
                // Thêm Components định nghĩa Security Scheme
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme))
                // Áp dụng Security Requirement
                .addSecurityItem(securityRequirement);
    }
}
