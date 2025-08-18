package no.nav.bidrag.commons.tilgang

import no.nav.bidrag.commons.web.client.AbstractRestClient
import no.nav.bidrag.commons.web.config.RestOperationsAzure
import no.nav.bidrag.transport.tilgang.Sporingsdata
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
@Import(RestOperationsAzure::class)
class TilgangClient(
    @Value("\${BIDRAG_TILGANG_URL}") private val tilgangURI: URI,
    @Qualifier("azure") private val restTemplate: RestTemplate,
) : AbstractRestClient(restTemplate, "tilgang") {
    private val logger = LoggerFactory.getLogger(this::class.java)

    val sakUri =
        UriComponentsBuilder
            .fromUri(tilgangURI)
            .pathSegment("v2", "api", "tilgang", "sak")
            .build()
            .toUri()
    val personUri =
        UriComponentsBuilder
            .fromUri(tilgangURI)
            .pathSegment("v2", "api", "tilgang", "person")
            .build()
            .toUri()
    val sporingsdataSakUri =
        UriComponentsBuilder
            .fromUri(tilgangURI)
            .pathSegment("v2", "api", "sporingsdata", "sak")
            .build()
            .toUri()
    val sporingsdataPersonUri =
        UriComponentsBuilder
            .fromUri(tilgangURI)
            .pathSegment("v2", "api", "sporingsdata", "person")
            .build()
            .toUri()

    fun harTilgangSaksnummer(saksnummer: String): Boolean {
        try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.TEXT_PLAIN
            return postForNonNullEntity(sakUri, saksnummer, headers)
        } catch (e: Exception) {
            logger.error("Feil ved sjekk på tilgang til saksnummer $saksnummer ", e)
            return false
        }
    }

    fun harTilgangPerson(personIdent: String): Boolean {
        try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.TEXT_PLAIN
            return postForNonNullEntity(personUri, personIdent, headers)
        } catch (e: Exception) {
            logger.error("Feil ved sjekk på tilgang til person ", e)
            return false
        }
    }

    fun hentSporingsdataSak(saksnummer: String): Sporingsdata {
        try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.TEXT_PLAIN
            return postForNonNullEntity(sporingsdataSakUri, saksnummer, headers)
        } catch (e: Exception) {
            logger.error("Feil ved henting av sporingsdata for sak $saksnummer ", e)
            throw e
        }
    }

    fun hentSporingsdataPerson(personIdent: String): Sporingsdata {
        try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.TEXT_PLAIN
            return postForNonNullEntity(sporingsdataPersonUri, personIdent, headers)
        } catch (e: Exception) {
            logger.error("Feil ved henting av sporingsdata for person ", e)
            throw e
        }
    }
}
