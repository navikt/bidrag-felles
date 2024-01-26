package no.nav.bidrag.transport.behandling.felles.grunnlag

data class NotatGrunnlag(
    val innhold: String,
    val erMedIVedtaksdokumentet: Boolean,
) : GrunnlagInnhold
