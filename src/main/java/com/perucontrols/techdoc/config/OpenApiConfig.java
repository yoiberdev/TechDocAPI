package com.perucontrols.techdoc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server prodServer = new Server();
        prodServer.setUrl("https://techdoc.yoiber.com/api");
        prodServer.setDescription("Servidor de producción");

        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080/api");
        devServer.setDescription("Servidor local de desarrollo");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("API de Documentación Técnica de Embarcaciones")
                .version("1.0")
                .description("API REST para gestionar documentación técnica de embarcaciones.")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(prodServer, devServer)); // prod primero = default
    }
}
