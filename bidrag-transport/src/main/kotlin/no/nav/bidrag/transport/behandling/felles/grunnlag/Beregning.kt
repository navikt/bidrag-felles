package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

data class Sluttberegning(
    override val periode: ÅrMånedsperiode,
    val beregnetBeløp: BigDecimal,
    @Schema(
        description = """
        Endelig beløp som enten er lik beregnet beløp eller er justert manuelt av saksbehandler. 
        Er alltid lik beregnet beløp for forskudd og særtilskudd.
        """,
    )
    val endeligBeløp: BigDecimal = beregnetBeløp,
    val resultatKode: String,
) : GrunnlagBeregningPeriode

data class DelberegningInntekt(
    override val periode: ÅrMånedsperiode,
    val summertBeløp: BigDecimal,
) : Delberegning

data class DelberegningBarnIHusstand(
    override val periode: ÅrMånedsperiode,
    val antallBarn: Int,
) : Delberegning

fun List<GrunnlagInnhold>.filtrerDelberegninger() = filterIsInstance<Delberegning>()
