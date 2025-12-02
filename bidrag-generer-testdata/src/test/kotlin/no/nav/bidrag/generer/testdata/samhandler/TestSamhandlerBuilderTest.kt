package no.nav.bidrag.generer.testdata.samhandler

import io.kotest.matchers.shouldNotBe
import no.nav.bidrag.generer.testdata.samhandler.TestSamhandlerBuilder.Companion.samhandler
import org.junit.jupiter.api.Test

class TestSamhandlerBuilderTest {
    @Test
    fun skalOppretteSamhandler() {
        val samhandler = samhandler().opprett()
        samhandler shouldNotBe null
    }
}
