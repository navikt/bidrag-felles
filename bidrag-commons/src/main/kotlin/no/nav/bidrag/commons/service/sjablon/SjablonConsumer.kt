package no.nav.bidrag.commons.service.sjablon

import io.github.oshai.kotlinlogging.KotlinLogging
import no.nav.bidrag.commons.CorrelationId
import no.nav.bidrag.commons.service.AppContext
import no.nav.bidrag.commons.service.retryTemplate
import no.nav.bidrag.commons.service.sjablon.SjablonProvider.Companion.SJABLONTALL_CACHE
import no.nav.bidrag.commons.util.secureLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cache.annotation.Cacheable
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate

private val logger = KotlinLogging.logger {}

/**
 * SjablonConsumer brukes for å hente sjablontall.
 *
 * For å ta i bruker provideren må følgende annotering legges til i konfigurasjon
 *
 * ```kotlin
 * @EnableSjablonProvider
 * ```
 *
 * Hvor følgende miljøvariabler er definert i nais konfigurasjonen og outbound traffik er tillatt for BIDRAG_SJABLON_URL
 * ```yaml
 *   BIDRAG_SJABLON_URL: https://bidrag-sjablon.<cluster>-pub.nais.io/bidrag-sjablon
 * ```
 */
@Component("CommonsSjablonConsumer")
internal class SjablonConsumer(
    @Value("\${BIDRAG_SJABLON_URL}") url: String,
) {
    private val sjablontallUrl = "/sjablontall/all"
    val restTemplate: RestTemplate =
        RestTemplateBuilder()
            .rootUri(url)
            .defaultHeader("Nav-Call-Id", CorrelationId.fetchCorrelationIdForThread())
            .defaultHeader("Nav-Consumer-Id", System.getenv("NAIS_APP_NAME") ?: "bidrag-commons")
            .build()

    @Cacheable(SJABLONTALL_CACHE)
    fun hentSjablontall(): ResponseEntity<List<Sjablontall>> {
        try {
            val sjablonResponse =
                restTemplate.exchange(
                    sjablontallUrl,
                    HttpMethod.GET,
                    null,
                    object : ParameterizedTypeReference<List<Sjablontall>>() {},
                )
            logger.info { "Hentet sjablontall fra bidrag-sjablon" }
            return sjablonResponse
        } catch (exception: RestClientResponseException) {
            logger.error(exception) {
                "Det skjedde en feil ved henting av sjablontall med feil ${exception.statusText} og feilmelding " +
                    "${exception.message}"
            }
            throw exception
        }
    }
}

class SjablonProvider {
    companion object {
        const val SJABLONTALL_CACHE = "SJABLONTALL_CACHE"

        /**
         * Hent liste over alle sjablonverdier
         */
        fun hentSjablontall(): List<Sjablontall> {
            return try {
                retryTemplate(
                    "SjablonProvider.hentSjablontall",
                ).execute<List<Sjablontall>, HttpClientErrorException> {
                    AppContext.getBean("CommonsSjablonConsumer", SjablonConsumer::class.java).hentSjablontall().body
                }
            } catch (e: Exception) {
                secureLogger.error(e) { "Det skjedde en feil ved henting av sjablontall" }
                emptyList()
            }
        }
    }
}
