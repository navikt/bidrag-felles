package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.enums.person.AldersgruppeForskudd
import no.nav.bidrag.domene.sak.Saksnummer
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal
import java.math.MathContext

data class SluttberegningForskudd(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
    val resultatKode: Resultatkode,
    val aldersgruppe: AldersgruppeForskudd,
) : Sluttberegning

data class SluttberegningSærbidrag(
    override val periode: ÅrMånedsperiode,
    val beregnetBeløp: BigDecimal,
    val resultatKode: Resultatkode,
    val resultatBeløp: BigDecimal?,
) : Sluttberegning

data class SluttberegningBarnebidrag(
    override val periode: ÅrMånedsperiode,
    val beregnetBeløp: BigDecimal,
    val resultatKode: Resultatkode,
    val resultatBeløp: BigDecimal,
    val kostnadsberegnetBidrag: BigDecimal,
    val nettoBarnetilleggBP: BigDecimal,
    val nettoBarnetilleggBM: BigDecimal,
    val justertNedTilEvne: Boolean,
    val justertNedTil25ProsentAvInntekt: Boolean,
    val justertForNettoBarnetilleggBP: Boolean,
    val justertForNettoBarnetilleggBM: Boolean,
) : Sluttberegning

@Deprecated("", replaceWith = ReplaceWith("DelberegningSumInntekt"))
data class DelberegningInntekt(
    override val periode: ÅrMånedsperiode,
    val summertBeløp: BigDecimal,
) : Delberegning

data class DelberegningSumInntekt(
    override val periode: ÅrMånedsperiode,
    val totalinntekt: BigDecimal,
    val kontantstøtte: BigDecimal? = null,
    val skattepliktigInntekt: BigDecimal? = null,
    val barnetillegg: BigDecimal? = null,
    val utvidetBarnetrygd: BigDecimal? = null,
    val småbarnstillegg: BigDecimal? = null,
) : Delberegning

data class DelberegningBarnIHusstand(
    override val periode: ÅrMånedsperiode,
    val antallBarn: Double,
) : Delberegning

data class DelberegningBidragsevne(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
    val skatt: Skatt,
    val underholdBarnEgenHusstand: BigDecimal,
    val sumInntekt25Prosent: BigDecimal = BigDecimal.ZERO,
) : Delberegning {
    data class Skatt(
        val minstefradrag: BigDecimal,
        val skattAlminneligInntekt: BigDecimal,
        val trinnskatt: BigDecimal,
        val trygdeavgift: BigDecimal,
        val sumSkatt: BigDecimal,
        val sumSkattFaktor: BigDecimal = BigDecimal.ZERO,
    )
}

data class DelberegningVoksneIHusstand(
    override val periode: ÅrMånedsperiode,
    val borMedAndreVoksne: Boolean,
) : Delberegning

typealias DelberegningVoksneIHustand = DelberegningVoksneIHusstand

data class DelberegningBoforhold(
    override val periode: ÅrMånedsperiode,
    val antallBarn: Double,
    val borMedAndreVoksne: Boolean,
) : Delberegning

data class DelberegningBidragspliktigesAndel(
    override val periode: ÅrMånedsperiode,
    @JsonAlias("andelFaktor", "andelProsent")
    val endeligAndelFaktor: BigDecimal,
    val andelBeløp: BigDecimal,
    val beregnetAndelFaktor: BigDecimal,
    val barnEndeligInntekt: BigDecimal,
    val barnetErSelvforsørget: Boolean,
) : Delberegning {
    @get:JsonIgnore
    val andelProsent: BigDecimal
        get() =
            if (endeligAndelFaktor < BigDecimal.ONE) {
                endeligAndelFaktor
                    .multiply(BigDecimal(100))
                    .round(MathContext(4))
            } else {
                endeligAndelFaktor.round(MathContext(4))
            }

    @get:JsonIgnore
    val erAndelRedusert: Boolean
        get() = endeligAndelFaktor < beregnetAndelFaktor
}

data class DelberegningUtgift(
    override val periode: ÅrMånedsperiode,
    val sumBetaltAvBp: BigDecimal,
    val sumGodkjent: BigDecimal,
) : Delberegning

data class DelberegningBidragspliktigesBeregnedeTotalbidrag(
    override val periode: ÅrMånedsperiode,
    @JsonAlias("sum")
    val bidragspliktigesBeregnedeTotalbidrag: BigDecimal,
    val beregnetBidragPerBarnListe: List<BeregnetBidragPerBarn> = emptyList(),
) : Delberegning

data class BeregnetBidragPerBarn(
    val gjelderBarn: Grunnlagsreferanse,
    val saksnummer: Saksnummer,
    val løpendeBeløp: BigDecimal,
    val valutakode: String = "NOK",
    val samværsklasse: Samværsklasse,
    val samværsfradrag: BigDecimal,
    val beregnetBeløp: BigDecimal,
    val faktiskBeløp: BigDecimal,
    val reduksjonUnderholdskostnad: BigDecimal,
    val beregnetBidrag: BigDecimal,
)

data class DelberegningSamværsfradrag(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
) : Delberegning

data class DelberegningNettoTilsynsutgift(
    override val periode: ÅrMånedsperiode,
    val totaltFaktiskUtgiftBeløp: BigDecimal,
    val tilsynsutgiftBarnListe: List<TilsynsutgiftBarn>,
) : Delberegning

data class TilsynsutgiftBarn(
    val gjelderBarn: Grunnlagsreferanse,
    val sumFaktiskeUtgifter: BigDecimal,
    val endeligSumFaktiskeUtgifter: BigDecimal,
    val skattefradragsbeløpPerBarn: BigDecimal,
    val tilleggsstønad: BigDecimal,
    val nettoTilsynsutgift: BigDecimal,
)

fun List<GrunnlagInnhold>.filtrerDelberegninger() = filterIsInstance<Delberegning>()

data class DelberegningUnderholdskostnad(
    override val periode: ÅrMånedsperiode,
    val forbruksutgift: BigDecimal,
    val boutgift: BigDecimal,
    val barnetilsynMedStønad: BigDecimal,
    val nettoTilsynsutgift: BigDecimal,
    val barnetrygd: BigDecimal,
    val underholdskostnad: BigDecimal,
) : Delberegning
