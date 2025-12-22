package org.family.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI familyOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("API 文档")
                .description("""
                    """)
                .version("v1.0.0")
                .contact(new Contact()
                    .name("技术支持")
                    .email("support@family.com")
                    .url("https://github.com/15565772892/family"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0"))
                .termsOfService("https://family.example.com/terms"))
            .externalDocs(new io.swagger.v3.oas.models.ExternalDocumentation()
                .description("Wiki 文档")
                .url("https://github.com/15565772892/family/wiki"));
    }
}