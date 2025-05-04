package com.perucontrols.techdoc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
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
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080/api");
        devServer.setDescription("URL del servidor de desarrollo");

        Server prodServer = new Server();
        prodServer.setUrl("https://api.perucontrols.com");
        prodServer.setDescription("URL del servidor de producción");

        License mitLicense = new License()
                .name("Licencia MIT")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("API de Documentación Técnica de Embarcaciones")
                .version("1.0")
                .description("Esta API proporciona endpoints para gestionar la documentación técnica de sistemas instalados en embarcaciones marítimas.")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
}