package no.nav.bidrag.commons.web.config

import java.time.Duration
import java.time.temporal.ChronoUnit
import no.nav.bidrag.commons.web.interceptor.ConsumerIdClientInterceptor
import no.nav.bidrag.commons.web.interceptor.MdcValuesPropagatingClientInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.metrics.web.client.ObservationRestTemplateCustomizer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Scope

@Suppress("SpringFacetCodeInspection")
@Configuration
@Import(
    ConsumerIdClientInterceptor::class,
    MdcValuesPropagatingClientInterceptor::class,
    NaisProxyCustomizer::class,
    ObservationRestTemplateCustomizer::class,
)
class RestTemplateBuilderBean {
    @Bean
    @Scope("prototype")
    @ConditionalOnProperty("no.nav.security.jwt.issuer.aad.proxy_url")
    fun restTemplateBuilder(
        iNaisProxyCustomizer: INaisProxyCustomizer,
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
        observationRestTemplateCustomizer: ObservationRestTemplateCustomizer,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
        @Value("\${bidrag.rest.read.timeout.seconds:15}") readTimeoutSeconds: Long,
        @Value("\${bidrag.rest.connect.timeout.seconds:15}") connectTimeoutSeconds: Long,
    ) = RestTemplateBuilder()
        .additionalInterceptors(consumerIdClientInterceptor, mdcValuesPropagatingClientInterceptor)
        .additionalCustomizers(observationRestTemplateCustomizer)
        .additionalCustomizers(iNaisProxyCustomizer)
        .connectTimeout(Duration.of(connectTimeoutSeconds, ChronoUnit.SECONDS))
        .readTimeout(Duration.of(readTimeoutSeconds, ChronoUnit.SECONDS))

    /**
     * Denne bønnnen initialiseres hvis proxy-url ikke finnes. Hvis proxy-url finnnes vil bønnen over
     * initialiseres og denne initialiseres ikke med mindre proxyen har verdien "umulig verdi",
     * som den aldri skal ha.
     */
    @Bean
    @Scope("prototype")
    @ConditionalOnProperty(
        "no.nav.security.jwt.issuer.aad.proxy_url",
        matchIfMissing = true,
        havingValue = "Umulig verdi",
    )
    fun restTemplateBuilderNoProxy(
        consumerIdClientInterceptor: ConsumerIdClientInterceptor,
        observationRestTemplateCustomizer: ObservationRestTemplateCustomizer,
        mdcValuesPropagatingClientInterceptor: MdcValuesPropagatingClientInterceptor,
        @Value("\${bidrag.rest.read.timeout.seconds:15}") readTimeoutSeconds: Long,
        @Value("\${bidrag.rest.connect.timeout.seconds:15}") connectTimeoutSeconds: Long,
    ): RestTemplateBuilder =
        RestTemplateBuilder()
            .additionalInterceptors(consumerIdClientInterceptor, mdcValuesPropagatingClientInterceptor)
            .additionalCustomizers(observationRestTemplateCustomizer)
            .connectTimeout(Duration.of(connectTimeoutSeconds, ChronoUnit.SECONDS))
            .readTimeout(Duration.of(readTimeoutSeconds, ChronoUnit.SECONDS))
}
