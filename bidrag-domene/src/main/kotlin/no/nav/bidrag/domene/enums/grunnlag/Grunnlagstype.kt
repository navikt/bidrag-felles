package no.nav.bidrag.domene.enums.grunnlag

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Typer grunnlag som benyttes i behandling
 */
@Schema(enumAsRef = true)
enum class Grunnlagstype(val value: String) {
    SÆRFRADRAG(GrunnlagstypeConstants.SÆRFRADRAG),
    SØKNADSBARN_INFO(GrunnlagstypeConstants.SØKNADSBARN_INFO),
    SKATTEKLASSE(GrunnlagstypeConstants.SKATTEKLASSE),
    BARN_I_HUSSTAND(GrunnlagstypeConstants.BARN_I_HUSSTAND),
    BOSTATUS(GrunnlagstypeConstants.BOSTATUS),
    BOSTATUS_BP(GrunnlagstypeConstants.BOSTATUS_BP),
    INNTEKT(GrunnlagstypeConstants.INNTEKT),
    INNTEKT_BARN(GrunnlagstypeConstants.INNTEKT_BARN),
    INNTEKT_UTVIDET_BARNETRYGD(GrunnlagstypeConstants.INNTEKT_UTVIDET_BARNETRYGD),
    KAPITALINNTEKT(GrunnlagstypeConstants.KAPITALINNTEKT),
    KAPITALINNTEKT_BARN(GrunnlagstypeConstants.KAPITALINNTEKT_BARN),
    NETTO_SÆRTILSKUDD(GrunnlagstypeConstants.NETTO_SÆRTILSKUDD),
    SAMVÆRSKLASSE(GrunnlagstypeConstants.SAMVÆRSKLASSE),
    BIDRAGSEVNE(GrunnlagstypeConstants.BIDRAGSEVNE),
    SAMVÆRSFRADRAG(GrunnlagstypeConstants.SAMVÆRSFRADRAG),
    SJABLON(GrunnlagstypeConstants.SJABLON),
    LØPENDE_BIDRAG(GrunnlagstypeConstants.LØPENDE_BIDRAG),
    FAKTISK_UTGIFT(GrunnlagstypeConstants.FAKTISK_UTGIFT),
    BARNETILSYN_MED_STØNAD(GrunnlagstypeConstants.BARNETILSYN_MED_STØNAD),
    FORPLEINING_UTGIFT(GrunnlagstypeConstants.FORPLEINING_UTGIFT),
    BARN(GrunnlagstypeConstants.BARN),
    SIVILSTAND(GrunnlagstypeConstants.SIVILSTAND),
    BARNETILLEGG(GrunnlagstypeConstants.BARNETILLEGG),
    BARNETILLEGG_FORSVARET(GrunnlagstypeConstants.BARNETILLEGG_FORSVARET),
    DELT_BOSTED(GrunnlagstypeConstants.DELT_BOSTED),
    NETTO_BARNETILSYN(GrunnlagstypeConstants.NETTO_BARNETILSYN),
    UNDERHOLDSKOSTNAD(GrunnlagstypeConstants.UNDERHOLDSKOSTNAD),
    BPS_ANDEL_UNDERHOLDSKOSTNAD(GrunnlagstypeConstants.BPS_ANDEL_UNDERHOLDSKOSTNAD),
    TILLEGGSBIDRAG(GrunnlagstypeConstants.TILLEGGSBIDRAG),
    MAKS_BIDRAG_PER_BARN(GrunnlagstypeConstants.MAKS_BIDRAG_PER_BARN),
    BPS_ANDEL_SÆRTILSKUDD(GrunnlagstypeConstants.BPS_ANDEL_SÆRTILSKUDD),
    MAKS_GRENSE_25_INNTEKT(GrunnlagstypeConstants.MAKS_GRENSE_25_INNTEKT),
    GEBYRFRITAK(GrunnlagstypeConstants.GEBYRFRITAK),
    SØKNAD_INFO(GrunnlagstypeConstants.SØKNAD_INFO),
    BARN_INFO(GrunnlagstypeConstants.BARN_INFO),
    PERSON_INFO(GrunnlagstypeConstants.PERSON_INFO),
    SAKSBEHANDLER_INFO(GrunnlagstypeConstants.SAKSBEHANDLER_INFO),
    VEDTAK_INFO(GrunnlagstypeConstants.VEDTAK_INFO),
    INNBETALT_BELØP(GrunnlagstypeConstants.INNBETALT_BELØP),
    FORHOLDSMESSIG_FORDELING(GrunnlagstypeConstants.FORHOLDSMESSIG_FORDELING),
    SLUTTBEREGNING_BBM(GrunnlagstypeConstants.SLUTTBEREGNING_BBM),
    KLAGE_STATISTIKK(GrunnlagstypeConstants.KLAGE_STATISTIKK),
    PERSON(GrunnlagstypeConstants.PERSON),
    BOSTATUS_PERIODE(GrunnlagstypeConstants.BOSTATUS_PERIODE),
    BEREGNING_INNTEKT_RAPPORTERING_PERIODE(GrunnlagstypeConstants.BEREGNING_INNTEKT_RAPPORTERING_PERIODE),
    SIVILSTAND_PERIODE(GrunnlagstypeConstants.SIVILSTAND_PERIODE),
    VIRKNINGSDATO(GrunnlagstypeConstants.VIRKNINGSDATO),
    NOTAT(GrunnlagstypeConstants.NOTAT),
    ;

