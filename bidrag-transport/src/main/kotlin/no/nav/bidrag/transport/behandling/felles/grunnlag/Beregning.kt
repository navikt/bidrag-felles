package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.person.AldersgruppeForskudd
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

data class SluttberegningForskudd(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
    val resultatKode: Resultatkode,
    val aldersgruppe: AldersgruppeForskudd,
) : Sluttberegning

data class SluttberegningSærtilskudd(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
    val resultatKode: Resultatkode,
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
    val antallBarn: Int,
) : Delberegning

data class DelberegningBidragsevne(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
) : Delberegning

data class DelberegningVoksneIHustand(
    override val periode: ÅrMånedsperiode,
    val borMedAndreVoksne: Boolean,
) : Delberegning

data class DelberegningBidragspliktigesAndelSærtilskudd(
    override val periode: ÅrMånedsperiode,
    val resultatAndelProsent: BigDecimal,
    val resultatAndelBeløp: BigDecimal,
    val barnetErSelvforsørget: Boolean,
) : Delberegning

data class DelberegningUtgift(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
) : Delberegning

data class DelberegningSamværsfrdragSærtilskudd(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
) : Delberegning

fun List<GrunnlagInnhold>.filtrerDelberegninger() = filterIsInstance<Delberegning>()
