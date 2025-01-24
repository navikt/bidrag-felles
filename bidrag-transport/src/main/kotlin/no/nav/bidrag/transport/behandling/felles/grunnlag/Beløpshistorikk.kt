package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

data class BeløpshistorikkGrunnlag(
    val beløpshistorikk: List<BeløpshistorikkPeriode>,
) : GrunnlagInnhold

data class BeløpshistorikkPeriode(
    val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
)
