package no.nav.bidrag.generer.testdata.sak

import io.kotest.matchers.shouldNotBe
import org.junit.Test

class GenererSaksnummerTest {

    @Test
    fun skalGenerereSaksnummer() {
        val saksnummer = genererSaksnummer()
        saksnummer shouldNotBe null
        println(saksnummer)
    }
}