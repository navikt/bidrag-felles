package no.nav.bidrag.domene.enums.behandling

import io.swagger.v3.oas.annotations.media.Schema

typealias BisysSøknadstype = Behandlingstype

// Enum verdier av kodetabellen t_kode_sokn_type
// Ved konvertering til Vedtakstype så blir noen detaljer borte (feks om det er begrenset revurdering eller revurdering).
// Dette er enum som er 1-1 med bisys sine søknadstyper. Brukes i blant annet bidrag-behandling
@Schema(enumAsRef = true)
enum class Behandlingstype(
    val bisysKode: String,
    val erKlage: Boolean,
) {
    ENDRING("EN", false),
    EGET_TILTAK("ET", false),
    SØKNAD("FA", false),
    INNKREVINGSGRUNNLAG("IG", false),
    ALDERSJUSTERING("AJ", false),
    INDEKSREGULERING("IR", false),
    KLAGE_BEGRENSET_SATS("KB", true),
    KLAGE("KL", true),
    FØLGER_KLAGE("KM", true),
    KORRIGERING("KR", false),
    KONVERTERING("KV", false),
    OPPHØR("OH", false),
    PRIVAT_AVTALE("PA", false),
    BEGRENSET_REVURDERING("RB", false),
    REVURDERING("RF", false),
    OPPJUSTERT_FORSKUDD("OF", false),
    OMGJØRING("OM", true),
    OMGJØRING_BEGRENSET_SATS("OB", true),
    PARAGRAF_35_C("35", true),
    PARAGRAF_35_C_BEGRENSET_SATS("3B", true),
    MÅNEDLIG_PÅLOP("MP", false),

    ;

    fun erBegrensetRevurdering() =
        listOf(
            PARAGRAF_35_C_BEGRENSET_SATS,
            OMGJØRING_BEGRENSET_SATS,
            KLAGE_BEGRENSET_SATS,
            REVURDERING,
            BEGRENSET_REVURDERING,
        ).contains(this)

    companion object {
        fun fraKode(kode: String): Behandlingstype? =
            try {
                enumValues<Behandlingstype>().find { res -> res.bisysKode == kode } ?: Behandlingstype.valueOf(kode)
            } catch (e: Exception) {
                null
            }
    }
}
