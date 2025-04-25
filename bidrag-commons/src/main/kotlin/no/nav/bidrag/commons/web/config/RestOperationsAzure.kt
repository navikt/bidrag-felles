package no.nav.bidrag.commons.web.config

import no.nav.bidrag.commons.web.interceptor.BearerTokenClientInterceptor
import no.nav.bidrag.commons.web.interceptor.CustomOAuth2CacheFactory
import no.nav.bidrag.commons.web.interceptor.ServiceUserAuthTokenInterceptor
import no.nav.bidrag.transport.felles.commonObjectmapper
import no.nav.security.token.support.client.core.context.JwtBearerTokenResolver
import no.nav.security.token.support.client.core.http.OAuth2HttpClient
import no.nav.security.token.support.client.core.oauth2.ClientCredentialsTokenClient
import no.nav.security.token.support.client.core.oauth2.OAuth2AccessTokenService
import no.nav.security.token.support.client.core.oauth2.OnBehalfOfTokenClient
import no.nav.security.token.support.client.core.oauth2.TokenExchangeClient
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Scope
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter
import org.springframework.web.client.RestTemplate

@Suppress("SpringFacetCodeInspection")
@Configuration
@Import(RestTemplateBuilderBean::class, BearerTokenClientInterceptor::class, ServiceUserAuthTokenInterceptor::class)
class RestOperationsAzure {
    @Bean
    @Primary
    fun oAuth2AccessTokenService(
        bearerTokenResolver: JwtBearerTokenResolver,
        client: OAuth2HttpClient,
    ): OAuth2AccessTokenService {
        val maxx = 1000L
        val skew = 10L
        return OAuth2AccessTokenService(
            bearerTokenResolver,
            OnBehalfOfTokenClient(client),
            ClientCredentialsTokenClient(client),
            TokenExchangeClient(client),
            CustomOAuth2CacheFactory.accessTokenResponseCache(maxx, skew),
            CustomOAuth2CacheFactory.accessTokenResponseCache(maxx, skew),
            CustomOAuth2CacheFactory.accessTokenResponseCache(maxx, skew),
        )
    }

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

        restTemplate.messageConverters =
            restTemplate.messageConverters
                .filter { obj -> !MappingJackson2XmlHttpMessageConverter::class.java.isInstance(obj) }
                .toMutableList()
    }
}
