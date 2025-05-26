package no.nav.bidrag.domene.enums.samhandler

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true, name = "Offentlig ID type")
enum class OffentligIdType {
    NORSK_ORGNR,
    NORSK_FNR,
    UTENLANDSK_ORGNR,
    UTENLANDSK_FNR,
}
