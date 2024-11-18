package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.enums.barnetillegg.Barnetilleggstype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

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
