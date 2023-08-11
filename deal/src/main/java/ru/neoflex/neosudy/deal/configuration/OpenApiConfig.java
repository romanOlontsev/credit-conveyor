package ru.neoflex.neosudy.deal.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                              .title("Deal API")
                              .version("1.0.0")
                              .contact(new Contact()
                                               .name("Roman Olontsev")
                                               .email("rs.olontsev@gmail.com")
                                               .url("https://t.me/r_olontsev")));
    }
}