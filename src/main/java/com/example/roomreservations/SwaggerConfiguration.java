package com.example.roomreservations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.not;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.builders.RequestHandlerSelectors.any;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@EnableSwagger2
@Configuration
class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(any())
                .paths(not(regex("/error.*")))
                .build();
    }
}
