package com.perucontrols.techdoc.config;

import com.perucontrols.techdoc.model.error.ErrorResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Documentación Técnica")
                        .description("API REST para gestionar documentación técnica de sistemas industriales")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Peru Controls")
                                .url("https://www.perucontrols.com")
                                .email("soporte@perucontrols.com"))
                        .license(new License().name("Licencia privada").url("https://www.perucontrols.com/licencia")))
                .components(new Components()
                        .addResponses("NotFound", createApiResponse("Recurso no encontrado", "El recurso solicitado no fue encontrado"))
                        .addResponses("BadRequest", createApiResponse("Petición inválida", "Los datos enviados no son válidos"))
                        .addResponses("Conflict", createApiResponse("Conflicto de datos", "Los datos proporcionados entran en conflicto con los existentes"))
                        .addResponses("Forbidden", createApiResponse("Operación no permitida", "No tiene permisos para realizar esta operación"))
                        .addResponses("InternalServerError", createApiResponse("Error interno del servidor", "Se ha producido un error al procesar su solicitud"))
                        .addSchemas("ErrorResponse", createErrorResponseSchema()));
    }

    private ApiResponse createApiResponse(String description, String message) {
        return new ApiResponse()
                .description(description)
                .content(new Content()
                        .addMediaType("application/json", new MediaType()
                                .schema(new Schema<ErrorResponse>().$ref("#/components/schemas/ErrorResponse"))));
    }

    private Schema createErrorResponseSchema() {
        Schema<ErrorResponse> schema = new Schema<>();
        schema.type("object");
        schema.addProperty("timestamp", new Schema<>().type("string").format("date-time"));
        schema.addProperty("status", new Schema<>().type("integer").format("int32"));
        schema.addProperty("error", new Schema<>().type("string"));
        schema.addProperty("message", new Schema<>().type("string"));
        schema.addProperty("path", new Schema<>().type("string"));
        return schema;
    }
}