package no.nav.bidrag.domene.enums.grunnlag

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Typer grunnlag som kan innhentes av tjenesten bidrag-grunnlag.
 * Brukes for å spesifisere hvilke grunnlag man ønsker at skal innhentes for en grunnlagspakke.
 * Må ikke forveksles med GrunnlagType.
 */
@Schema(enumAsRef = true)
enum class GrunnlagRequestType {
    AINNTEKT,
    SKATTEGRUNNLAG,
    UTVIDET_BARNETRYGD_OG_SMÅBARNSTILLEGG,
    BARNETILLEGG,
    HUSSTANDSMEDLEMMER_OG_EGNE_BARN,
    SIVILSTAND,
    KONTANTSTØTTE,
    BARNETILSYN,
    ARBEIDSFORHOLD,

    @Deprecated("OVERGANGSSTONAD skal utgå som egen type. Må koordineres med Bisys")
    OVERGANGSSTONAD,
}
