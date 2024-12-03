package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.kotest.matchers.shouldBe
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

class SluttberegningBarnebidragTest {
    private val sluttberegning =
        SluttberegningBarnebidrag(
            periode = ÅrMånedsperiode(LocalDate.now(), null),
            beregnetBeløp = BigDecimal.ONE,
            resultatBeløp = BigDecimal.ONE,
            uMinusNettoBarnetilleggBM = BigDecimal.ONE,
            bruttoBidragEtterBarnetilleggBM = BigDecimal.ONE,
            nettoBidragEtterBarnetilleggBM = BigDecimal.ONE,
            bruttoBidragJustertForEvneOg25Prosent = BigDecimal.ONE,
            bruttoBidragEtterBarnetilleggBP = BigDecimal.ONE,
            nettoBidragEtterSamværsfradrag = BigDecimal.ONE,
            bpAndelAvUVedDeltBostedFaktor = BigDecimal.ONE,
            bpAndelAvUVedDeltBostedBeløp = BigDecimal.ONE,
            ingenEndringUnderGrense = false,
            barnetErSelvforsørget = false,
            bidragJustertForDeltBosted = false,
            bidragJustertForNettoBarnetilleggBP = false,
            bidragJustertForNettoBarnetilleggBM = false,
            bidragJustertNedTilEvne = false,
            bidragJustertNedTil25ProsentAvInntekt = false,
        )

    @Test
    fun `skal oversette til riktig bisys resultatkode`() {
        sluttberegning.bisysResultatkode shouldBe "KBB"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = true,
                barnetErSelvforsørget = true,
                bidragJustertForDeltBosted = true,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = true,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "VO"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = true,
                bidragJustertForDeltBosted = true,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = true,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "5SF"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = true,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = true,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "6MB"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = true,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = false,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "7M"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = true,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = false,
                bidragJustertNedTil25ProsentAvInntekt = false,
                bidragJustertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "8DN"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = false,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = true,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "101"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = false,
                bidragJustertForNettoBarnetilleggBP = false,
                bidragJustertNedTilEvne = true,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "6MB"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = false,
                bidragJustertForNettoBarnetilleggBP = false,
                bidragJustertNedTilEvne = false,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "7M"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = false,
                bidragJustertForNettoBarnetilleggBP = false,
                bidragJustertNedTilEvne = false,
                bidragJustertNedTil25ProsentAvInntekt = false,
                bidragJustertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "102"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = false,
                bidragJustertForNettoBarnetilleggBP = false,
                bidragJustertNedTilEvne = false,
                bidragJustertNedTil25ProsentAvInntekt = false,
                bidragJustertForNettoBarnetilleggBM = false,
            ).bisysResultatkode shouldBe "KBB"
    }

    @Test
    fun `skal oversette til riktig visningsnavn`() {
        sluttberegning.resultatVisningsnavn?.intern shouldBe "Kostnadsberegnet bidrag"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = true,
                barnetErSelvforsørget = true,
                bidragJustertForDeltBosted = true,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = true,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).resultatVisningsnavn
            ?.intern shouldBe "Ingen endring under grense"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = true,
                bidragJustertForDeltBosted = true,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = true,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).resultatVisningsnavn
            ?.intern shouldBe "Barnet er selvforsørget"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = true,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = true,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).resultatVisningsnavn
            ?.intern shouldBe "Bidrag redusert til evne"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = true,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = false,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).resultatVisningsnavn
            ?.intern shouldBe "Bidrag redusert til 25 prosent av inntekt"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = true,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = false,
                bidragJustertNedTil25ProsentAvInntekt = false,
                bidragJustertForNettoBarnetilleggBM = true,
            ).resultatVisningsnavn
            ?.intern shouldBe "Bidrag justert for delt bosted"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = false,
                bidragJustertForNettoBarnetilleggBP = true,
                bidragJustertNedTilEvne = true,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).resultatVisningsnavn
            ?.intern shouldBe "Bidrag satt til barnetillegg fra BP"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = false,
                bidragJustertForNettoBarnetilleggBP = false,
                bidragJustertNedTilEvne = true,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).resultatVisningsnavn
            ?.intern shouldBe "Bidrag redusert til evne"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = false,
                bidragJustertForNettoBarnetilleggBP = false,
                bidragJustertNedTilEvne = false,
                bidragJustertNedTil25ProsentAvInntekt = true,
                bidragJustertForNettoBarnetilleggBM = true,
            ).resultatVisningsnavn
            ?.intern shouldBe "Bidrag redusert til 25 prosent av inntekt"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = false,
                bidragJustertForNettoBarnetilleggBP = false,
                bidragJustertNedTilEvne = false,
                bidragJustertNedTil25ProsentAvInntekt = false,
                bidragJustertForNettoBarnetilleggBM = true,
            ).resultatVisningsnavn
            ?.intern shouldBe "Bidrag satt til underholdskostnad minus barnetillegg BM"

        sluttberegning
            .copy(
                ingenEndringUnderGrense = false,
                barnetErSelvforsørget = false,
                bidragJustertForDeltBosted = false,
                bidragJustertForNettoBarnetilleggBP = false,
                bidragJustertNedTilEvne = false,
                bidragJustertNedTil25ProsentAvInntekt = false,
                bidragJustertForNettoBarnetilleggBM = false,
            ).resultatVisningsnavn
            ?.intern shouldBe "Kostnadsberegnet bidrag"
    }
}
