package no.nav.bidrag.transport.behandling.stonad.response

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.sak.Saksnummer
import no.nav.bidrag.domene.tid.Datoperiode
import java.math.BigDecimal

@Schema(description = "Respons med alle bidragssaker for angitt skyldner")
data class SkyldnerBidragssakerResponse(
    @Schema(description = "Liste med info om skyldnerens bidragssaker")
    val bidragssakerListe: List<SkyldnerBidragssak> = emptyList(),
)

@Schema(description = "Objekt med relevant informasjon om skyldners bidragssaker")
data class SkyldnerBidragssak(
    @Schema(description = "Saksnummer")
    val sak: Saksnummer,
    @Schema(description = "Hvilken type stønad det er snakk om")
    val type: Stønadstype,
    @Schema(description = "Personen som er kravhaver i stønaden")
    val kravhaver: Personident,
    val perioder: List<SkylderBidragssakPeriode>,
)

@Schema(description = "Objekt med relevant informasjon om periode for skyldners bidragssaker")
data class SkylderBidragssakPeriode(
    val periode: Datoperiode,
    @Schema(description = "Beløp i stønaden")
    val beløp: BigDecimal,
    @Schema(description = "Valutakoden tilhørende stønadsbeløpet")
    val valutakode: String = "NOK",
)
