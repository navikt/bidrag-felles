package no.nav.bidrag.commons.web.config

import no.nav.bidrag.commons.web.interceptor.BearerTokenClientInterceptor
import no.nav.bidrag.commons.web.interceptor.ServiceUserAuthTokenInterceptor
import no.nav.bidrag.transport.felles.commonObjectmapper
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Scope
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@Suppress("SpringFacetCodeInspection")
@Configuration
@Import(RestTemplateBuilderBean::class, BearerTokenClientInterceptor::class, ServiceUserAuthTokenInterceptor::class)
class RestOperationsAzure {
    @Bean("azure")
    @Scope("prototype")
    fun restOperationsJwtBearer(
        restTemplateBuilder: RestTemplateBuilder,
        bearerTokenClientInterceptor: BearerTokenClientInterceptor,
    ) = restTemplateBuilder.additionalInterceptors(bearerTokenClientInterceptor)
        .additionalMessageConverters(MappingJackson2HttpMessageConverter(commonObjectmapper))
        .additionalMessageConverters(ByteArrayHttpMessageConverter())
        .build()

    @Bean("azureService")
    @Scope("prototype")
    fun restOperationsServiceJwtBearer(
        restTemplateBuilder: RestTemplateBuilder,
        bearerTokenClientInterceptor: ServiceUserAuthTokenInterceptor,
    ) = restTemplateBuilder.additionalInterceptors(bearerTokenClientInterceptor)
        .additionalMessageConverters(MappingJackson2HttpMessageConverter(commonObjectmapper))
        .additionalMessageConverters(ByteArrayHttpMessageConverter())
        .build()
}
