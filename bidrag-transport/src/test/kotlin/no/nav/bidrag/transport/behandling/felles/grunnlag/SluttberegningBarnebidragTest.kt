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
            kostnadsberegnetBidrag = BigDecimal.ONE,
            nettoBarnetilleggBP = BigDecimal.ONE,
            nettoBarnetilleggBM = BigDecimal.ONE,
            ingenEndringUnderGrense = false,
            justertNedTilEvne = false,
            justertNedTil25ProsentAvInntekt = false,
            justertForNettoBarnetilleggBP = false,
            justertForNettoBarnetilleggBM = false,
        )

    @Test
    fun `skal oversette til riktig bisys resultatkode`() {
        sluttberegning.bisysResultatkode shouldBe "BB"
        sluttberegning
            .copy(
                ingenEndringUnderGrense = true,
            ).bisysResultatkode shouldBe "VO"
        sluttberegning
            .copy(
                justertNedTilEvne = true,
            ).bisysResultatkode shouldBe "6MB"

        sluttberegning
            .copy(
                justertNedTil25ProsentAvInntekt = true,
            ).bisysResultatkode shouldBe "7M"

        sluttberegning
            .copy(
                justertForNettoBarnetilleggBP = true,
            ).bisysResultatkode shouldBe "101"

        sluttberegning
            .copy(
                justertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "102"
    }

    @Test
    fun `skal oversette til riktig bisys resultat hvis justert ned til BPs barnetillegg`() {
        sluttberegning
            .copy(
                justertNedTilEvne = true,
                justertForNettoBarnetilleggBP = true,
            ).bisysResultatkode shouldBe "101"

        sluttberegning
            .copy(
                justertNedTil25ProsentAvInntekt = true,
                justertForNettoBarnetilleggBP = true,
            ).bisysResultatkode shouldBe "101"

        sluttberegning
            .copy(
                justertForNettoBarnetilleggBM = true,
                justertForNettoBarnetilleggBP = true,
            ).bisysResultatkode shouldBe "101"

        sluttberegning
            .copy(
                justertNedTilEvne = true,
                justertNedTil25ProsentAvInntekt = true,
                justertForNettoBarnetilleggBM = true,
                justertForNettoBarnetilleggBP = true,
            ).bisysResultatkode shouldBe "101"
    }

    @Test
    fun `skal oversette til riktig bisys resultat hvis justert ned til BMs barnetillegg`() {
        sluttberegning
            .copy(
                justertNedTilEvne = true,
                justertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "102"

        sluttberegning
            .copy(
                justertNedTil25ProsentAvInntekt = true,
                justertForNettoBarnetilleggBM = true,
            ).bisysResultatkode shouldBe "102"

        sluttberegning
            .copy(
                justertNedTilEvne = true,
                justertNedTil25ProsentAvInntekt = true,
                justertForNettoBarnetilleggBM = true,
                justertForNettoBarnetilleggBP = false,
            ).bisysResultatkode shouldBe "102"
    }

    @Test
    fun `skal oversette til riktig bisys resultat hvis ingen endring under grense`() {
        sluttberegning
            .copy(
                justertNedTilEvne = true,
                justertNedTil25ProsentAvInntekt = true,
                justertForNettoBarnetilleggBM = true,
                justertForNettoBarnetilleggBP = true,
                ingenEndringUnderGrense = true,
            ).bisysResultatkode shouldBe "VO"
    }

    @Test
    fun `skal oversette til riktig visningsnavn`() {
        sluttberegning
            .copy(
                justertNedTilEvne = true,
                justertNedTil25ProsentAvInntekt = true,
                justertForNettoBarnetilleggBM = true,
                justertForNettoBarnetilleggBP = true,
                ingenEndringUnderGrense = true,
            ).resultatVisningsnavn
            ?.intern shouldBe "Ingen endring under grense"

        sluttberegning
            .copy(
                justertNedTilEvne = true,
                justertNedTil25ProsentAvInntekt = true,
                justertForNettoBarnetilleggBM = true,
                justertForNettoBarnetilleggBP = true,
                ingenEndringUnderGrense = false,
            ).resultatVisningsnavn
            ?.intern shouldBe "Justert for netto barnetillegg BP"

        sluttberegning
            .copy(
                justertNedTilEvne = true,
                justertNedTil25ProsentAvInntekt = true,
                justertForNettoBarnetilleggBM = true,
                justertForNettoBarnetilleggBP = false,
                ingenEndringUnderGrense = false,
            ).resultatVisningsnavn
            ?.intern shouldBe "Justert for netto barnetillegg BM"

        sluttberegning
            .copy(
                justertNedTil25ProsentAvInntekt = true,
                justertNedTilEvne = false,
                justertForNettoBarnetilleggBM = false,
                justertForNettoBarnetilleggBP = false,
                ingenEndringUnderGrense = false,
            ).resultatVisningsnavn
            ?.intern shouldBe "Justert ned til 25 prosent av inntekt"

        sluttberegning
            .copy(
                justertNedTilEvne = true,
                justertNedTil25ProsentAvInntekt = false,
                justertForNettoBarnetilleggBM = false,
                justertForNettoBarnetilleggBP = false,
                ingenEndringUnderGrense = false,
            ).resultatVisningsnavn
            ?.intern shouldBe "Justert ned til evne"

        sluttberegning
            .copy(
                justertNedTilEvne = false,
                justertNedTil25ProsentAvInntekt = false,
                justertForNettoBarnetilleggBM = false,
                justertForNettoBarnetilleggBP = false,
                ingenEndringUnderGrense = false,
            ).resultatVisningsnavn
            ?.intern shouldBe "Kostnadsberegnet bidrag"
    }
}
