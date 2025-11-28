package no.nav.bidrag.domene.enums.behandling

import io.swagger.v3.oas.annotations.media.Schema

// Enum verdier av tabellen T_KODE_SOKN_GR_KOM i bisys
@Schema(enumAsRef = true)
enum class SøknadGruppeKombinasjon(
    val kode: String,
    val beskrivelse: String,
    val behandlingstema: Behandlingstema,
    val innkreving: Int,
) {
    AVSKRIVNING_DIREKTE_BETALT("AV", "Avskrivning direkte betalt", Behandlingstema.AVSKRIVNING, 0),
    BIDRAG("B", "Bidrag", Behandlingstema.BIDRAG, 0),
    BIDRAG_TILLEGGSBIDRAG_INNKREVING("BG", "Bidrag,tilleggsb,innkreving", Behandlingstema.BIDRAG_PLUSS_TILLEGGSBIDRAG, 1),
    BIDRAG_INNKREVING("BI", "Bidrag,innkreving", Behandlingstema.BIDRAG, 1),
    BIDRAG_TILLEGGSBIDRAG("BT", "Bidrag,tilleggsb", Behandlingstema.BIDRAG_PLUSS_TILLEGGSBIDRAG, 0),
    DIREKTE_OPPGJØR("DO", "Direkte oppgjør", Behandlingstema.DIREKTE_OPPGJØR, 0),
    EKTEFELLEBIDRAG_UTEN_INNKREVING("EB", "Ektefellebidrag u/innkr", Behandlingstema.EKTEFELLEBIDRAG, 0),
    ETTERGIVELSE("EG", "Ettergivelse", Behandlingstema.ETTERGIVELSE, 0),
    EKTEFELLEBIDRAG_MED_INNKREVING("EI", "Ektefellebidrag m/innkr", Behandlingstema.EKTEFELLEBIDRAG, 1),
    ERSTATNING("ER", "Erstatning", Behandlingstema.ERSTATNING, 0),
    FARSKAP("FA", "Farskap", Behandlingstema.FARSSKAP, 0),
    KUNNSKAP_OM_BIOLOGISK_FAR("FB", "Kunnskap om biologisk far", Behandlingstema.KUNNSKAP_OM_BIOLOGISK_FAR, 0),
    FORSKUDD("FO", "Forskudd", Behandlingstema.FORSKUDD, 0),
    GEBYR("GB", "Gebyr", Behandlingstema.GEBYR, 0),
    BIDRAG_18_ÅR_TILLEGGSBIDRAG("IG", "18 år, tilleggsb.", Behandlingstema.BIDRAG_18_ÅR_PLUSS_TILLEGGSBIDRAG, 0),
    BIDRAG_18_ÅR_INNKREVING("II", "18 år, innkreving", Behandlingstema.BIDRAG_18_ÅR, 1),
    INNKREVING("IK", "Innkreving", Behandlingstema.INNKREVING, 1),
    SÆRBIDRAG_INNKREVING("IS", "Særtilskudd,innkreving", Behandlingstema.SÆRBIDRAG, 1),
    BIDRAG_18_AAR_TILLEGGSBIDRAG_INNKREVING("IT", "18 år, tilleggsb,innkreving", Behandlingstema.BIDRAG_18_ÅR_PLUSS_TILLEGGSBIDRAG, 1),
    MORSKAP("MO", "Morskap", Behandlingstema.MORSSKAP, 0),
    MOTREGNING("MR", "Motregning", Behandlingstema.MOTREGNING, 0),
    OPPFOSTRINGSBIDRAG_INNKREVING("OI", "Oppfostringsbidrag,innkreving", Behandlingstema.OPPFOSTRINGSBIDRAG, 1),
    REFUSJON_BIDRAG("RB", "Refusjon bidrag", Behandlingstema.REFUSJON_BIDRAG, 0),
    SAKSOMKOSTNINGER("SO", "Saksomkostninger", Behandlingstema.SAKSOMKOSTNINGER, 0),
    SÆRBIDRAG("ST", "Særtilskudd", Behandlingstema.SÆRBIDRAG, 0),
    TILLEGGSBIDRAG("TB", "Tilleggsbidrag", Behandlingstema.TILLEGGSBIDRAG, 0),
    TILBAKEKREVING_ETTERGIVELSE("TE", "Tilbakekr,ettergivelse", Behandlingstema.TILBAKEKREVING_ETTERGIVELSE, 0),
    TILLEGGSBIDRAG_INNKREVING("TI", "Tilleggsbidrag,innkreving", Behandlingstema.TILLEGGSBIDRAG, 1),
    TILBAKEKREVING("TK", "Tilbakekreving", Behandlingstema.TILBAKEKREVING, 0),
    BIDRAG_18_ÅR("18", "18 år", Behandlingstema.BIDRAG_18_ÅR, 0),
    REISEKOSTNADER("RK", "Reisekostnader", Behandlingstema.REISEKOSTNADER, 0),
    ;

    companion object {
        fun fraKode(kode: String): SøknadGruppeKombinasjon? =
            try {
                enumValues<SøknadGruppeKombinasjon>().find { it.kode == kode }
            } catch (e: Exception) {
                null
            }

        fun fraBehandlingstemaOgInnkreving(
            behandlingstema: Behandlingstema,
            medInnkreving: Boolean,
        ): SøknadGruppeKombinasjon? =
            enumValues<SøknadGruppeKombinasjon>().find {
                it.behandlingstema == behandlingstema && it.innkreving == if (medInnkreving) 1 else 0
            }
    }
}
