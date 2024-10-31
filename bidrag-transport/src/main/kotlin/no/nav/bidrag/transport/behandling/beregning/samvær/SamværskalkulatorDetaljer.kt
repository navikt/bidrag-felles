package no.nav.bidrag.transport.behandling.beregning.samvær

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.bidrag.domene.enums.samværskalkulator.SamværskalkulatorFerietype
import no.nav.bidrag.domene.enums.samværskalkulator.SamværskalkulatorNetterFrekvens

data class SamværskalkulatorDetaljer(
    val ferier: List<SamværskalkulatorFerie> = emptyList(),
    val regelmessigSamværNetter: Int,
) {
    data class SamværskalkulatorFerie(
        val type: SamværskalkulatorFerietype,
        val bidragsmottakerNetter: Int = 0,
        val bidragspliktigNetter: Int = 0,
        val frekvens: SamværskalkulatorNetterFrekvens,
    ) {
        @get:JsonIgnore
        val bidragsmottakerTotalAntallNetterOverToÅr get() =
            bidragsmottakerNetter *
                if (frekvens == SamværskalkulatorNetterFrekvens.HVERT_ÅR) 2 else 1

        @get:JsonIgnore
        val bidragspliktigTotalAntallNetterOverToÅr get() =
            bidragspliktigNetter *
                if (frekvens == SamværskalkulatorNetterFrekvens.HVERT_ÅR) 2 else 1
    }
}