    object GrunnlagstypeConstants {
        const val SÆRFRADRAG = "SÆRFRADRAG"
        const val SØKNADSBARN_INFO = "SØKNADSBARN_INFO"
        const val SKATTEKLASSE = "SKATTEKLASSE"
        const val BARN_I_HUSSTAND = "BARN_I_HUSSTAND"
        const val BOSTATUS = "BOSTATUS"
        const val BOSTATUS_BP = "BOSTATUS_BP"
        const val INNTEKT = "INNTEKT"
        const val INNTEKT_BARN = "INNTEKT_BARN"
        const val INNTEKT_UTVIDET_BARNETRYGD = "INNTEKT_UTVIDET_BARNETRYGD"
        const val KAPITALINNTEKT = "KAPITALINNTEKT"
        const val KAPITALINNTEKT_BARN = "KAPITALINNTEKT_BARN"
        const val NETTO_SÆRTILSKUDD = "NETTO_SÆRTILSKUDD"
        const val SAMVÆRSKLASSE = "SAMVÆRSKLASSE"
        const val BIDRAGSEVNE = "BIDRAGSEVNE"
        const val SAMVÆRSFRADRAG = "SAMVÆRSFRADRAG"
        const val SJABLON = "SJABLON"
        const val LØPENDE_BIDRAG = "LØPENDE_BIDRAG"
        const val FAKTISK_UTGIFT = "FAKTISK_UTGIFT"
        const val BARNETILSYN_MED_STØNAD = "BARNETILSYN_MED_STØNAD"
        const val FORPLEINING_UTGIFT = "FORPLEINING_UTGIFT"
        const val BARN = "BARN"
        const val SIVILSTAND = "SIVILSTAND"
        const val BARNETILLEGG = "BARNETILLEGG"
        const val BARNETILLEGG_FORSVARET = "BARNETILLEGG_FORSVARET"
        const val DELT_BOSTED = "DELT_BOSTED"
        const val NETTO_BARNETILSYN = "NETTO_BARNETILSYN"
        const val UNDERHOLDSKOSTNAD = "UNDERHOLDSKOSTNAD"
        const val BPS_ANDEL_UNDERHOLDSKOSTNAD = "BPS_ANDEL_UNDERHOLDSKOSTNAD"
        const val TILLEGGSBIDRAG = "TILLEGGSBIDRAG"
        const val MAKS_BIDRAG_PER_BARN = "MAKS_BIDRAG_PER_BARN"
        const val BPS_ANDEL_SÆRTILSKUDD = "BPS_ANDEL_SÆRTILSKUDD"
        const val MAKS_GRENSE_25_INNTEKT = "MAKS_GRENSE_25_INNTEKT"
        const val GEBYRFRITAK = "GEBYRFRITAK"
        const val SØKNAD_INFO = "SØKNAD_INFO"
        const val BARN_INFO = "BARN_INFO"
        const val PERSON_INFO = "PERSON_INFO"
        const val SAKSBEHANDLER_INFO = "SAKSBEHANDLER_INFO"
        const val VEDTAK_INFO = "VEDTAK_INFO"
        const val INNBETALT_BELØP = "INNBETALT_BELØP"
        const val FORHOLDSMESSIG_FORDELING = "FORHOLDSMESSIG_FORDELING"
        const val SLUTTBEREGNING_BBM = "SLUTTBEREGNING_BBM"
        const val KLAGE_STATISTIKK = "KLAGE_STATISTIKK"
        const val PERSON = "PERSON"
        const val BOSTATUS_PERIODE = "BOSTATUS_PERIODE"
        const val BEREGNING_INNTEKT_RAPPORTERING_PERIODE = "BEREGNING_INNTEKT_RAPPORTERING_PERIODE"
        const val SIVILSTAND_PERIODE = "SIVILSTAND_PERIODE"
        const val VIRKNINGSDATO = "VIRKNINGSDATO"
        const val NOTAT = "NOTAT"
    }
}
