@file:Suppress("unused")

package no.nav.bidrag.domene.enums.grunnlag

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Typer grunnlag som benyttes i behandling
 */
@Schema(enumAsRef = true)
enum class Grunnlagstype {
    @JsonEnumDefaultValue
    UKJENT,

    SÆRFRADRAG,
    SKATTEKLASSE,
    SAMVÆRSKLASSE,
    BIDRAGSEVNE,
    LØPENDE_BIDRAG,
    FAKTISK_UTGIFT_PERIODE,
    TILLEGGSSTØNAD_PERIODE,
    BARNETILSYN_MED_STØNAD_PERIODE,
    FORPLEINING_UTGIFT,

    @Deprecated(
        "Bruk NETTO_TILSYNSUTGIFT i stedet",
        replaceWith = ReplaceWith("NETTO_TILSYNSUTGIFT"),
    )
    NETTO_BARNETILSYN,
    UNDERHOLDSKOSTNAD,
    BPS_ANDEL_UNDERHOLDSKOSTNAD,
    TILLEGGSBIDRAG,
    MAKS_BIDRAG_PER_BARN,
    MAKS_GRENSE_25_INNTEKT,
    GEBYRFRITAK,
    INNBETALT_BELØP,
    FORHOLDSMESSIG_FORDELING,
    KLAGE_STATISTIKK,
    NETTO_TILSYNSUTGIFT,

    // Barnebidrag
    SAMVÆRSPERIODE,
    SAMVÆRSKALKULATOR,
    DELBEREGNING_SAMVÆRSKLASSE,
    DELBEREGNING_SAMVÆRSKLASSE_NETTER,

    // Sjablon
    @JsonAlias("SJABLON")
    SJABLON_SJABLONTALL,

    SJABLON_BIDRAGSEVNE,
    SJABLON_TRINNVIS_SKATTESATS,
    SJABLON_BARNETILSYN,
    SJABLON_FORBRUKSUTGIFTER,
    SJABLON_SAMVARSFRADRAG,
    SJABLON_MAKS_FRADRAG,
    SJABLON_MAKS_TILSYN,

    BOSTATUS_PERIODE,
    SIVILSTAND_PERIODE,
    INNTEKT_RAPPORTERING_PERIODE,

    SØKNAD,
    VIRKNINGSTIDSPUNKT,
    NOTAT,

    // Særbidrag
    SÆRBIDRAG_KATEGORI,
    UTGIFT_DIREKTE_BETALT,
    UTGIFT_MAKS_GODKJENT_BELØP,
    UTGIFTSPOSTER,

    SLUTTBEREGNING_FORSKUDD,
    DELBEREGNING_SUM_INNTEKT,
    DELBEREGNING_BARN_I_HUSSTAND,

    SLUTTBEREGNING_SÆRBIDRAG,
    DELBEREGNING_BIDRAGSEVNE,
    DELBEREGNING_BIDRAGSPLIKTIGES_BEREGNEDE_TOTALBIDRAG,
    DELBEREGNING_VOKSNE_I_HUSSTAND,
    DELBEREGNING_FAKTISK_UTGIFT,
    DELBEREGNING_TILLEGGSSTØNAD,

    DELBEREGNING_BOFORHOLD,

    @Deprecated(
        "Bruk DELBEREGNING_BIDRAGSPLIKTIGES_ANDEL i stedet",
        replaceWith = ReplaceWith("DELBEREGNING_BIDRAGSPLIKTIGES_ANDEL"),
    )
    DELBEREGNING_BIDRAGSPLIKTIGES_ANDEL_SÆRBIDRAG,
    DELBEREGNING_BIDRAGSPLIKTIGES_ANDEL,
    DELBEREGNING_UTGIFT,

    // Bidrag
    DELBEREGNING_SAMVÆRSFRADRAG,
    DELBEREGNING_NETTO_TILSYNSUTGIFT,
    DELBEREGNING_BARNETILLEGG_SKATTESATS,
    DELBEREGNING_NETTO_BARNETILLEGG,

    DELBEREGNING_UNDERHOLDSKOSTNAD,
    SLUTTBEREGNING_BARNEBIDRAG,
    BARNETILLEGG_PERIODE,

    // Gebyr
    MANUELT_OVERSTYRT_GEBYR,
    DELBEREGNING_INNTEKTSBASERT_GEBYR,
    SLUTTBEREGNING_GEBYR,

    @Deprecated("Bruk de spesifikke grunnlagstypene som starter med PERSON_ i stedet")
    PERSON,
    PERSON_BIDRAGSMOTTAKER,
    PERSON_BIDRAGSPLIKTIG,
    PERSON_REELL_MOTTAKER,
    PERSON_SØKNADSBARN,
    PERSON_HUSSTANDSMEDLEM,

    @Schema(description = "Barn til BP som ikke er husstandsmedlem eller søknadsbarn")
    PERSON_BARN_BIDRAGSPLIKTIG,

    @Schema(description = "Barn til BM som ikke er husstandsmedlem eller søknadsbarn")
    PERSON_BARN_BIDRAGSMOTTAKER,

    // Inntekt som er beregnet av bidrag-inntekt
    BEREGNET_INNTEKT,

    INNHENTET_HUSSTANDSMEDLEM,
    INNHENTET_ANDRE_VOKSNE_I_HUSSTANDEN,
    INNHENTET_SIVILSTAND,
    INNHENTET_ARBEIDSFORHOLD,
    INNHENTET_TILLEGGSTØNAD,

    INNHENTET_INNTEKT_SKATTEGRUNNLAG_PERIODE,
    INNHENTET_INNTEKT_AORDNING,
    INNHENTET_INNTEKT_BARNETILLEGG,
    INNHENTET_INNTEKT_KONTANTSTØTTE,
    INNHENTET_INNTEKT_AINNTEKT,
    INNHENTET_INNTEKT_BARNETILSYN,
    INNHENTET_INNTEKT_SMÅBARNSTILLEGG,
    INNHENTET_INNTEKT_UTVIDETBARNETRYGD,

    // Brukes ifbm Bisys grunnlag
    UNNTAK,
}
