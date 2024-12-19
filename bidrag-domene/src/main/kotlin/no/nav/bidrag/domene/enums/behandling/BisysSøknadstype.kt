package no.nav.bidrag.domene.enums.behandling

// Ved konvertering til Vedtakstype så blir noen detaljer borte (feks om det er begrenset revurdering eller revurdering).
// Dette er enum som er 1-1 med bisys sine søknadstyper. Brukes i blant annet bidrag-behandling
enum class BisysSøknadstype(
    val bisysKode: String,
) {
    ENDRING("EN"),
    EGET_TILTAK("ET"),
    SØKNAD("FA"),
    INNKREVINGSGRUNNLAG("IG"),
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

    ;

    companion object {
        fun fraKode(kode: String): BisysSøknadstype? =
            try {
                enumValues<BisysSøknadstype>().find { res -> res.bisysKode == kode } ?: BisysSøknadstype.valueOf(kode)
            } catch (e: Exception) {
                null
            }
    }
}
