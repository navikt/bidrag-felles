package no.nav.bidrag.domene.enums.barnetillegg

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class Barnetilleggstype {
    PENSJON,
    DAGPENGER,
    AAP,
    TILTAKSPENGER,
    FORSVARET,
    ANNET,
}
