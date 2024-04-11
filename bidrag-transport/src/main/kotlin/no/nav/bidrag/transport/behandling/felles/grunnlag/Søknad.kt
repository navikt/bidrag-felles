package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.rolle.SøktAvType
import no.nav.bidrag.domene.enums.vedtak.VirkningstidspunktÅrsakstype
import java.time.LocalDate

data class SøknadGrunnlag(
    val mottattDato: LocalDate,
    val søktFraDato: LocalDate,
    val søktAv: SøktAvType,
) : GrunnlagInnhold

data class VirkningstidspunktGrunnlag(
    val virkningstidspunkt: LocalDate,
    val årsak: VirkningstidspunktÅrsakstype? = null,
    val avslag: Resultatkode? = null,
) : GrunnlagInnhold
