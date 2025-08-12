package no.nav.bidrag.transport.behandling.felles.grunnlag

import java.time.LocalDateTime

data class ResultatFraVedtakGrunnlag(
    val vedtaksid: Int?,
    val klagevedtak: Boolean = false,
    val beregnet: Boolean = false,
    // Om det skal opprettes paragraf 35c søknad for vedtakisden.
    // Brukes for etterfølgende vedtak når klagevedtak har beregningsperiode til opprinnelig vedtakstidspunkt
    val opprettParagraf35c: Boolean = false,
    val vedtakstidspunkt: LocalDateTime? = null,
) : GrunnlagInnhold

data class VedtakOrkestreringDetaljerGrunnlag(
    val klagevedtakId: Int,
) : GrunnlagInnhold
