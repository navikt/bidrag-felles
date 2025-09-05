package no.nav.bidrag.domene.enums.vedtak

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class BeregnTil {
    OPPRINNELIG_VEDTAKSTIDSPUNKT,

    // Hvis det er opprettet klage på klage så vil OPPRINNELIG_VEDTAKSTIDSPUNKT og OMGJORT_VEDTAK_VEDTAKSTIDSPUNKT være ulik. Ellers er det samme
    OMGJORT_VEDTAK_VEDTAKSTIDSPUNKT,
    INNEVÆRENDE_MÅNED,
    ETTERFØLGENDE_MANUELL_VEDTAK,
}
