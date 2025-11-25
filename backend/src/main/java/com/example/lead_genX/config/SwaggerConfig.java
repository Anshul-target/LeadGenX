package com.example.lead_genX.config;




import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "LeadGenx Backend",
                version = "1.0",
                description = "This is the API documentation for leadGenx backend."
        ),
        servers = {
                @Server(url = "http://localhost:8081", description = "Local Server")
//                @Server(url = "https://edtech-platform-production.up.railway.app/", description = "Local Server")
        }
)

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}

