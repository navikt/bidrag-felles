package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.bidrag.domene.enums.sjablon.SjablonTallNavn
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

data class SjablonGrunnlag(
    override val periode: ÅrMånedsperiode,
    @JsonIgnore
    override val manueltRegistrert: Boolean = false,
    val sjablon: SjablonTallNavn,
    val verdi: BigDecimal,
) : GrunnlagPeriodeInnhold

data class SjablonTrinnvisSkattesatsPeriode(
    override val periode: ÅrMånedsperiode,
    @JsonIgnore
    override val manueltRegistrert: Boolean = false,
    val trinnliste: List<SjablonTrinnvisSkattesats>,
) : GrunnlagPeriodeInnhold

data class SjablonTrinnvisSkattesats(
    val inntekstgrense: Int,
    val sats: BigDecimal,
)
