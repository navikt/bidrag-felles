package no.nav.bidrag.domene.util

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.beregning.ResultatkodeBarnebidrag
import no.nav.bidrag.domene.enums.beregning.ResultatkodeForskudd
import no.nav.bidrag.domene.enums.beregning.ResultatkodeSærtilskudd
import no.nav.bidrag.domene.enums.diverse.Språk
import no.nav.bidrag.domene.enums.inntekt.Inntektsrapportering
import no.nav.bidrag.domene.enums.inntekt.Inntektstype
import no.nav.bidrag.domene.enums.person.Bostatuskode
import no.nav.bidrag.domene.enums.person.Sivilstandskode
import no.nav.bidrag.domene.enums.vedtak.VirkningstidspunktÅrsakstype
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class VisningsnavnTest {
    @Nested
    internal inner class InntektstypeTest {
        @Test
        fun `Valider at alle kodeverdier har visningsnavn`() {
            Inntektstype.entries.forEach {
                withClue("${it.name} mangler visningsnavn") {
                    it.visningsnavn.intern.isNotEmpty() shouldBe true
                }
            }
        }
    }

    @Nested
    internal inner class VirkningstidspunktÅrsakstypeTest {
        @Test
        fun `Valider at alle kodeverdier har visningsnavn`() {
            VirkningstidspunktÅrsakstype.entries.forEach {
                withClue("${it.name} mangler visningsnavn") {
                    it.visningsnavn.intern.isNotEmpty() shouldBe true
                }
            }
        }
    }

    @Nested
    internal inner class InntektsrapporteringTest {
        @Test
        fun `Valider at alle kodeverdier har visningsnavn`() {
            Inntektsrapportering.entries.forEach {
                withClue("${it.name} mangler visningsnavn") {
                    it.visningsnavn.intern.isNotEmpty() shouldBe true
                }
            }
        }

        @Test
        fun `Skal returnere inntekstrapporteringer som krever årstall`() {
            Inntektsrapportering.visningsnavnSomKreverÅrstall shouldContainAll
                listOf(
                    Inntektsrapportering.LIGNINGSINNTEKT,
                    Inntektsrapportering.AINNTEKT,
                    Inntektsrapportering.KAPITALINNTEKT,
                )
        }

        @Test
        fun `Skal hente visningsnavn for inntekt AINNTEKT_BEREGNET_12MND`() {
            val visningsnavn = Inntektsrapportering.AINNTEKT_BEREGNET_12MND.visningsnavn
            val visningsnavnSaksbehandler = Inntektsrapportering.AINNTEKT_BEREGNET_12MND.visningsnavnIntern(2020)

            visningsnavn.intern shouldBe "A-inntekt siste 12 måneder"
            visningsnavnSaksbehandler shouldBe "A-inntekt siste 12 måneder 2020"
            visningsnavn.bruker[Språk.NB] shouldBe "Opplysninger fra arbeidsgiver"
        }

        @Test
        fun `Skal hente visningsnavn for inntekt KAPITALINNTEKT`() {
            val visningsnavn = Inntektsrapportering.KAPITALINNTEKT.visningsnavn
            val visningsnavnSaksbehandler = Inntektsrapportering.KAPITALINNTEKT.visningsnavnIntern(2020)

            visningsnavn.intern shouldBe "Kapitalinntekt"
            visningsnavnSaksbehandler shouldBe "Kapitalinntekt 2020"
            visningsnavn.bruker[Språk.NB] shouldBe "Kapitalinntekt"
        }
    }

    @Nested
    internal inner class SivilstandTest {
        @Test
        fun `Valider at alle kodeverdier har visningsnavn`() {
            Sivilstandskode.entries.forEach {
                withClue("${it.name} mangler visningsnavn") {
                    it.visningsnavn.intern.isNotEmpty() shouldBe true
                }
            }
        }

        @Test
        fun `Skal hente visningsnavn for sivilstand GIFT_SAMBOER`() {
            val visningsnavn = Sivilstandskode.GIFT_SAMBOER.visningsnavn

            visningsnavn.intern shouldBe "Gift/samboer"
            visningsnavn.bruker[Språk.NB] shouldBe "Gift/samboer"
        }
    }

    @Nested
    internal inner class BostatuskodeTest {
        @Test
        fun `Valider at alle kodeverdier har visningsnavn`() {
            Bostatuskode.entries.forEach {
                withClue("${it.name} mangler visningsnavn") {
                    it.visningsnavn.intern.isNotEmpty() shouldBe true
                }
            }
        }

        @Test
        fun `Skal hente visningsnavn for bostatus`() {
            assertSoftly("Visningsnavn med forelder") {
                val visningsnavn = Bostatuskode.MED_FORELDER.visningsnavn

                visningsnavn.intern shouldBe "Registrert på adresse"
                visningsnavn.bruker[Språk.NB] shouldBe "Registrert på adresse"
            }

            assertSoftly("Visningsnavn med dokumentert skolegang") {
                val visningsnavn = Bostatuskode.DOKUMENTERT_SKOLEGANG.visningsnavn

                visningsnavn.intern shouldBe "Dokumentert skolegang"
                visningsnavn.bruker[Språk.NB] shouldBe "Dokumentert skolegang"
            }

            assertSoftly("Visningsnavn med ikke med forelder") {
                val visningsnavn = Bostatuskode.IKKE_MED_FORELDER.visningsnavn

                visningsnavn.intern shouldBe "Ikke registrert på adresse"
                visningsnavn.bruker[Språk.NB] shouldBe "Ikke registrert på adresse"
            }

            assertSoftly("Visningsnavn med verge") {
                val visningsnavn = Bostatuskode.MED_VERGE.visningsnavn

                visningsnavn.intern shouldBe "Bor med verge"
                visningsnavn.bruker[Språk.NB] shouldBe "Bor med verge"
            }

            assertSoftly("Visningsnavn alene") {
                val visningsnavn = Bostatuskode.ALENE.visningsnavn

                visningsnavn.intern shouldBe "Bor alene"
                visningsnavn.bruker[Språk.NB] shouldBe "Bor alene"
            }

            assertSoftly("Visningsnavn delt bosted") {
                val visningsnavn = Bostatuskode.DELT_BOSTED.visningsnavn

                visningsnavn.intern shouldBe "Delt bosted"
                visningsnavn.bruker[Språk.NB] shouldBe "Delt bosted"
            }
        }
    }

    @Nested
    internal inner class ResultatkodeTest {
        @Test
        fun `Valider at alle kodeverdier har visningsnavn`() {
            Resultatkode.entries.forEach {
                withClue("${it.name} mangler visningsnavn") {
                    it.visningsnavn.intern.isNotEmpty() shouldBe true
                }
            }
            ResultatkodeForskudd.entries.forEach {
                withClue("${it.name} mangler visningsnavn") {
                    it.visningsnavn.intern.isNotEmpty() shouldBe true
                }
            }
            ResultatkodeSærtilskudd.entries.forEach {
                withClue("${it.name} mangler visningsnavn") {
                    it.visningsnavn.intern.isNotEmpty() shouldBe true
                }
            }
            ResultatkodeBarnebidrag.entries.forEach {
                withClue("${it.name} mangler visningsnavn") {
                    it.visningsnavn.intern.isNotEmpty() shouldBe true
                }
            }
        }

        @Test
        fun `Skal hente visningsnavn for forskudd resultatkode FORHØYET_FORSKUDD_100_PROSENT`() {
            val visningsnavn = ResultatkodeForskudd.FORHØYET_FORSKUDD_100_PROSENT.visningsnavn

            visningsnavn.intern shouldBe "Forhøyet forskudd"
            visningsnavn.bruker[Språk.NB] shouldBe "Forhøyet forskudd"
        }

        @Test
        fun `Skal hente visningsnavn for særtilskudd esultatkode BARNET_ER_SELVFORSØRGET`() {
            val visningsnavn = ResultatkodeSærtilskudd.BARNET_ER_SELVFORSØRGET.visningsnavn

            visningsnavn.intern shouldBe "Barnet er selvforsørget"
            visningsnavn.bruker[Språk.NB] shouldBe "Barnet er selvforsørget"
        }

        @Test
        fun `Skal hente visningsnavn for barnebidrag esultatkode KOSTNADSBEREGNET_BIDRAG`() {
            val visningsnavn = ResultatkodeBarnebidrag.KOSTNADSBEREGNET_BIDRAG.visningsnavn

            visningsnavn.intern shouldBe "Kostnadsberegnet bidrag"
            visningsnavn.bruker[Språk.NB] shouldBe "Kostnadsberegnet bidrag"
        }
    }
}
