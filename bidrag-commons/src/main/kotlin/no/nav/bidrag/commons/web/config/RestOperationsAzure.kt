package no.nav.bidrag.commons.web.config

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.bidrag.commons.web.interceptor.BearerTokenClientInterceptor
import no.nav.bidrag.commons.web.interceptor.ServiceUserAuthTokenInterceptor
import no.nav.bidrag.transport.felles.commonObjectmapper
import org.springframework.boot.restclient.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Scope
import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.AbstractHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
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
        return restTemplateBuilder
            .additionalInterceptors(bearerTokenClientInterceptor)
            .messageConverters(createMessageConverters())
            .build()
    }

    @Bean("azureService")
    @Scope("prototype")
    fun restOperationsServiceJwtBearer(
        restTemplateBuilder: RestTemplateBuilder,
        bearerTokenClientInterceptor: ServiceUserAuthTokenInterceptor,
    ): RestTemplate {
        return restTemplateBuilder
            .additionalInterceptors(bearerTokenClientInterceptor)
            .messageConverters(createMessageConverters())
            .build()
    }

    /**
     * Creates a list of message converters with the common ObjectMapper.
     * This ensures all REST calls use consistent Jackson configuration.
     */
    private fun createMessageConverters(): List<HttpMessageConverter<*>> {
        return listOf(
            CustomJacksonHttpMessageConverter(commonObjectmapper),
        )
    }

    /**
     * Custom JSON message converter that uses the shared ObjectMapper configuration.
     * This avoids ClassLoader/version conflicts by ensuring all deserialization
     * uses the same ObjectMapper instance.
     */
    private class CustomJacksonHttpMessageConverter(private val objectMapper: ObjectMapper) :
        AbstractHttpMessageConverter<Any>(MediaType.APPLICATION_JSON) {

        init {
            setSupportedMediaTypes(listOf(
                MediaType.APPLICATION_JSON,
                MediaType("application", "*+json"),
            ))
        }

        override fun supports(clazz: Class<*>): Boolean = true

        override fun readInternal(clazz: Class<out Any>, inputMessage: HttpInputMessage): Any {
            return objectMapper.readValue(inputMessage.body, clazz)
        }

        override fun writeInternal(obj: Any, outputMessage: HttpOutputMessage) {
            outputMessage.body.use { os ->
                objectMapper.writeValue(os, obj)
            }
        }
    }
}
