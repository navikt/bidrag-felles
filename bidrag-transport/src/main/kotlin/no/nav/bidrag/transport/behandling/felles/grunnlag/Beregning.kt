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

fun List<GrunnlagInnhold>.filtrerDelberegninger() = filterIsInstance<Delberegning>()
