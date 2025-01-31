package no.nav.bidrag.transport.behandling.stonad.response

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

@Schema(description = "Respons med stønad og beløpsinformasjon for alle perioder")
data class StønadMedPeriodeBeløpResponse(
    @Schema(description = "Angir første år en stønad skal indeksreguleres")
    val førsteIndeksreguleringsår: Int?,
    @Schema(description = "Liste med perioder med beløpsinformasjon for stønaden")
    val periodeBeløpListe: List<PeriodeBeløp> = emptyList(),
)

data class PeriodeBeløp(
    @Schema(description = "Periode med fra-og-med-dato og til-dato med format ÅÅÅÅ-MM")
    val periode: ÅrMånedsperiode,
    @Schema(description = "Beregnet stønadsbeløp")
    val beløp: BigDecimal?,
    @Schema(description = "Valutakode for stønadsbeløpet")
    val valutakode: String?,
)
