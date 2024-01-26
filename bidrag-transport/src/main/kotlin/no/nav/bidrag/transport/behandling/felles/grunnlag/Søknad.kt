package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.enums.rolle.SøktAvType
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal
import java.time.LocalDate

data class SøknadGrunnlag(
    val mottattDato: LocalDate,
    val søktFraDato: LocalDate,
    val søktAv: SøktAvType,
) : GrunnlagInnhold

data class VirkningsdatoGrunnlag(
    val virkningsdato: LocalDate,
    val årsak: String,
) : GrunnlagInnhold

data class SluttberegningBBM(
    val periode: ÅrMånedsperiode,
    val beregnetBidragBeløp: BigDecimal,
    val endeligBidragBeløp: BigDecimal,
    val resultatKode: String,
) : GrunnlagInnhold
