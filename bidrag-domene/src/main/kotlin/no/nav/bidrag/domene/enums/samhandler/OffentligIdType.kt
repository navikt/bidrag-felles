package no.nav.bidrag.domene.enums.samhandler

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true, name = "Offentlig ID type")
enum class OffentligIdType(
    val visningsnavn: String,
) {
    NORSK_ORGNR("Norsk orgnummer"),
    NORSK_FNR("Norsk personnummer"),
    UTENLANDSK_ORGNR("Utenlandsk orgnummer"),
    UTENLANDSK_FNR("Utenlandsk personnummer"),
    ;

    companion object {
        fun fraVisningsnavn(visningsnavn: String): OffentligIdType? = entries.firstOrNull { it.visningsnavn == visningsnavn }
    }
}
