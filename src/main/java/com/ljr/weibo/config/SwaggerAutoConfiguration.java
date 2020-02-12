package com.ljr.weibo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * swagger的相关配置
 */
@Configuration
@EnableSwagger2 //启用swagger
@ConditionalOnClass(value = SwaggerProperties.class)
@EnableConfigurationProperties(value = {SwaggerProperties.class})
public class SwaggerAutoConfiguration {

    @Autowired
    private SwaggerProperties properties;

    @Bean
    public Docket swaggerSpringMvcPlugin(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build();
    }



    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().description(properties.getDescription())
                .contact(new Contact(properties.getUsername(),properties.getPhone(),properties.getEmail()))
                .version(properties.getVersion())
                .license(properties.getLicense())
                .build();
    }
}
