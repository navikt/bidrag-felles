package no.nav.bidrag.domene.enums.beregning

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.util.fjernAvslagOpphørPrefiks
import no.nav.bidrag.domene.util.visningsnavn
import no.nav.bidrag.domene.util.visningsnavnIntern

@Schema(enumAsRef = true, name = "Resultatkode")
enum class Resultatkode(
    val bisysKode: List<BisysResultatkode>,
    vararg val type: ResultatkodeType,
) {
    BARNET_ER_SELVFORSØRGET(
        listOf(BisysResultatkode("5SF", BisysResultatkodeType.AVSLAG)),
        ResultatkodeType.BARNEBIDRAG,
        ResultatkodeType.SÆRBIDRAG,
        ResultatkodeType.AVSLAG,
    ),

    // Resultat av beregning av barnebidrag, angir at det må gjøres en forholdsmessig fordeling
    BEGRENSET_EVNE_FLERE_SAKER_UTFØR_FORHOLDSMESSIG_FORDELING(
        listOf(BisysResultatkode("")),
        ResultatkodeType.BARNEBIDRAG,
    ),

    // Beregnet bidrag er større enn forskuddsats, settes lik forskuddssats
    BEGRENSET_REVURDERING(listOf(BisysResultatkode("")), ResultatkodeType.BARNEBIDRAG),

    // Barnet har delt bosted og BPs andel av U er under 50%, bidrag skal ikke beregnes
    BIDRAG_IKKE_BEREGNET_DELT_BOSTED(listOf(BisysResultatkode("")), ResultatkodeType.BARNEBIDRAG),

    // Bidrag redusert pga ikke full evne
    BIDRAG_REDUSERT_AV_EVNE(listOf(BisysResultatkode("")), ResultatkodeType.BARNEBIDRAG),

    // Maks 25% av inntekt
    BIDRAG_REDUSERT_TIL_25_PROSENT_AV_INNTEKT(listOf(BisysResultatkode("")), ResultatkodeType.BARNEBIDRAG),

    // BarnetilleggBP er høyere enn beregnet bidrag
    BIDRAG_SATT_TIL_BARNETILLEGG_BP(listOf(BisysResultatkode("")), ResultatkodeType.BARNEBIDRAG),

    // Barnebidrag settes likt barnetillegg fra forsvaret
    BIDRAG_SATT_TIL_BARNETILLEGG_FORSVARET(listOf(BisysResultatkode("")), ResultatkodeType.BARNEBIDRAG),

    // Beregnet bidrag er lavere enn underholdskostnad minus barnetilleggBM
    BIDRAG_SATT_TIL_UNDERHOLDSKOSTNAD_MINUS_BARNETILLEGG_BM(
        listOf(BisysResultatkode("")),
        ResultatkodeType.BARNEBIDRAG,
    ),

    // Barnet bor like mye hos begge foreldre
    DELT_BOSTED(
        listOf(BisysResultatkode("")),
        ResultatkodeType.BARNEBIDRAG,
    ),

    // Beregning av forholdsmessig fordeling er utført og det er beregnet nytt bidragsbeløp
    FORHOLDSMESSIG_FORDELING_BIDRAGSBELØP_ENDRET(
        listOf(BisysResultatkode("")),
        ResultatkodeType.BARNEBIDRAG,
    ),

    // Beregning av forholdsmessig fordeling er utført og det er ingen endringer på bidragsbeløp
    FORHOLDSMESSIG_FORDELING_INGEN_ENDRING(listOf(BisysResultatkode("")), ResultatkodeType.BARNEBIDRAG),

    // BP har 0.- i bidragsevne, bidrag satt til 0.-
    INGEN_EVNE(listOf(BisysResultatkode("")), ResultatkodeType.BARNEBIDRAG),

    // Kostnadsberegnet bidrag
    KOSTNADSBEREGNET_BIDRAG(listOf(BisysResultatkode("")), ResultatkodeType.BARNEBIDRAG),

    REDUSERT_FORSKUDD_50_PROSENT(listOf(BisysResultatkode("50")), ResultatkodeType.FORSKUDD),
    ORDINÆRT_FORSKUDD_75_PROSENT(listOf(BisysResultatkode("75")), ResultatkodeType.FORSKUDD),
    FORHØYET_FORSKUDD_100_PROSENT(listOf(BisysResultatkode("100")), ResultatkodeType.FORSKUDD),
    FORHØYET_FORSKUDD_11_ÅR_125_PROSENT(listOf(BisysResultatkode("125")), ResultatkodeType.FORSKUDD),

    // Resultat av beregning av særbidrag
    @Deprecated("SÆRTILSKUDD er erstattet med SÆRBIDRAG", ReplaceWith("SÆRBIDRAG_INNVILGET"))
    SÆRTILSKUDD_INNVILGET(listOf(BisysResultatkode("VS")), ResultatkodeType.SÆRBIDRAG),
    SÆRBIDRAG_INNVILGET(listOf(BisysResultatkode("VS")), ResultatkodeType.SÆRBIDRAG),

    // Resultat av beregning av særbidrag
    @Deprecated("SÆRTILSKUDD er erstattet med SÆRBIDRAG", ReplaceWith("SÆRBIDRAG_IKKE_FULL_BIDRAGSEVNE"))
    SÆRTILSKUDD_IKKE_FULL_BIDRAGSEVNE(
        listOf(BisysResultatkode("ABS", BisysResultatkodeType.AVSLAG)),
        ResultatkodeType.SÆRBIDRAG,
        ResultatkodeType.AVSLAG,
    ),
    SÆRBIDRAG_IKKE_FULL_BIDRAGSEVNE(
        listOf(BisysResultatkode("ABS", BisysResultatkodeType.AVSLAG)),
        ResultatkodeType.SÆRBIDRAG,
        ResultatkodeType.AVSLAG,
    ),
    SÆRBIDRAG_MANGLER_BIDRAGSEVNE(
        listOf(BisysResultatkode("ABB", BisysResultatkodeType.AVSLAG)),
        ResultatkodeType.SÆRBIDRAG,
        ResultatkodeType.AVSLAG,
    ),

    AVSLAG(listOf(BisysResultatkode("A", BisysResultatkodeType.AVSLAG)), ResultatkodeType.AVSLAG),
    AVSLAG2(listOf(BisysResultatkode("AA", BisysResultatkodeType.AVSLAG)), ResultatkodeType.AVSLAG),

    AVSLAG_OVER_18_ÅR(
        listOf(BisysResultatkode("A18", BisysResultatkodeType.AVSLAG), BisysResultatkode("OH2", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),
    AVSLAG_IKKE_REGISTRERT_PÅ_ADRESSE(
        listOf(BisysResultatkode("ARA", BisysResultatkodeType.AVSLAG), BisysResultatkode("ORA", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
    ),

    AVSLAG_HØY_INNTEKT(
        listOf(BisysResultatkode("AHI", BisysResultatkodeType.AVSLAG), BisysResultatkode("OHI", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),

    PÅ_GRUNN_AV_BARNEPENSJON(
        listOf(BisysResultatkode("ABA", BisysResultatkodeType.AVSLAG), BisysResultatkode("OBA", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),
    IKKE_OMSORG(
        listOf(BisysResultatkode("AIO", BisysResultatkodeType.AVSLAG), BisysResultatkode("OIO", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),
    BARNETS_EKTESKAP(
        listOf(BisysResultatkode("ABE", BisysResultatkodeType.AVSLAG), BisysResultatkode("OBE", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),
    BARNETS_INNTEKT(
        listOf(BisysResultatkode("ABI", BisysResultatkodeType.AVSLAG), BisysResultatkode("OBI", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),
    PÅ_GRUNN_AV_YTELSE_FRA_FOLKETRYGDEN(
        listOf(BisysResultatkode("AFT", BisysResultatkodeType.AVSLAG), BisysResultatkode("OFT", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),
    FULLT_UNDERHOLDT_AV_OFFENTLIG(
        listOf(BisysResultatkode("AFU", BisysResultatkodeType.AVSLAG), BisysResultatkode("OFU", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),

    IKKE_OPPHOLD_I_RIKET(
        listOf(BisysResultatkode("AIR", BisysResultatkodeType.AVSLAG), BisysResultatkode("OIR", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),
    MANGLENDE_DOKUMENTASJON(
        listOf(BisysResultatkode("AMD", BisysResultatkodeType.AVSLAG), BisysResultatkode("OMD", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.SÆRBIDRAG,
        ResultatkodeType.FORSKUDD,
    ),
    PÅ_GRUNN_AV_SAMMENFLYTTING(
        listOf(BisysResultatkode("ASA", BisysResultatkodeType.AVSLAG), BisysResultatkode("OSA", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),
    OPPHOLD_I_UTLANDET(
        listOf(BisysResultatkode("AUT", BisysResultatkodeType.AVSLAG), BisysResultatkode("OUT", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),
    UTENLANDSK_YTELSE(
        listOf(BisysResultatkode("AUY", BisysResultatkodeType.AVSLAG), BisysResultatkode("OUY", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),
    AVSLAG_PRIVAT_AVTALE_BIDRAG(
        listOf(BisysResultatkode("APA", BisysResultatkodeType.AVSLAG), BisysResultatkode("OPA", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),

    // I tilfeller når BP bor i Lugano-land og BM søker kun om forskudd, så har vi ikke hjemmel til å ta bidragssaken opp av eget tiltak etter forskotteringsloven.
    // Da må BM søke om fastsettelse av bidrag før vi innvilger forskuddet. Hvis hen ikke søker, så avslår vi kravet på grunn av manglende samarbeid (§3 i forskotteringsloven).
    @Deprecated("Bruk IKKE_INNKREVING_AV_BIDRAG istedenfor", ReplaceWith("IKKE_INNKREVING_AV_BIDRAG"))
    IKKE_SØKT_OM_INNKREVING_AV_BIDRAG(
        listOf(BisysResultatkode("AIB", BisysResultatkodeType.AVSLAG), BisysResultatkode("OIB", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),
    IKKE_INNKREVING_AV_BIDRAG(
        listOf(BisysResultatkode("AIB", BisysResultatkodeType.AVSLAG), BisysResultatkode("OIB", BisysResultatkodeType.OPPHØR)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.FORSKUDD,
    ),

    // Særbidrag avslag
    UTGIFTER_DEKKES_AV_BARNEBIDRAGET(
        listOf(BisysResultatkode("ADB", BisysResultatkodeType.AVSLAG)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.SÆRBIDRAG,
    ),
    IKKE_NØDVENDIGE_UTGIFTER(
        listOf(BisysResultatkode("AIN", BisysResultatkodeType.AVSLAG)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.SÆRBIDRAG,
    ),
    PRIVAT_AVTALE(
        listOf(BisysResultatkode("VX")),
        ResultatkodeType.BARNEBIDRAG,
    ),
    AVSLAG_PRIVAT_AVTALE_OM_SÆRBIDRAG(
        listOf(BisysResultatkode("APS", BisysResultatkodeType.AVSLAG)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.DIREKTE_AVSLAG,
        ResultatkodeType.SÆRBIDRAG,
    ),
    ALLE_UTGIFTER_ER_FORELDET(
        listOf(BisysResultatkode("AUF", BisysResultatkodeType.AVSLAG)),
        ResultatkodeType.AVSLAG,
        ResultatkodeType.SÆRBIDRAG,
    ),
    GODKJENT_BELØP_ER_LAVERE_ENN_FORSKUDDSSATS(
        listOf(BisysResultatkode("AMF")),
        ResultatkodeType.SÆRBIDRAG,
        ResultatkodeType.AVSLAG,
    ),

    ;

    val legacyKode get() = bisysKode.firstOrNull()?.resultatKode

    companion object {
        fun fraKode(kode: String): Resultatkode? =
            try {
                enumValues<Resultatkode>().find { res -> res.bisysKode.any { it.resultatKode == kode } } ?: Resultatkode.valueOf(kode)
            } catch (e: Exception) {
                null
            }

        fun Resultatkode.tilBisysResultatkode(vedtakstype: Vedtakstype? = null) =
            if (vedtakstype == Vedtakstype.OPPHØR) {
                bisysKode.find { it.type == BisysResultatkodeType.OPPHØR }?.resultatKode ?: bisysKode.firstOrNull()?.resultatKode
            } else {
                bisysKode.firstOrNull()?.resultatKode
            }

        fun fraVisningsnavn(
            visningsnavn: String,
            vedtakstype: Vedtakstype? = null,
        ): Resultatkode? =
            try {
                enumValues<Resultatkode>().find {
                    it.visningsnavnIntern(vedtakstype).equals(visningsnavn, ignoreCase = true) ||
                        it.visningsnavn.intern.equals(visningsnavn, ignoreCase = true) ||
                        it.visningsnavn.intern.equals(visningsnavn.fjernAvslagOpphørPrefiks(), ignoreCase = true)
                }
            } catch (e: Exception) {
                null
            }

        fun Resultatkode.erType(type: ResultatkodeType): Boolean = Resultatkode.alleMedType(type).contains(this)

        fun Resultatkode.erDirekteAvslag(): Boolean = erType(ResultatkodeType.DIREKTE_AVSLAG)

        fun Resultatkode.erAvslag(): Boolean =
            erType(ResultatkodeType.AVSLAG) ||
                erType(ResultatkodeType.DIREKTE_AVSLAG)

        fun alleMedType(type: ResultatkodeType): List<Resultatkode> =
            try {
                enumValues<Resultatkode>().filter { it.type.contains(type) }
            } catch (e: Exception) {
                emptyList()
            }
    }

    data class BisysResultatkode(
        val resultatKode: String,
        val type: BisysResultatkodeType = BisysResultatkodeType.INNVILGELSE,
    )

    enum class BisysResultatkodeType {
        AVSLAG,
        OPPHØR,
        INNVILGELSE,
    }

    enum class ResultatkodeType {
        AVSLAG,
        DIREKTE_AVSLAG,
        FORSKUDD,
        BARNEBIDRAG,
        SÆRBIDRAG,
    }
}
