package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "Privat avtale i bidragssaken")
data class PrivatAvtaleGrunnlag(
    val avtaleInngåttDato: LocalDate,
    val skalIndeksreguleres: Boolean,
) : GrunnlagInnhold

data class PrivatAvtalePeriodeGrunnlag(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
    override val manueltRegistrert: Boolean = true,
) : GrunnlagPeriodeInnhold

data class DelberegningPrivatAvtalePeriode(
    override val periode: ÅrMånedsperiode,
    val indeksreguleringFaktor: BigDecimal? = null,
    val beløp: BigDecimal,
) : Delberegning
