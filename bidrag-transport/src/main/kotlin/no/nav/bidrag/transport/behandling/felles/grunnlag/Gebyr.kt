package no.nav.bidrag.transport.behandling.felles.grunnlag

import java.math.BigDecimal

data class ManueltOverstyrtGebyr(
    val ilagtGebyr: Boolean,
    val begrunnelse: String,
) : GrunnlagInnhold

data class DelberegningInnteksbasertGebyr(
    val ileggesGebyr: Boolean,
    val sumInntekt: BigDecimal,
) : GrunnlagInnhold

data class SluttberegningGebyr(
    val ilagtGebyr: Boolean,
) : GrunnlagInnhold
