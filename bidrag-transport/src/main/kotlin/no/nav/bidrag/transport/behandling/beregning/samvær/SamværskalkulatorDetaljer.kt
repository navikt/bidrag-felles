package no.nav.bidrag.transport.behandling.beregning.samvær

import no.nav.bidrag.domene.enums.samværskalkulator.SamværskalkulatorFerietype
import no.nav.bidrag.domene.enums.samværskalkulator.SamværskalkulatorNetterFrekvens
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
    )
}
