package no.nav.bidrag.transport.behandling.stonad.response

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.sak.Saksnummer

@Schema(description = "Respons med alle bidragstønader for angitt skyldner")
data class SkyldnerBidragstønaderResponse(
    @Schema(description = "Liste med info om skyldnerens bidragstønader")
    val bidragssakerListe: List<SkyldnerBidragstønader> = emptyList(),
)

@Schema(description = "Objekt med relevant informasjon om skyldners bidragstønader")
data class SkyldnerBidragstønader(
    @Schema(description = "Saksnummer")
    val sak: Saksnummer,
    @Schema(description = "Hvilken type stønad det er snakk om")
    val type: Stønadstype,
    @Schema(description = "Personen som er kravhaver i stønaden")
    val kravhaver: Personident,
)
