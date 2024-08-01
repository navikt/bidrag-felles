package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.rolle.SøktAvType
import no.nav.bidrag.domene.enums.vedtak.VirkningstidspunktÅrsakstype
import java.time.LocalDate

data class SøknadGrunnlag(
    val mottattDato: LocalDate,
    val søktFraDato: LocalDate,
    val søktAv: SøktAvType,
    @Schema(description = "Mottatt dato på originale søknad. Er relevant hvis det klages på vedtaket")
    val opprinneligMottattDato: LocalDate? = null,
) : GrunnlagInnhold

data class VirkningstidspunktGrunnlag(
    val virkningstidspunkt: LocalDate,
    val årsak: VirkningstidspunktÅrsakstype? = null,
    val avslag: Resultatkode? = null,
) : GrunnlagInnhold
