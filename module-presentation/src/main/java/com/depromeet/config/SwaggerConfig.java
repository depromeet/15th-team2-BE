package com.depromeet.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    private static final String API_TITLE = "Depromeet Team2 Backend API Docs";
    private static final String API_DESCRIPTION = "Depromeet Team2 Backend API 문서입니다.";

    private final SwaggerProperties swaggerProperties;

    @Bean
    public OpenAPI openAPI() {
        Info info =
                new Info()
                        .version(swaggerProperties.version())
                        .title(API_TITLE)
                        .description(API_DESCRIPTION);

        SecurityScheme apiKey =
                new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("Bearer Token");

        Server productionServer = new Server();
        productionServer.setDescription("Production Server");
        productionServer.setUrl("https://api.swimie.life");

        Server localServer = new Server();
        localServer.setDescription("Local Server");
        localServer.setUrl("http://localhost:8080");

        return new OpenAPI()
                .info(info)
                .addSecurityItem(getSecurityRequirement())
                .components(getAuthComponent())
                .servers(List.of(productionServer, localServer))
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(securityRequirement);
    }

    private SecurityRequirement getSecurityRequirement() {
        String jwt = "JWT";
        return new SecurityRequirement().addList(jwt);
    }

    private Components getAuthComponent() {
        return new Components()
                .addSecuritySchemes(
                        "JWT",
                        new SecurityScheme()
                                .name("JWT")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization"));
    }
}
