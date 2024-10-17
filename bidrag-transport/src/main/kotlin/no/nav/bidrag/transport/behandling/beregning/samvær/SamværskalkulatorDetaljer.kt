package no.nav.bidrag.transport.behandling.beregning.samvær

import no.nav.bidrag.domene.enums.beregning.Samværsklasse

data class SamværskalkulatorDetaljer(
    val ferier: List<SamværskalkulatorFerie> = emptyList(),
    val regelmessigSamværNetter: Int,
    val samværsklasse: Samværsklasse,
) {
    data class SamværskalkulatorFerie(
        val type: SamværskalkulatorFerietype,
        val bidragsmottaker: SamværskalkulatorFerieNetter,
        val bidragspliktig: SamværskalkulatorFerieNetter,
    )

    data class SamværskalkulatorFerieNetter(
        val antallNetter: Int = 0,
        val frekvens: SamværskalkulatorNetterFrekvens,
    ) {
        val totalAntallNetterOverToÅr get() = antallNetter * if (frekvens == SamværskalkulatorNetterFrekvens.HVERT_ÅR) 2 else 1
    }

    enum class SamværskalkulatorNetterFrekvens {
        HVERT_ÅR,
        ANNEN_HVERT_ÅR,
    }

    enum class SamværskalkulatorFerietype {
        JUL_NYTTÅR,
        VINTERFERIE,
        PÅSKE,
        SOMMERFERIE,
        HØSTFERIE,
        ANNET,
    }
}
