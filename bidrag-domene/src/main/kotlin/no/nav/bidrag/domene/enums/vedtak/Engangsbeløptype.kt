package no.nav.bidrag.domene.enums.vedtak

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class Engangsbeløptype {
    @Deprecated("DIREKTE_OPPGJOR skal ikke brukes", ReplaceWith("DIREKTE_OPPGJØR"))
    DIREKTE_OPPGJOR,
    DIREKTE_OPPGJØR,
    ETTERGIVELSE,
    ETTERGIVELSE_TILBAKEKREVING,
    GEBYR_MOTTAKER,
    GEBYR_SKYLDNER,
    INNKREVING_GJELD,

    @Deprecated("SAERTILSKUDD skal ikke brukes", ReplaceWith("SÆRTILSKUDD"))
    SAERTILSKUDD,
    SÆRTILSKUDD,
    TILBAKEKREVING,
}
