package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.enums.person.AldersgruppeForskudd
import no.nav.bidrag.domene.sak.Saksnummer
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.domene.util.lastVisningsnavnFraFil
import java.math.BigDecimal
import java.math.MathContext
import java.time.LocalDate
import java.util.Collections.emptyList

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

private val sluttberegningAvslagResultater =
    listOf(
        SluttberegningBarnebidrag::ikkeOmsorgForBarnet.name,
        SluttberegningBarnebidrag::barnetErSelvforsørget.name,
    )

// Rekkefølge har ikke noe å si her. Dette er bare konvertering av resultatet til bisyskode.
private val sluttberegningBisyskodeMap =
    mapOf(
        SluttberegningBarnebidrag::ikkeOmsorgForBarnet.name to "AIO",
        SluttberegningBarnebidrag::barnetErSelvforsørget.name to "5SF",
        SluttberegningBarnebidrag::bidragJustertForDeltBosted.name to "8DN",
        SluttberegningBarnebidrag::bidragJustertForNettoBarnetilleggBP.name to "101",
        SluttberegningBarnebidrag::bidragJustertForNettoBarnetilleggBM.name to "102",
        SluttberegningBarnebidrag::bidragJustertNedTilEvne.name to "6MB",
        SluttberegningBarnebidrag::bidragJustertNedTil25ProsentAvInntekt.name to "7M",
        SluttberegningBarnebidrag::bidragJustertTilForskuddssats.name to "RFO",
        SluttberegningBarnebidrag::bidragJustertManueltTilForskuddssats.name to "RFO",
        "kostnadsberegnet" to "KBB",
    )

data class SluttberegningBarnebidrag(
    override val periode: ÅrMånedsperiode,
    val beregnetBeløp: BigDecimal?,
    val resultatBeløp: BigDecimal?,
    @JsonAlias("uminusNettoBarnetilleggBM")
    val uMinusNettoBarnetilleggBM: BigDecimal,
    val bruttoBidragEtterBarnetilleggBM: BigDecimal,
    val nettoBidragEtterBarnetilleggBM: BigDecimal,
    val bruttoBidragJustertForEvneOg25Prosent: BigDecimal,
    val bruttoBidragEtterBegrensetRevurdering: BigDecimal = BigDecimal.ZERO,
    val bruttoBidragEtterBarnetilleggBP: BigDecimal,
    val nettoBidragEtterSamværsfradrag: BigDecimal,
    val bpAndelAvUVedDeltBostedFaktor: BigDecimal,
    val bpAndelAvUVedDeltBostedBeløp: BigDecimal,
    val løpendeForskudd: BigDecimal? = null,
    val løpendeBidrag: BigDecimal? = null,
    val barnetErSelvforsørget: Boolean = false,
    val bidragJustertForDeltBosted: Boolean = false,
    val bidragJustertForNettoBarnetilleggBP: Boolean = false,
    val bidragJustertForNettoBarnetilleggBM: Boolean = false,
    val bidragJustertNedTilEvne: Boolean = false,
    val bidragJustertNedTil25ProsentAvInntekt: Boolean = false,
    val bidragJustertTilForskuddssats: Boolean = false,
    val bidragJustertManueltTilForskuddssats: Boolean = false,
    val begrensetRevurderingUtført: Boolean = false,
    val ikkeOmsorgForBarnet: Boolean = false,
    // Brukes bare ved overføring av bisys vedtak
    val tilleggsbidrag: BigDecimal? = null,
    // Brukes bare ved overføring av bisys vedtak
    val forsvaretsBarnetillegg: Boolean? = null,
    // Beregnet evne til BP etter FF
    val bpEvneVedForholdsmessigFordeling: BigDecimal? = null,
    // Andel av U basert på fordeling fra FF
    val bpAndelAvUVedForholdsmessigFordelingFaktor: BigDecimal? = null,
    val bpSumAndelAvU: BigDecimal? = null,
) : Sluttberegning {
    @get:JsonIgnore
    val resultat
        get() =
            // Rekkefølgen bestemmer hvilken som slår ut for sluttresultatet. Øverste har høyest prioritet.
            when {
                ikkeOmsorgForBarnet -> SluttberegningBarnebidrag::ikkeOmsorgForBarnet.name
                bidragJustertForNettoBarnetilleggBP -> SluttberegningBarnebidrag::bidragJustertForNettoBarnetilleggBP.name
                bidragJustertManueltTilForskuddssats -> SluttberegningBarnebidrag::bidragJustertManueltTilForskuddssats.name
                bidragJustertTilForskuddssats -> SluttberegningBarnebidrag::bidragJustertTilForskuddssats.name
                barnetErSelvforsørget -> SluttberegningBarnebidrag::barnetErSelvforsørget.name
                bidragJustertForDeltBosted && bidragJustertNedTilEvne -> SluttberegningBarnebidrag::bidragJustertNedTilEvne.name
                bidragJustertForDeltBosted && bidragJustertNedTil25ProsentAvInntekt ->
                    SluttberegningBarnebidrag::bidragJustertNedTil25ProsentAvInntekt.name

                bidragJustertForDeltBosted -> SluttberegningBarnebidrag::bidragJustertForDeltBosted.name
                bidragJustertNedTilEvne -> SluttberegningBarnebidrag::bidragJustertNedTilEvne.name
                bidragJustertNedTil25ProsentAvInntekt -> SluttberegningBarnebidrag::bidragJustertNedTil25ProsentAvInntekt.name
                bidragJustertForNettoBarnetilleggBM -> SluttberegningBarnebidrag::bidragJustertForNettoBarnetilleggBM.name
                else -> "kostnadsberegnet"
            }

    @get:JsonIgnore
    val erResultatAvslag get() = sluttberegningAvslagResultater.contains(resultat)

    @get:JsonIgnore
    val bisysResultatkode
        get() = sluttberegningBisyskodeMap[resultat] ?: "KBB"

    @get:JsonIgnore
    val resultatVisningsnavn get() = lastVisningsnavnFraFil("sluttberegningBarnebidrag.yaml")[resultat]
}

