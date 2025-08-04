package no.nav.bidrag.transport.behandling.felles.grunnlag

data class ResultatFraVedtakGrunnlag(
    val vedtaksid: Int?,
    val klagevedtak: Boolean = false,
    val gjenopprettetBeløpshistorikk: Boolean = false,
) : GrunnlagInnhold
