package no.nav.bidrag.domene.enums.beregning

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.nav.bidrag.domene.enums.beregning.Resultatkode.Companion.erAvslagEllerOpphør
import org.junit.jupiter.api.Test

class ResultatkodeTest {
    @Test
    fun `Skal hente resultatkode basert på legacyKode`() {
        val resultatkode = Resultatkode.fraKode("A")
        val resultatkode2 = Resultatkode.fraKode("AVSLAG")

        resultatkode.shouldNotBeNull()
        resultatkode shouldBe Resultatkode.AVSLAG

        resultatkode2.shouldNotBeNull()
        resultatkode2 shouldBe Resultatkode.AVSLAG
    }

    @Test
    fun `Skal hente avslagskoder`() {
        val resultatkoder = Resultatkode.alleMedType(Resultatkode.ResultatkodeType.AVSLAG)

        resultatkoder.shouldHaveSize(14)
    }

    @Test
    fun `Skal hente avslagskoder og opphørskoder`() {
        Resultatkode.PRIVAT_AVTALE_OM_SÆRBIDRAG.erAvslagEllerOpphør().shouldBeTrue()
        Resultatkode.SÆRBIDRAG_IKKE_FULL_BIDRAGSEVNE.erAvslagEllerOpphør().shouldBeTrue()
        Resultatkode.BARNET_ER_SELVFORSØRGET.erAvslagEllerOpphør().shouldBeTrue()
    }
}
