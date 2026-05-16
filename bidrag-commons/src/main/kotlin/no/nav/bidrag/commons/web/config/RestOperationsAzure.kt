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
import org.springframework.http.converter.AbstractGenericHttpMessageConverter
import org.springframework.http.converter.ByteArrayHttpMessageConverter
import org.springframework.http.converter.FormHttpMessageConverter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.lang.reflect.Type

@Suppress("SpringFacetCodeInspection")
@Configuration
@Import(RestTemplateBuilderBean::class, BearerTokenClientInterceptor::class, ServiceUserAuthTokenInterceptor::class)
class RestOperationsAzure {
    @Bean("azure")
    @Scope("prototype")
    fun restOperationsJwtBearer(
        restTemplateBuilder: RestTemplateBuilder,
        bearerTokenClientInterceptor: BearerTokenClientInterceptor,
    ): RestTemplate =
        restTemplateBuilder
            .additionalInterceptors(bearerTokenClientInterceptor)
            .messageConverters(createMessageConverters())
            .build()

    @Bean("azureService")
    @Scope("prototype")
    fun restOperationsServiceJwtBearer(
        restTemplateBuilder: RestTemplateBuilder,
        bearerTokenClientInterceptor: ServiceUserAuthTokenInterceptor,
    ): RestTemplate =
        restTemplateBuilder
            .additionalInterceptors(bearerTokenClientInterceptor)
            .messageConverters(createMessageConverters())
            .build()

    /**
     * Creates a list of message converters with the common ObjectMapper.
     * This ensures all REST calls use consistent Jackson configuration.
     */
    private fun createMessageConverters(): List<HttpMessageConverter<*>> =
        listOf(
            ByteArrayHttpMessageConverter(),
            CustomJacksonHttpMessageConverter(commonObjectmapper),
            StringHttpMessageConverter(),
            Jaxb2RootElementHttpMessageConverter(),
            MarshallingHttpMessageConverter(),
            FormHttpMessageConverter(),
        )

    /**
     * Custom JSON message converter that uses the shared ObjectMapper configuration.
     * This avoids ClassLoader/version conflicts by ensuring all deserialization
     * uses the same ObjectMapper instance.
     */
    private class CustomJacksonHttpMessageConverter(
        private val objectMapper: ObjectMapper,
    ) : AbstractGenericHttpMessageConverter<Any>(
            MediaType.APPLICATION_JSON,
            MediaType("application", "*+json"),
        ) {
        override fun supports(clazz: Class<*>): Boolean {
            // Don't handle primitive types, strings, or byte arrays
            return !clazz.isPrimitive &&
                clazz != String::class.java &&
                clazz != ByteArray::class.java
        }

        override fun read(
            type: Type,
            contextClass: Class<*>?,
            inputMessage: HttpInputMessage,
        ): Any = objectMapper.readValue(inputMessage.body, objectMapper.constructType(type))

        override fun readInternal(
            clazz: Class<out Any>,
            inputMessage: HttpInputMessage,
        ): Any = objectMapper.readValue(inputMessage.body, clazz)

        override fun writeInternal(
            obj: Any,
            type: Type?,
            outputMessage: HttpOutputMessage,
        ) {
            outputMessage.body.use { os ->
                if (type == null) {
                    objectMapper.writeValue(os, obj)
                } else {
                    objectMapper.writerFor(objectMapper.constructType(type)).writeValue(os, obj)
                }
            }
        }
    }
}
