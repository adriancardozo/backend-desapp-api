package ar.edu.unq.desapp.grupoL.backenddesappapi.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfiguration {
    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(getApiInfo())
            .select()
            .paths(PathSelectors.any())
            .apis(RequestHandlerSelectors.basePackage("ar.edu.unq.desapp.grupoL.backenddesappapi.webservices"))
            .build()
    }

    private fun getApiInfo(): ApiInfo {
        val contact = Contact("Adrián Cardozo", "www.adriancardozo.com", "adriancardozo@email.com")
        return ApiInfoBuilder()
            .title("Crypto p2p API")
            .description("An API for Crypto P2P operations")
            .version("1.0.0")
            .contact(contact)
            .build()
    }
}