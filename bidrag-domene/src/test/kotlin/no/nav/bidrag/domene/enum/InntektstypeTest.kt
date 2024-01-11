package no.nav.bidrag.domene.enum

import io.kotest.matchers.shouldBe
import no.nav.bidrag.domene.enums.inntekt.Inntektsrapportering
import no.nav.bidrag.domene.enums.inntekt.Inntektstype
import no.nav.bidrag.domene.enums.inntekt.Inntektstype.Companion.inngårIInntektRapporteringer
import org.junit.jupiter.api.Test

class InntektstypeTest {

    @Test
    fun `skal hente alle inntektRapporteringer som inneholder en gitt inntektType`() {
        Inntektstype.AAP.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.AINNTEKT,
            Inntektsrapportering.AINNTEKT_BEREGNET_3MND,
            Inntektsrapportering.AINNTEKT_BEREGNET_12MND,
            Inntektsrapportering.LIGNINGSINNTEKT,
            Inntektsrapportering.AAP,
            Inntektsrapportering.PERSONINNTEKT_EGNE_OPPLYSNINGER,
            Inntektsrapportering.SAKSBEHANDLER_BEREGNET_INNTEKT,
            Inntektsrapportering.YTELSE_FRA_OFFENTLIG_MANUELT_BEREGNET,
        )

        Inntektstype.DAGPENGER.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.AINNTEKT,
            Inntektsrapportering.AINNTEKT_BEREGNET_3MND,
            Inntektsrapportering.AINNTEKT_BEREGNET_12MND,
            Inntektsrapportering.LIGNINGSINNTEKT,
            Inntektsrapportering.DAGPENGER,
            Inntektsrapportering.PERSONINNTEKT_EGNE_OPPLYSNINGER,
            Inntektsrapportering.SAKSBEHANDLER_BEREGNET_INNTEKT,
            Inntektsrapportering.YTELSE_FRA_OFFENTLIG_MANUELT_BEREGNET,
        )

        Inntektstype.FORELDREPENGER.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.AINNTEKT,
            Inntektsrapportering.AINNTEKT_BEREGNET_3MND,
            Inntektsrapportering.AINNTEKT_BEREGNET_12MND,
            Inntektsrapportering.LIGNINGSINNTEKT,
            Inntektsrapportering.FORELDREPENGER,
            Inntektsrapportering.PERSONINNTEKT_EGNE_OPPLYSNINGER,
            Inntektsrapportering.SAKSBEHANDLER_BEREGNET_INNTEKT,
            Inntektsrapportering.YTELSE_FRA_OFFENTLIG_MANUELT_BEREGNET,
        )

        Inntektstype.INTRODUKSJONSSTØNAD.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.AINNTEKT,
            Inntektsrapportering.AINNTEKT_BEREGNET_3MND,
            Inntektsrapportering.AINNTEKT_BEREGNET_12MND,
            Inntektsrapportering.LIGNINGSINNTEKT,
            Inntektsrapportering.INTRODUKSJONSSTØNAD,
            Inntektsrapportering.PERSONINNTEKT_EGNE_OPPLYSNINGER,
            Inntektsrapportering.SAKSBEHANDLER_BEREGNET_INNTEKT,
            Inntektsrapportering.YTELSE_FRA_OFFENTLIG_MANUELT_BEREGNET,
        )

        Inntektstype.KVALIFISERINGSSTØNAD.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.AINNTEKT,
            Inntektsrapportering.AINNTEKT_BEREGNET_3MND,
            Inntektsrapportering.AINNTEKT_BEREGNET_12MND,
            Inntektsrapportering.LIGNINGSINNTEKT,
            Inntektsrapportering.KVALIFISERINGSSTØNAD,
            Inntektsrapportering.PERSONINNTEKT_EGNE_OPPLYSNINGER,
            Inntektsrapportering.SAKSBEHANDLER_BEREGNET_INNTEKT,
            Inntektsrapportering.YTELSE_FRA_OFFENTLIG_MANUELT_BEREGNET,
        )

        Inntektstype.OVERGANGSSTØNAD.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.AINNTEKT,
            Inntektsrapportering.AINNTEKT_BEREGNET_3MND,
            Inntektsrapportering.AINNTEKT_BEREGNET_12MND,
            Inntektsrapportering.LIGNINGSINNTEKT,
            Inntektsrapportering.OVERGANGSSTØNAD,
            Inntektsrapportering.PERSONINNTEKT_EGNE_OPPLYSNINGER,
            Inntektsrapportering.SAKSBEHANDLER_BEREGNET_INNTEKT,
            Inntektsrapportering.YTELSE_FRA_OFFENTLIG_MANUELT_BEREGNET,
        )

        Inntektstype.PENSJON.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.AINNTEKT,
            Inntektsrapportering.AINNTEKT_BEREGNET_3MND,
            Inntektsrapportering.AINNTEKT_BEREGNET_12MND,
            Inntektsrapportering.LIGNINGSINNTEKT,
            Inntektsrapportering.PENSJON,
            Inntektsrapportering.PERSONINNTEKT_EGNE_OPPLYSNINGER,
            Inntektsrapportering.SAKSBEHANDLER_BEREGNET_INNTEKT,
            Inntektsrapportering.YTELSE_FRA_OFFENTLIG_MANUELT_BEREGNET,
        )

        Inntektstype.SYKEPENGER.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.AINNTEKT,
            Inntektsrapportering.AINNTEKT_BEREGNET_3MND,
            Inntektsrapportering.AINNTEKT_BEREGNET_12MND,
            Inntektsrapportering.LIGNINGSINNTEKT,
            Inntektsrapportering.SYKEPENGER,
            Inntektsrapportering.PERSONINNTEKT_EGNE_OPPLYSNINGER,
            Inntektsrapportering.SAKSBEHANDLER_BEREGNET_INNTEKT,
            Inntektsrapportering.YTELSE_FRA_OFFENTLIG_MANUELT_BEREGNET,
        )

        Inntektstype.KONTANTSTØTTE.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.KONTANTSTØTTE,
        )

        Inntektstype.SMÅBARNSTILLEGG.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.SMÅBARNSTILLEGG,
        )

        Inntektstype.UTVIDET_BARNETRYGD.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.UTVIDET_BARNETRYGD,
        )

        Inntektstype.KAPITALINNTEKT.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.KAPITALINNTEKT,
            Inntektsrapportering.KAPITALINNTEKT_EGNE_OPPLYSNINGER,
        )

        Inntektstype.LØNNSINNTEKT.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.AINNTEKT,
            Inntektsrapportering.AINNTEKT_BEREGNET_3MND,
            Inntektsrapportering.AINNTEKT_BEREGNET_12MND,
            Inntektsrapportering.LIGNINGSINNTEKT,
            Inntektsrapportering.PERSONINNTEKT_EGNE_OPPLYSNINGER,
            Inntektsrapportering.SAKSBEHANDLER_BEREGNET_INNTEKT,
            Inntektsrapportering.LØNN_MANUELT_BEREGNET,
        )

        Inntektstype.NÆRINGSINNTEKT.inngårIInntektRapporteringer() shouldBe listOf(
            Inntektsrapportering.NÆRINGSINNTEKT_MANUELT_BEREGNET,
        )
    }
}
