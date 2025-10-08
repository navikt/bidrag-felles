package no.nav.bidrag.domene.enums.behandling

// Enum verdier av tabellen t_kode_sokn_gr i bisys
// Dette er enum som er 1-1 med bisys sine søknad gruppe kode. Brukes i blant annet bidrag-behandling og ved opprettelse av oppgaver i bidrag-arbeidsflyt
enum class Behandlingstema(
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
        fun fraKode(kode: String): Behandlingstema? =
            try {
                enumValues<Behandlingstema>().find { res -> res.bisysKode == kode } ?: Behandlingstema.valueOf(kode)
            } catch (e: Exception) {
                null
            }
    }
}
