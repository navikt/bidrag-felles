package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

@Schema(description = "Barnetillegg for person")
data class BarnetilleggPeriode(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
    val skattFaktor: BigDecimal,
    override val manueltRegistrert: Boolean,
) : GrunnlagPeriodeInnhold
