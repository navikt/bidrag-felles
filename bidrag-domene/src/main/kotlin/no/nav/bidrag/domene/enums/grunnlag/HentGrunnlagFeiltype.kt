package no.nav.bidrag.domene.enums.grunnlag

enum class HentGrunnlagFeiltype {
    TJENESTE_UTILGJENGELIG, // 503
    FEIL_I_TJENESTE, // 5xx
    FEIL_I_REQUEST, // 4xx
    PERSON_ID_UGYLDIG,
    UKJENT_FEIL,
}
