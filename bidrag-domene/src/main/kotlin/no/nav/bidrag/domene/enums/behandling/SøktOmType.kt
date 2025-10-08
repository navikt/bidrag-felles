package no.nav.bidrag.domene.enums.behandling

// Ved konvertering til Stønadstype så blir noen detaljer borte.
// Dette er enum som er 1-1 med bisys sine søknad gruppe kode. Brukes i blant annet bidrag-behandling og ved opprettelse av oppgaver
enum class SøktOmType(
    val bisysKode: String,
) {
    AVSKRIVNING("AV"),
    BIDRAG("BI"),
    BIDRAG_PLUSS_TILLEGGSBIDRAG("BT"),
    DIREKTE_OPPGJØR("DO"),
    EKTEFELLEBIDRAG("EB"),
    ETTERGIVELSE("EG"),
    ERSTATNING("ER"),
    FARSSKAP("FA"),
    KUNNSKAP_OM_BIOLOGISK_FAR("FB"),
    FORSKUDD("FO"),
    GEBYR("GB"),
    INNKREVING("IK"),
    MORSSKAP("MO"),
    MOTREGNING("MR"),
    OPPFOSTRINGSBIDRAG("OB"),
    REFUSJON_BIDRAG("RB"),
    SAKSOMKOSTNINGER("SO"),
    SÆRBIDRAG("ST"),
    TILLEGGSBIDRAG("TB"),
    TILBAKEKREVING_ETTERGIVELSE("TE"),
    TILBAKEKREVING("TK"),
    BIDRAG_18_ÅR_PLUSS_TILLEGGSBIDRAG("T1"),
    BIDRAG_18_ÅR("18"),
    REISEKOSTNADER("RK"),

    ;

    companion object {
        fun fraKode(kode: String): SøktOmType? =
            try {
                enumValues<SøktOmType>().find { res -> res.bisysKode == kode } ?: SøktOmType.valueOf(kode)
            } catch (e: Exception) {
                null
            }
    }
}
