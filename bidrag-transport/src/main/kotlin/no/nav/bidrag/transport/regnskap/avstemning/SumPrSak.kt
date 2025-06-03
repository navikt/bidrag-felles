package no.nav.bidrag.transport.regnskap.avstemning

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import java.math.BigDecimal
import java.time.YearMonth

data class SumPrSakResponse(
    val saker: List<SumPrSak>,
)

data class SumPrSak(
    @Schema(description = "Saksnummer.")
    val saksnummer: String,
    @Schema(description = "Antall innslag av innsendt stønadstype for saken.")
    val antall: Long,
    @Schema(description = "Samlet beløp for alle innslag i saken.")
    val beløp: BigDecimal,
)

data class SumPrSakRequest(
    @Schema(description = "Stønadstypen som skal summeres.")
    val stønadstype: Stønadstype,
    @Schema(description = "Måned det skal summeres for. Sjekkes om er større eller lik fra dato og mindre enn til dato.")
    val måned: YearMonth
)