package no.nav.bidrag.domene.enums.beregning

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true, name = "Resultatkode")
enum class Resultatkode(
    val legacyKode: String,
    vararg val type: ResultatkodeType,
) {
    BARNET_ER_SELVFORSØRGET("5SF", ResultatkodeType.BARNEBIDRAG, ResultatkodeType.SÆRBIDRAG, ResultatkodeType.AVSLAG),

    // Resultat av beregning av barnebidrag, angir at det må gjøres en forholdsmessig fordeling
    BEGRENSET_EVNE_FLERE_SAKER_UTFØR_FORHOLDSMESSIG_FORDELING("", ResultatkodeType.BARNEBIDRAG),

    // Beregnet bidrag er større enn forskuddsats, settes lik forskuddssats
    BEGRENSET_REVURDERING("", ResultatkodeType.BARNEBIDRAG),

    // Barnet har delt bosted og BPs andel av U er under 50%, bidrag skal ikke beregnes
    BIDRAG_IKKE_BEREGNET_DELT_BOSTED("", ResultatkodeType.BARNEBIDRAG),

    // Bidrag redusert pga ikke full evne
    BIDRAG_REDUSERT_AV_EVNE("", ResultatkodeType.BARNEBIDRAG),

    // Maks 25% av inntekt
    BIDRAG_REDUSERT_TIL_25_PROSENT_AV_INNTEKT("", ResultatkodeType.BARNEBIDRAG),

    // BarnetilleggBP er høyere enn beregnet bidrag
    BIDRAG_SATT_TIL_BARNETILLEGG_BP("", ResultatkodeType.BARNEBIDRAG),

    // Barnebidrag settes likt barnetillegg fra forsvaret
    BIDRAG_SATT_TIL_BARNETILLEGG_FORSVARET("", ResultatkodeType.BARNEBIDRAG),

    // Beregnet bidrag er lavere enn underholdskostnad minus barnetilleggBM
    BIDRAG_SATT_TIL_UNDERHOLDSKOSTNAD_MINUS_BARNETILLEGG_BM("", ResultatkodeType.BARNEBIDRAG),

    // Barnet bor like mye hos begge foreldre
    DELT_BOSTED("", ResultatkodeType.BARNEBIDRAG),

    // Beregning av forholdsmessig fordeling er utført og det er beregnet nytt bidragsbeløp
    FORHOLDSMESSIG_FORDELING_BIDRAGSBELØP_ENDRET("", ResultatkodeType.BARNEBIDRAG),

    // Beregning av forholdsmessig fordeling er utført og det er ingen endringer på bidragsbeløp
    FORHOLDSMESSIG_FORDELING_INGEN_ENDRING("", ResultatkodeType.BARNEBIDRAG),

    // BP har 0.- i bidragsevne, bidrag satt til 0.-
    INGEN_EVNE("", ResultatkodeType.BARNEBIDRAG),

    // Kostnadsberegnet bidrag
    KOSTNADSBEREGNET_BIDRAG("", ResultatkodeType.BARNEBIDRAG),

    REDUSERT_FORSKUDD_50_PROSENT("50", ResultatkodeType.FORSKUDD),
    ORDINÆRT_FORSKUDD_75_PROSENT("75", ResultatkodeType.FORSKUDD),
    FORHØYET_FORSKUDD_100_PROSENT("100", ResultatkodeType.FORSKUDD),
    FORHØYET_FORSKUDD_11_ÅR_125_PROSENT("125", ResultatkodeType.FORSKUDD),

    GODKJENT_BELØP_LAVERE_ENN_FORSKUDDSSATS("AMF", ResultatkodeType.SÆRBIDRAG, ResultatkodeType.AVSLAG),

    // Resultat av beregning av særbidrag
    @Deprecated("SÆRTILSKUDD er erstattet med SÆRBIDRAG", ReplaceWith("SÆRBIDRAG_INNVILGET"))
    SÆRTILSKUDD_INNVILGET("VS", ResultatkodeType.SÆRBIDRAG),
    SÆRBIDRAG_INNVILGET("VS", ResultatkodeType.SÆRBIDRAG),

    // Resultat av beregning av særbidrag
    @Deprecated("SÆRTILSKUDD er erstattet med SÆRBIDRAG", ReplaceWith("SÆRBIDRAG_IKKE_FULL_BIDRAGSEVNE"))
    SÆRTILSKUDD_IKKE_FULL_BIDRAGSEVNE("ABB", ResultatkodeType.SÆRBIDRAG, ResultatkodeType.AVSLAG),
    SÆRBIDRAG_IKKE_FULL_BIDRAGSEVNE("ABB", ResultatkodeType.SÆRBIDRAG, ResultatkodeType.AVSLAG),

    AVSLAG("A", ResultatkodeType.AVSLAG),
    AVSLAG2("AA", ResultatkodeType.AVSLAG),
    PÅ_GRUNN_AV_BARNEPENSJON("ABA", ResultatkodeType.AVSLAG),
    AVSLAG_OVER_18_ÅR("A18", ResultatkodeType.AVSLAG),
    AVSLAG_IKKE_REGISTRERT_PÅ_ADRESSE("AIO", ResultatkodeType.AVSLAG),
    AVSLAG_HØY_INNTEKT("AHI", ResultatkodeType.AVSLAG),

    BARNETS_EKTESKAP("OBE", ResultatkodeType.OPPHØR),
    BARNETS_INNTEKT("OBI", ResultatkodeType.OPPHØR, ResultatkodeType.SÆRBIDRAG, ResultatkodeType.BARNEBIDRAG),
    PÅ_GRUNN_AV_YTELSE_FRA_FOLKETRYGDEN("OFT", ResultatkodeType.OPPHØR),
    FULLT_UNDERHOLDT_AV_OFFENTLIG("OFU", ResultatkodeType.OPPHØR),
    IKKE_OMSORG("OIO", ResultatkodeType.OPPHØR),
    IKKE_OPPHOLD_I_RIKET("OIR", ResultatkodeType.OPPHØR),
    MANGLENDE_DOKUMENTASJON(
        "OMD",
        ResultatkodeType.OPPHØR,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.SÆRBIDRAG,
        ResultatkodeType.FORSKUDD,
    ),
    PÅ_GRUNN_AV_SAMMENFLYTTING("OSA", ResultatkodeType.OPPHØR),
    OPPHOLD_I_UTLANDET("OUT", ResultatkodeType.OPPHØR),
    UTENLANDSK_YTELSE("OUY", ResultatkodeType.OPPHØR),
    AVSLAG_PRIVAT_AVTALE_BIDRAG("OH", ResultatkodeType.OPPHØR),

    // I tilfeller når BP bor i Lugano-land og BM søker kun om forskudd, så har vi ikke hjemmel til å ta bidragssaken opp av eget tiltak etter forskotteringsloven.
    // Da må BM søke om fastsettelse av bidrag før vi innvilger forskuddet. Hvis hen ikke søker, så avslår vi kravet på grunn av manglende samarbeid (§3 i forskotteringsloven).
    @Deprecated("Bruk IKKE_INNKREVING_AV_BIDRAG istedenfor", ReplaceWith("IKKE_INNKREVING_AV_BIDRAG"))
    IKKE_SØKT_OM_INNKREVING_AV_BIDRAG("OH", ResultatkodeType.OPPHØR),
    IKKE_INNKREVING_AV_BIDRAG("OH", ResultatkodeType.OPPHØR),

    // Særbidrag avslag
    UTGIFTER_DEKKES_AV_BARNEBIDRAGET("ADB", ResultatkodeType.AVSLAG, ResultatkodeType.DIREKTE_AVSLAG, ResultatkodeType.SÆRBIDRAG),
    IKKE_NØDVENDIGE_UTGIFTER("AIN", ResultatkodeType.AVSLAG, ResultatkodeType.DIREKTE_AVSLAG, ResultatkodeType.SÆRBIDRAG),

    PRIVAT_AVTALE_OM_SÆRBIDRAG("VX", ResultatkodeType.AVSLAG, ResultatkodeType.DIREKTE_AVSLAG, ResultatkodeType.SÆRBIDRAG),
    ALLE_UTGIFTER_ER_FORELDET("AUF", ResultatkodeType.AVSLAG, ResultatkodeType.DIREKTE_AVSLAG, ResultatkodeType.SÆRBIDRAG),
    ;

    companion object {
        fun fraKode(kode: String): Resultatkode? =
            try {
                enumValues<Resultatkode>().find { it.legacyKode == kode } ?: Resultatkode.valueOf(kode)
            } catch (e: Exception) {
                null
            }

        fun Resultatkode.erType(type: ResultatkodeType): Boolean = Resultatkode.alleMedType(type).contains(this)

        fun Resultatkode.erDirekteAvslag(): Boolean = erType(ResultatkodeType.DIREKTE_AVSLAG)

        fun Resultatkode.erAvslagEllerOpphør(): Boolean =
            erType(ResultatkodeType.AVSLAG) ||
                erType(ResultatkodeType.OPPHØR) ||
                erType(ResultatkodeType.DIREKTE_AVSLAG)

        fun alleMedType(type: ResultatkodeType): List<Resultatkode> =
            try {
                enumValues<Resultatkode>().filter { it.type.contains(type) }
            } catch (e: Exception) {
                emptyList()
            }
    }

    enum class ResultatkodeType {
        AVSLAG,
        DIREKTE_AVSLAG,
        OPPHØR,
        FORSKUDD,
        BARNEBIDRAG,
        SÆRBIDRAG,
    }
}
