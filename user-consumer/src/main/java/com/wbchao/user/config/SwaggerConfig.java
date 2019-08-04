package com.wbchao.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(new ApiInfoBuilder().title("User-Service")
                                                                                   .description("Proved the CRUD API for User")
                                                                                   .contact(new Contact("Chao Wenbo", "", "sandy320@sohu.com"))
                                                                                   .version("1.0")
                                                                                   .build())
                                                      .select()
                                                      .apis(RequestHandlerSelectors.basePackage("com.wbchao.user.controller"))
                                                      .paths(PathSelectors.any())
                                                      .build();
    }

}
