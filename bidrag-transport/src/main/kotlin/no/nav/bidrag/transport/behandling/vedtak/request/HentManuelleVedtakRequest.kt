package no.nav.bidrag.transport.behandling.vedtak.request

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.ident.Personident
import java.time.YearMonth

data class HentManuelleVedtakRequest(
    @Schema(description = "PeriodeFom som vedtak skal sjekkes mot. Vedtak eldre enn denne datoen skal ikke returneres")
    val periodeFom: YearMonth,
    @Schema(description = "Bidragspliktiges personident")
    val skyldner: Personident,
)