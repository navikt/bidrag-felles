package no.nav.bidrag.transport.behandling.felles.grunnlag

data class NotatGrunnlag(
    val innhold: String,
    val erMedIVedtaksdokumentet: Boolean,
    val type: NotatType,
    val fraOpprinneligVedtak: Boolean = false,
) : GrunnlagInnhold {
    enum class NotatType {
        VIRKNINGSTIDSPUNKT,
        VIRKNINGSTIDSPUNKT_VURDERING_AV_SKOLEGANG,
        BOFORHOLD,
        INNTEKT,
        UTGIFTER,
        SAMVÆR,
        UNDERHOLDSKOSTNAD,
        PRIVAT_AVTALE,
    }
}
