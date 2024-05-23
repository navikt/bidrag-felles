@file:Suppress("unused")

package no.nav.bidrag.domene.enums.vedtak

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true, name = "Årsakstype")
enum class VirkningstidspunktÅrsakstype(val legacyKode: String) {
    ANNET("SF"),
    ENDRING_3_MÅNEDER_TILBAKE("NF"),
    ENDRING_3_ÅRS_REGELEN("OF"),
    FRA_BARNETS_FØDSEL("AF"),
    FRA_BARNETS_FLYTTEMÅNED("CF"),
    FRA_KRAVFREMSETTELSE("DF"),
    FRA_MÅNED_ETTER_INNTEKTEN_ØKTE("LF"),
    FRA_OPPHOLDSTILLATELSE("GF"),
    FRA_SØKNADSTIDSPUNKT("HF"),
    FRA_SAMLIVSBRUDD("BF"),
    FRA_SAMME_MÅNED_SOM_INNTEKTEN_BLE_REDUSERT("KF"),
    PRIVAT_AVTALE("PA"),
    REVURDERING_MÅNEDEN_ETTER("QF"),
    SØKNADSTIDSPUNKT_ENDRING("MF"),
    TIDLIGERE_FEILAKTIG_AVSLAG("PF"),
    TRE_MÅNEDER_TILBAKE("EF"),
    TRE_ÅRS_REGELEN("FF"),
    FRA_MÅNEDEN_ETTER_I_PÅVENTE_AV_BIDRAGSSAK("LF"),
    ;

    companion object {
        fun fraLegacyKode(legacyKode: String): VirkningstidspunktÅrsakstype? {
            return try {
                enumValues<VirkningstidspunktÅrsakstype>().find { it.legacyKode == legacyKode } ?: VirkningstidspunktÅrsakstype.valueOf(
                    legacyKode,
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}
