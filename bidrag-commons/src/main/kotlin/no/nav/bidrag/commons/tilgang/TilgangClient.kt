package no.nav.bidrag.commons.tilgang

import no.nav.bidrag.commons.web.client.AbstractRestClient
import no.nav.bidrag.commons.web.config.RestOperationsAzure
import no.nav.bidrag.transport.tilgang.Sporingsdata
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
    val sakUri =
        UriComponentsBuilder
            .fromUri(tilgangURI)
            .pathSegment("api", "tilgang", "sak")
            .build()
            .toUri()
    val personUri =
        UriComponentsBuilder
            .fromUri(tilgangURI)
            .pathSegment("api", "tilgang", "person")
            .build()
            .toUri()
    val sporingsdataSakUri =
        UriComponentsBuilder
            .fromUri(tilgangURI)
            .pathSegment("api", "sporingsdata", "sak")
            .build()
            .toUri()
    val sporingsdataPersonUri =
        UriComponentsBuilder
            .fromUri(tilgangURI)
            .pathSegment("api", "sporingsdata", "person")
            .build()
            .toUri()

    fun harTilgangSaksnummer(saksnummer: String): Boolean {
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        return postForNonNullEntity(sakUri, saksnummer, headers)
    }

    fun harTilgangPerson(personIdent: String): Boolean {
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        return postForNonNullEntity(personUri, personIdent, headers)
    }

    fun hentSporingsdataSak(saksnummer: String): Sporingsdata {
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        return postForNonNullEntity(sporingsdataSakUri, saksnummer, headers)
    }

    fun hentSporingsdataPerson(personIdent: String): Sporingsdata {
        val headers = HttpHeaders()
        headers.contentType = MediaType.TEXT_PLAIN
        return postForNonNullEntity(sporingsdataPersonUri, personIdent, headers)
    }
}
