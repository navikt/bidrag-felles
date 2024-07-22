package no.nav.bidrag.transport.behandling.felles.grunnlag

data class NotatGrunnlag(
    val innhold: String,
    val erMedIVedtaksdokumentet: Boolean,
    val type: NotatType,
) : GrunnlagInnhold {
    enum class NotatType {
        VIRKNINGSTIDSPUNKT,
        BOFORHOLD,
        INNTEKT,
        UTGIFTER,
    }
}
