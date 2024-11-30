package no.nav.bidrag.commons.web.config

import no.nav.bidrag.commons.web.interceptor.BearerTokenClientInterceptor
import no.nav.bidrag.commons.web.interceptor.ServiceUserAuthTokenInterceptor
import no.nav.bidrag.transport.felles.commonObjectmapper
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Scope
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Suppress("SpringFacetCodeInspection")
@Configuration
@Import(RestTemplateBuilderBean::class, BearerTokenClientInterceptor::class, ServiceUserAuthTokenInterceptor::class)
class RestOperationsAzure {
    @Bean("azure")
    @Scope("prototype")
    fun restOperationsJwtBearer(
        restTemplateBuilder: RestTemplateBuilder,
        bearerTokenClientInterceptor: BearerTokenClientInterceptor,
    ): RestTemplate {
        val restTemplate =
            restTemplateBuilder
                .additionalInterceptors(bearerTokenClientInterceptor)
                .build()
        configureJackson(restTemplate)
        return restTemplate
    }

    @Bean("azureService")
    @Scope("prototype")
    fun restOperationsServiceJwtBearer(
        restTemplateBuilder: RestTemplateBuilder,
        bearerTokenClientInterceptor: ServiceUserAuthTokenInterceptor,
    ): RestTemplate {
        val restTemplate =
            restTemplateBuilder
                .additionalInterceptors(bearerTokenClientInterceptor)
                .build()
        configureJackson(restTemplate)
        return restTemplate
    }

    private fun configureJackson(restTemplate: RestTemplate) {
        restTemplate.messageConverters
            .stream()
            .filter { obj -> MappingJackson2HttpMessageConverter::class.java.isInstance(obj) }
            .map { obj -> MappingJackson2HttpMessageConverter::class.java.cast(obj) }
            .findFirst()
            .ifPresent { converter: MappingJackson2HttpMessageConverter ->
                converter.objectMapper = commonObjectmapper
            }
    }
}
