package com.zylos.backend.core.config;

import java.util.List;

import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zylos.backend.core.exception.ErrorResponse;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;

@Configuration 
public class OpenApiConfig {
    
    @Bean 
    public GlobalOpenApiCustomizer globalErrorResponseCustomizer(){
        return openApi -> {
            if (openApi.getComponents() == null){
                openApi.setComponents(new Components());
            }

            openApi.getComponents().addSchemas("ErrorResponse",
                new Schema<>()
                    .type("object")
                    .addProperty("status", new IntegerSchema().example(400))
                    .addProperty("message", new StringSchema().example("An Error occured"))
                    .addProperty("timestamp", new StringSchema().format("date-time").example("2026-09-30T21:49:00Z"))
                    .addProperty("errors", new MapSchema().additionalProperties(new StringSchema()))
            );

            Schema<?> errorSchemaRef = new Schema<>()
                    .$ref("#/components/schemas/ErrorResponse");

            List<String> errorCodes = List.of("400", "401", "403", "404", "500", "default");

            openApi.getPaths().values().forEach(pathItem -> {
                pathItem.readOperations().forEach(operation -> {
                    for (String statusCode : errorCodes) {
                        ApiResponse errorResponse = new ApiResponse()
                            .description("Error Response (" + statusCode + ")")
                            .content(new Content().addMediaType("application/json", 
                                new MediaType().schema(errorSchemaRef)));

                        operation.getResponses().addApiResponse(statusCode, errorResponse);
                    }
                });
            });
        };
    }
}
