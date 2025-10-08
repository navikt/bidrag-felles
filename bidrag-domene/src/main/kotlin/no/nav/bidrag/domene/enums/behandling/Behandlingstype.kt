package no.nav.bidrag.domene.enums.behandling

typealias BisysSøknadstype = Behandlingstype

// Enum verdier av kodetabellen t_kode_sokn_type
// Ved konvertering til Vedtakstype så blir noen detaljer borte (feks om det er begrenset revurdering eller revurdering).
// Dette er enum som er 1-1 med bisys sine søknadstyper. Brukes i blant annet bidrag-behandling
enum class Behandlingstype(
    val bisysKode: String,
) {
    ENDRING("EN"),
    EGET_TILTAK("ET"),
    SØKNAD("FA"),
    INNKREVINGSGRUNNLAG("IG"),
    ALDERSJUSTERING("AJ"),
    INDEKSREGULERING("IR"),
    KLAGE_BEGRENSET_SATS("KB"),
    KLAGE("KL"),
    FØLGER_KLAGE("KM"),
    KORRIGERING("KR"),
    KONVERTERING("KV"),
    OPPHØR("OH"),
    PRIVAT_AVTALE("PA"),
    BEGRENSET_REVURDERING("RB"),
    REVURDERING("RF"),
    OPPJUSTERT_FORSKUDD("OF"),
    OMGJØRING("OM"),
    OMGJØRING_BEGRENSET_SATS("OB"),
    PARAGRAF_35_C("35"),
    PARAGRAF_35_C_BEGRENSET_SATS("3B"),

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
