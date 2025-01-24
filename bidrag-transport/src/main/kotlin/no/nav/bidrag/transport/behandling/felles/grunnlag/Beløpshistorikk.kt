package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.tid.Datoperiode
import java.math.BigDecimal

data class BeløpshistorikkGrunnlag(
    val beløpshistorikk: List<BeløpshistorikkPeriode>,
) : GrunnlagInnhold

data class BeløpshistorikkPeriode(
    val periode: Datoperiode,
    val beløp: BigDecimal,
)
