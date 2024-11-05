package no.nav.bidrag.transport.behandling.stonad.request

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.ident.Personident

@Schema(description = "Request for å hente alle bidragssaker for angitt skyldner")
data class SkyldnerBidragssakerRequest(
    @Schema(description = "Personen som er skyldner")
    val skyldner: Personident,
)
