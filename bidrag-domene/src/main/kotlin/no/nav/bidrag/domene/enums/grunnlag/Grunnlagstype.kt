@file:Suppress("unused")

package no.nav.bidrag.domene.enums.grunnlag

import io.swagger.v3.oas.annotations.media.Schema

/**
 * Typer grunnlag som benyttes i behandling
 */
@Schema(enumAsRef = true)
enum class Grunnlagstype {
    SÆRFRADRAG,
    SKATTEKLASSE,
    NETTO_SÆRTILSKUDD,
    SAMVÆRSKLASSE,
    BIDRAGSEVNE,
    SAMVÆRSFRADRAG,
    SJABLON,
    LØPENDE_BIDRAG,
    FAKTISK_UTGIFT,
    BARNETILSYN_MED_STØNAD,
    FORPLEINING_UTGIFT,
    BARN,
    DELT_BOSTED,
    NETTO_BARNETILSYN,
    UNDERHOLDSKOSTNAD,
    BPS_ANDEL_UNDERHOLDSKOSTNAD,
    TILLEGGSBIDRAG,
    MAKS_BIDRAG_PER_BARN,
    BPS_ANDEL_SÆRTILSKUDD,
    MAKS_GRENSE_25_INNTEKT,
    GEBYRFRITAK,
    INNBETALT_BELØP,
    FORHOLDSMESSIG_FORDELING,
    KLAGE_STATISTIKK,

    BOSTATUS_PERIODE,
    SIVILSTAND_PERIODE,
    INNTEKT_RAPPORTERING_PERIODE,

    SØKNAD,
    VIRKNINGSTIDSPUNKT,
    NOTAT,

    SLUTTBEREGNING_FORSKUDD,
    DELBEREGNING_INNTEKT,
    DELBEREGNING_BARN_I_HUSSTAND,

    @Deprecated("Bruk de spesifikke grunnlagstypene som starter med PERSON_ i stedet")
    PERSON,
    PERSON_BIDRAGSMOTTAKER,
    PERSON_BIDRAGSPLIKTIG,
    PERSON_REELL_MOTTAKER,
    PERSON_SØKNADSBARN,
    PERSON_HUSSTANDSMEDLEM,

    // Inntekt som er beregnet av bidrag-inntekt
    BEREGNET_INNTEKT,

    INNHENTET_HUSSTANDSMEDLEM,
    INNHENTET_SIVILSTAND,
    INNHENTET_ARBEIDSFORHOLD,
    INNHENTET_INNTEKT_SKATTEGRUNNLAG_PERIODE,
    INNHENTET_INNTEKT_AORDNING_PERIODE,
    INNHENTET_INNTEKT_BARNETILLEGG_PERIODE,
    INNHENTET_INNTEKT_KONTANTSTØTTE_PERIODE,
    INNHENTET_INNTEKT_AINNTEKT_PERIODE,
    INNHENTET_INNTEKT_BARNETILSYN_PERIODE,
    INNHENTET_INNTEKT_SMÅBARNSTILLEGG_PERIODE,
    INNHENTET_INNTEKT_UTVIDETBARNETRYGD_PERIODE,
}
