package me.project3.demo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors.any
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2) // open api spec 3.0
            .select()
            .apis(any())
            .paths(PathSelectors.ant("/api/**"))
            .build()
            .apiInfo(metaData())
            .securityContexts(listOf(securityContext()))
            .securitySchemes(listOf(apiKey()))
    }

    private fun metaData(): ApiInfo? {
        return ApiInfoBuilder()
            .title("demo api")
            .description("")
            .version("0.0.1")
            .termsOfServiceUrl("Terms of service")
            .contact(null)
            .license("Apache License Version 2.0")
            .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
            .build()
    }
    private fun apiKey(): ApiKey? {
        return ApiKey("JWT", "Authorization", "header")
    }

    private fun securityContext(): SecurityContext? {
        return SecurityContext
            .builder()
            .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build()
    }

    fun defaultAuth(): List<SecurityReference?>? {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes: Array<AuthorizationScope?> = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return listOf(SecurityReference("JWT", authorizationScopes))
    }
}
