package no.nav.bidrag.domene.enums.sjablon

enum class SjablonTallNavn(
    val navn: String,
    val id: String,
    val bidragsevne: Boolean,
    val nettoBarnetilsyn: Boolean,
    val underholdskostnad: Boolean,
    val bpAndelUnderholdskostnad: Boolean,
    val barnebidrag: Boolean,
    val forskudd: Boolean,
    val bpAndelSaertilskudd: Boolean,
    val saertilskudd: Boolean,
) {
    ORDINÆR_BARNETRYGD_BELØP("OrdinærBarnetrygdBeløp", "0001", false, false, true, false, false, false, false, false),
    ORDINÆR_SMÅBARNSTILLEGG_BELØP("OrdinærSmåbarnstilleggBeløp", "0002", false, false, false, false, false, false, false, false),
    BOUTGIFTER_BIDRAGSBARN_BELØP("BoutgifterBidragsbarnBeløp", "0003", false, false, true, false, false, false, false, false),
    FORDEL_SKATTEKLASSE2_BELØP("FordelSkatteklasse2Beløp", "0004", true, false, false, true, false, false, true, false),
    FORSKUDDSSATS_BELØP("ForskuddssatsBeløp", "0005", false, false, false, true, false, true, true, false),
    INNSLAG_KAPITALINNTEKT_BELØP("InnslagKapitalInntektBeløp", "0006", false, false, false, false, false, false, false, false),
    INNTEKTSINTERVALL_TILLEGGSBIDRAG_BELØP("InntektsintervallTilleggsbidragBeløp", "0007", false, false, false, false, false, false, false, false),
    MAKS_INNTEKT_BP_PROSENT("MaksInntektBPProsent", "0008", false, false, false, false, false, false, false, false),
    HØY_INNTEKT_BP_MULTIPLIKATOR("HøyInntektBPMultiplikator", "0009", false, false, false, false, false, false, false, false),
    INNTEKT_BB_MULTIPLIKATOR("InntektBBMultiplikator", "0010", false, false, false, false, false, false, false, false),
    MAKS_BIDRAG_MULTIPLIKATOR("MaksBidragMultiplikator", "0011", false, false, false, false, false, false, false, false),
    MAKS_INNTEKT_BB_MULTIPLIKATOR("MaksInntektBBMultiplikator", "0012", false, false, false, false, false, false, false, false),
    MAKS_INNTEKT_FORSKUDD_MOTTAKER_MULTIPLIKATOR("MaksInntektForskuddMottakerMultiplikator", "0013", false, false, false, false, false, true, false, false),
    NEDRE_INNTEKTSGRENSE_GEBYR_BELØP("NedreInntektsgrenseGebyrBeløp", "0014", false, false, false, false, false, false, false, false),
    SKATT_ALMINNELIG_INNTEKT_PROSENT("SkattAlminneligInntektProsent", "0015", false, true, false, false, false, false, false, false),
    TILLEGGSBIDRAG_PROSENT("TilleggsbidragProsent", "0016", false, false, false, false, false, false, false, false),
    TRYGDEAVGIFT_PROSENT("TrygdeavgiftProsent", "0017", true, false, false, false, false, false, false, false),
    BARNETILLEGG_SKATT_PROSENT("BarneTilleggSkattProsent", "0018", false, false, false, false, false, false, false, false),
    UNDERHOLD_EGNE_BARN_I_HUSSTAND_BELØP("UnderholdEgneBarnIHusstandBeløp", "0019", true, false, false, false, false, false, false, false),
    ENDRING_BIDRAG_GRENSE_PROSENT("EndringBidragGrenseProsent", "0020", false, false, false, false, false, false, false, false),
    BARNETILLEGG_FORSVARET_FØRSTE_BARN_BELØP("BarnetilleggForsvaretFørsteBarnBeløp", "0021", false, false, false, false, true, false, false, false),
    BARNETILLEGG_FORSVARET_ØVRIGE_BARN_BELØP("BarnetilleggForsvaretØvrigeBarnBeløp", "0022", false, false, false, false, true, false, false, false),
    MINSTEFRADRAG_INNTEKT_BELØP("MinstefradragInntektBeløp", "0023", true, false, false, false, false, false, false, false),
    GJENNOMSNITT_VIRKEDAGER_PR_MÅNED_ANTALL("GjennomsnittVirkedagerPrMånedAntall", "0024", false, false, false, false, false, false, false, false),
    MINSTEFRADRAG_INNTEKT_PROSENT("MinstefradragInntektProsent", "0025", true, false, false, false, false, false, false, false),
    DAGLIG_SATS_BARNETILLEGG_BELØP("DagligSatsBarnetilleggBeløp", "0026", false, false, false, false, false, false, false, false),
    PERSONFRADRAG_KLASSE1_BELØP("PersonfradragKlasse1Beløp", "0027", true, false, false, false, false, false, false, false),
    PERSONFRADRAG_KLASSE2_BELØP("PersonfradragKlasse2Beløp", "0028", true, false, false, false, false, false, false, false),
    KONTANTSTOTTE_BELØP("KontantstøtteBeløp", "0029", false, false, false, false, false, false, false, false),
    ØVRE_INNTEKTSGRENSE_IKKE_I_SKATTEPOSISJON_BELØP("ØvreInntektsgrenseIkkeISkatteposisjonBeløp", "0030", false, false, false, true, false, false, true, false),
    NEDRE_INNTEKTSGRENSE_FULL_SKATTEPOSISJON_BELØP("NedreInntektsgrenseFullSkatteposisjonBeløp", "0031", false, false, false, true, false, false, true, false),
    EKSTRA_SMÅBARNSTILLEGG_BELØP("EkstraSmåbarnstilleggBeløp", "0032", false, false, false, false, false, false, false, false),
    ØVRE_INNTEKTSGRENSE_FULLT_FORSKUDD_BELØP("ØvreInntektsgrenseFulltForskuddBeløp", "0033", false, false, false, false, false, true, false, false),
    ØVRE_INNTEKTSGRENSE_75PROSENT_FORSKUDD_EN_BELØP("ØvreInntektsgrense75ProsentForskuddEnBeløp", "0034", false, false, false, false, false, true, false, false),
    ØVRE_INNTEKTSGRENSE_75PROSENT_FORSKUDD_GS_BELØP("ØvreInntektsgrense75ProsentForskuddGSBeløp", "0035", false, false, false, false, false, true, false, false),
    INNTEKTSINTERVALL_FORSKUDD_BELØP("InntektsintervallForskuddBeløp", "0036", false, false, false, false, false, true, false, false),
    ØVRE_GRENSE_SÆRTILSKUDD_BELØP("ØvreGrenseSærtilskuddBeløp", "0037", false, false, false, false, false, false, false, false),
    FORSKUDDSSATS_75PROSENT_BELØP("Forskuddssats75ProsentBeløp", "0038", false, false, false, false, false, true, false, false),
    FORDEL_SÆRFRADRAG_BELØP("FordelSærfradragBeløp", "0039", true, false, false, true, false, false, true, false),
    SKATTESATS_ALMINNELIG_INNTEKT_PROSENT("SkattesatsAlminneligInntektProsent", "0040", true, false, false, false, false, false, false, false),
    FORHØYET_BARNETRYGD_BELØP("ForhøyetBarnetrygdBeløp", "0041", false, false, true, false, false, false, false, false),
    FASTSETTELSESGEBYR_BELØP("FastsettelsesgebyrBeløp", "0100", false, false, false, false, false, false, false, false),
    DUMMY("Dummy", "9999", false, false, false, false, false, false, false, false),
    ;

    companion object {
        fun from(search: String): SjablonTallNavn = requireNotNull(entries.find { it.navn == search }) { DUMMY }
    }
}
