package no.nav.bidrag.domene.enums.vedtak

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class BeregnTil {
    OPPRINNELIG_VEDTAKSTIDSPUNKT,
    INNEVÆRENDE_MÅNED,
    ETTERFØLGENDE_MANUELL_VEDTAK,
}
