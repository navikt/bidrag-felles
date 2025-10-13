package no.nav.bidrag.domene.enums.behandling

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.vedtak.Engangsbeløptype
import no.nav.bidrag.domene.enums.vedtak.Stønadstype

// Enum verdier av tabellen t_kode_sokn_gr i bisys
// Dette er enum som er 1-1 med bisys sine søknad gruppe kode. Brukes i blant annet bidrag-behandling og ved opprettelse av oppgaver i bidrag-arbeidsflyt
@Schema(enumAsRef = true)
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

fun tilBeskrivelseBehandlingstema(
    stønadstype: Stønadstype? = null,
    engangsbeløptype: Engangsbeløptype? = null,
    behandlingstema: Behandlingstema? = null,
): String? =
    when (stønadstype) {
        Stønadstype.FORSKUDD -> "Bidragsforskudd"
        Stønadstype.BIDRAG -> "Barnebidrag"
        Stønadstype.BIDRAG18AAR -> "Barnebidrag 18 år"
        Stønadstype.EKTEFELLEBIDRAG -> "Ektefellebidrag"
        Stønadstype.OPPFOSTRINGSBIDRAG -> "Oppfostringbidrag"
        Stønadstype.MOTREGNING -> "Motregning"
        else ->
            when (engangsbeløptype) {
                Engangsbeløptype.SAERTILSKUDD, Engangsbeløptype.SÆRTILSKUDD, Engangsbeløptype.SÆRBIDRAG -> "Særbidrag"
                Engangsbeløptype.DIREKTE_OPPGJOR, Engangsbeløptype.DIREKTE_OPPGJØR -> "Direkte oppgjør"
                Engangsbeløptype.ETTERGIVELSE -> "Ettergivelse"
                Engangsbeløptype.ETTERGIVELSE_TILBAKEKREVING -> "Ettergivelse tilbakekreving"
                Engangsbeløptype.GEBYR_MOTTAKER, Engangsbeløptype.GEBYR_SKYLDNER -> "Gebyr"
                Engangsbeløptype.TILBAKEKREVING -> "Tilbakekreving"
                else ->
                    behandlingstema
                        ?.name
                        ?.lowercase()
                        ?.replace("_", " ")
                        ?.replaceFirstChar { it.uppercase() }
            }
    }
