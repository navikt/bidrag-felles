package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.barnetillegg.Barnetilleggstype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

@Schema(description = "Barnetillegg for person")
data class BarnetilleggPeriode(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
    val skattFaktor: BigDecimal,
    override val manueltRegistrert: Boolean,
) : GrunnlagPeriodeInnhold

data class DelberegningBarnetilleggSkattesats(
    override val periode: ÅrMånedsperiode,
    val skattFaktor: BigDecimal,
) : Delberegning

data class DelberegningNettoBarnetillegg(
    override val periode: ÅrMånedsperiode,
    val summertBruttoBarnetillegg: BigDecimal,
    val summertNettoBarnetillegg: BigDecimal,
    val barnetilleggTypeListe: List<BarnetilleggType>,
) : Delberegning

data class BarnetilleggType(
    val barnetilleggType: Barnetilleggstype,
    val bruttoBarnetillegg: BigDecimal,
    val nettoBarnetillegg: BigDecimal,
)