data class SluttberegningBarnebidragAldersjustering(
    override val periode: ÅrMånedsperiode,
    val beregnetBeløp: BigDecimal,
    val resultatBeløp: BigDecimal,
    val bpAndelBeløp: BigDecimal,
    val bpAndelFaktorVedDeltBosted: BigDecimal? = null,
    val deltBosted: Boolean = false,
) : Sluttberegning {
    @get:JsonIgnore
    val resultat
        get() =
            when {
                deltBosted -> SluttberegningBarnebidrag::bidragJustertForDeltBosted.name
                else -> "kostnadsberegnet"
            }

    @get:JsonIgnore
    val bisysResultatkode
        get() = sluttberegningBisyskodeMap[resultat] ?: "KBB"

    @get:JsonIgnore
    val resultatVisningsnavn get() = lastVisningsnavnFraFil("sluttberegningBarnebidrag.yaml")[resultat]
}

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
    val totalTilsynsutgift: BigDecimal,
    val erBegrensetAvMaksTilsyn: Boolean,
    val bruttoTilsynsutgift: BigDecimal,
    val justertBruttoTilsynsutgift: BigDecimal,
    val andelTilsynsutgiftFaktor: BigDecimal,
    val antallBarnBMUnderTolvÅr: Int,
    val antallBarnBMBeregnet: Int = antallBarnBMUnderTolvÅr,
    val antallBarnMedTilsynsutgifter: Int = antallBarnBMBeregnet,
    val skattefradrag: BigDecimal,
    val skattefradragPerBarn: BigDecimal,
    val skattefradragTotalTilsynsutgift: BigDecimal,
    val skattefradragMaksfradrag: BigDecimal,
    val nettoTilsynsutgift: BigDecimal,
    val tilsynsutgiftBarnListe: List<TilsynsutgiftBarn>,
) : Delberegning

data class TilsynsutgiftBarn(
    val gjelderBarn: Grunnlagsreferanse,
    val sumTilsynsutgifter: BigDecimal,
    val endeligSumTilsynsutgifter: BigDecimal,
)

fun List<GrunnlagInnhold>.filtrerDelberegninger() = filterIsInstance<Delberegning>()

data class DelberegningUnderholdskostnad(
    override val periode: ÅrMånedsperiode,
    val forbruksutgift: BigDecimal,
    val boutgift: BigDecimal,
    val barnetilsynMedStønad: BigDecimal?,
    val nettoTilsynsutgift: BigDecimal?,
    val barnetrygd: BigDecimal,
    val underholdskostnad: BigDecimal,
    val forpleining: BigDecimal? = null,
) : Delberegning

data class FaktiskUtgiftPeriode(
    override val periode: ÅrMånedsperiode,
    val fødselsdatoBarn: LocalDate,
    val faktiskUtgiftBeløp: BigDecimal,
    val kostpengerBeløp: BigDecimal,
    val kommentar: String? = null,
    override val manueltRegistrert: Boolean,
) : GrunnlagPeriodeInnhold

data class TilleggsstønadPeriode(
    override val periode: ÅrMånedsperiode,
    val beløpDagsats: BigDecimal,
    override val manueltRegistrert: Boolean,
) : GrunnlagPeriodeInnhold

data class DelberegningFaktiskTilsynsutgift(
    override val periode: ÅrMånedsperiode,
    val beregnetBeløp: BigDecimal,
) : Delberegning

data class DelberegningTilleggsstønad(
    override val periode: ÅrMånedsperiode,
    val beregnetBeløp: BigDecimal,
) : Delberegning

data class DelberegningEndringSjekkGrensePeriode(
    override val periode: ÅrMånedsperiode,
    val løpendeBidragBeløp: BigDecimal? = null,
    val løpendeBidragFraPrivatAvtale: Boolean = false,
    val beregnetBidragBeløp: BigDecimal? = null,
    val faktiskEndringFaktor: BigDecimal? = null,
    val endringErOverGrense: Boolean,
) : Delberegning

data class DelberegningEndringSjekkGrense(
    override val periode: ÅrMånedsperiode,
    val endringErOverGrense: Boolean,
) : Delberegning
