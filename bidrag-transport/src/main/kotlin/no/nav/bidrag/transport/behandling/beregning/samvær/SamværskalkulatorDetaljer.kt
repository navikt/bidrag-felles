package no.nav.bidrag.transport.behandling.beregning.samvær

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.bidrag.domene.enums.samværskalkulator.SamværskalkulatorFerietype
import no.nav.bidrag.domene.enums.samværskalkulator.SamværskalkulatorNetterFrekvens
import no.nav.bidrag.domene.util.avrundetMedToDesimaler
import java.math.BigDecimal

data class SamværskalkulatorDetaljer(
    val ferier: List<SamværskalkulatorFerie> = emptyList(),
    val regelmessigSamværNetter: BigDecimal,
) {
    data class SamværskalkulatorFerie(
        val type: SamværskalkulatorFerietype,
        val bidragsmottakerNetter: BigDecimal = BigDecimal.ZERO,
        val bidragspliktigNetter: BigDecimal = BigDecimal.ZERO,
        val frekvens: SamværskalkulatorNetterFrekvens,
    ) {
        private val frekvensSomAntallNetter =
            if (frekvens == SamværskalkulatorNetterFrekvens.HVERT_ÅR) {
                BigDecimal.TWO
            } else {
                BigDecimal.ONE
            }

        @get:JsonIgnore
        val bidragsmottakerTotalAntallNetterOverToÅr get() =
            bidragsmottakerNetter
                .multiply(frekvensSomAntallNetter)
                .avrundetMedToDesimaler

        @get:JsonIgnore
        val bidragspliktigTotalAntallNetterOverToÅr get() =
            bidragspliktigNetter
                .multiply(frekvensSomAntallNetter)
                .avrundetMedToDesimaler
    }
}
