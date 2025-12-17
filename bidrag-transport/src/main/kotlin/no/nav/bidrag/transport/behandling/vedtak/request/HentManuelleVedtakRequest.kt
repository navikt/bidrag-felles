package no.nav.bidrag.transport.behandling.vedtak.request

data class HentManuelleVedtakRequest(
    @Schema(description = "PeriodeFom som vedtak skal sjekkes mot. Vedtak eldre enn denne datoen skal ikke returneres")
    val periodeFom: YearMonth,
    @Schema(description = "Bidragspliktiges personident")
    val skyldner: Personident,
)