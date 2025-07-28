@file:Suppress("unused")

package no.nav.bidrag.domene.beløp

import no.nav.bidrag.domene.enums.samhandler.Valutakode
import no.nav.bidrag.domene.felles.Verdiobjekt
import no.nav.bidrag.domene.ident.Organisasjonsnummer
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.ident.SamhandlerId
import java.math.BigDecimal

class Beløp(
    override val verdi: BigDecimal,
    val valutakode: Valutakode?,
) : Verdiobjekt<BigDecimal>() {
    override fun gyldig(): Boolean = verdi >= BigDecimal.ZERO && (valutakode == null || valutakode in Valutakode.entries)

    fun erINorskeKroner() = valutakode == null || valutakode == Valutakode.NOK

    fun erISvenskeKroner() = valutakode != null && valutakode == Valutakode.SEK

    fun erIIslandskeKroner() = valutakode != null && valutakode == Valutakode.ISK

    fun erIEuro() = valutakode != null && valutakode == Valutakode.EUR

    fun erIUtenlandskValuta() = valutakode != null && valutakode != Valutakode.NOK
}
