package no.nav.bidrag.domene.enums.beregning

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
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

        resultatkoder.shouldHaveSize(6)
    }
}
