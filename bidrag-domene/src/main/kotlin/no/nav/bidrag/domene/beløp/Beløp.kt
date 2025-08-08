@file:Suppress("unused")

package no.nav.bidrag.domene.beløp

import no.nav.bidrag.domene.enums.samhandler.Valutakode
import no.nav.bidrag.domene.felles.Verdiobjekt
import java.math.BigDecimal

data class Beløp(
    override val verdi: BigDecimal,
    val valutakode: Valutakode? = Valutakode.NOK,
) : Verdiobjekt<BigDecimal>() {
    override fun gyldig(): Boolean = verdi >= BigDecimal.ZERO && (valutakode == null || valutakode in Valutakode.entries)

    fun erINorskeKroner() = valutakode == null || valutakode == Valutakode.NOK

    fun erISvenskeKroner() = valutakode != null && valutakode == Valutakode.SEK

    fun erIIslandskeKroner() = valutakode != null && valutakode == Valutakode.ISK

    fun erIEuro() = valutakode != null && valutakode == Valutakode.EUR

    fun erIUtenlandskValuta() = valutakode != null && valutakode != Valutakode.NOK
}
