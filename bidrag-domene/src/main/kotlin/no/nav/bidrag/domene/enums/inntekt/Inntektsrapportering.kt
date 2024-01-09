package no.nav.bidrag.domene.enums.inntekt

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class Inntektsrapportering(
    val hentesAutomatisk: Boolean,
    val kanLeggesInnManuelt: Boolean,
    val inneholderInntektstypeListe: List<Inntektstype>,
) {

    // Rapporteringer fra bidrag-inntekt

    AINNTEKT(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.AAP,
            Inntektstype.DAGPENGER,
            Inntektstype.FORELDREPENGER,
            Inntektstype.INTRODUKSJONSSTØNAD,
            Inntektstype.KVALIFISERINGSSTØNAD,
            Inntektstype.LØNNSINNTEKT,
            Inntektstype.OVERGANGSSTØNAD,
            Inntektstype.PENSJON,
            Inntektstype.SYKEPENGER,
        ),
    ),

    AINNTEKT_BEREGNET_3MND(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.AAP,
            Inntektstype.DAGPENGER,
            Inntektstype.FORELDREPENGER,
            Inntektstype.INTRODUKSJONSSTØNAD,
            Inntektstype.KVALIFISERINGSSTØNAD,
            Inntektstype.LØNNSINNTEKT,
            Inntektstype.OVERGANGSSTØNAD,
            Inntektstype.PENSJON,
            Inntektstype.SYKEPENGER,
        ),
    ),

    AINNTEKT_BEREGNET_12MND(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.AAP,
            Inntektstype.DAGPENGER,
            Inntektstype.FORELDREPENGER,
            Inntektstype.INTRODUKSJONSSTØNAD,
            Inntektstype.KVALIFISERINGSSTØNAD,
            Inntektstype.LØNNSINNTEKT,
            Inntektstype.OVERGANGSSTØNAD,
            Inntektstype.PENSJON,
            Inntektstype.SYKEPENGER,
        ),
    ),

    KAPITALINNTEKT(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.KAPITALINNTEKT,
        ),
    ),

    LIGNINGSINNTEKT(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.AAP,
            Inntektstype.DAGPENGER,
            Inntektstype.FORELDREPENGER,
            Inntektstype.INTRODUKSJONSSTØNAD,
            Inntektstype.KVALIFISERINGSSTØNAD,
            Inntektstype.LØNNSINNTEKT,
            Inntektstype.OVERGANGSSTØNAD,
            Inntektstype.PENSJON,
            Inntektstype.SYKEPENGER,
        ),
    ),

    KONTANTSTØTTE(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = listOf(
            Inntektstype.KONTANTSTØTTE,
        ),
    ),

    SMÅBARNSTILLEGG(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = listOf(
            Inntektstype.SMÅBARNSTILLEGG,
        ),
    ),

    UTVIDET_BARNETRYGD(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = listOf(
            Inntektstype.UTVIDET_BARNETRYGD,
        ),
    ),

    AAP(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.AAP,
        ),
    ),

    DAGPENGER(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.DAGPENGER,
        ),
    ),

    FORELDREPENGER(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.FORELDREPENGER,
        ),
    ),

    INTRODUKSJONSSTØNAD(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.INTRODUKSJONSSTØNAD,
        ),
    ),

    KVALIFISERINGSSTØNAD(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.KVALIFISERINGSSTØNAD,
        ),
    ),

    OVERGANGSSTØNAD(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.OVERGANGSSTØNAD,
        ),
    ),

    PENSJON(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.PENSJON,
        ),
    ),

    SYKEPENGER(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = listOf(
            Inntektstype.SYKEPENGER,
        ),
    ),

    // Manuelt registrerte rapporteringer

    PERSONINNTEKT_EGNE_OPPLYSNINGER(
        hentesAutomatisk = false,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = listOf(
            Inntektstype.AAP,
            Inntektstype.DAGPENGER,
            Inntektstype.FORELDREPENGER,
            Inntektstype.INTRODUKSJONSSTØNAD,
            Inntektstype.KVALIFISERINGSSTØNAD,
            Inntektstype.LØNNSINNTEKT,
            Inntektstype.OVERGANGSSTØNAD,
            Inntektstype.PENSJON,
            Inntektstype.SYKEPENGER,
        ),
    ),

    KAPITALINNTEKT_EGNE_OPPLYSNINGER(
        hentesAutomatisk = false,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = listOf(
            Inntektstype.KAPITALINNTEKT,
        ),
    ),

    SAKSBEHANDLER_BEREGNET_INNTEKT(
        hentesAutomatisk = false,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = listOf(
            Inntektstype.AAP,
            Inntektstype.DAGPENGER,
            Inntektstype.FORELDREPENGER,
            Inntektstype.INTRODUKSJONSSTØNAD,
            Inntektstype.KVALIFISERINGSSTØNAD,
            Inntektstype.LØNNSINNTEKT,
            Inntektstype.OVERGANGSSTØNAD,
            Inntektstype.PENSJON,
            Inntektstype.SYKEPENGER,
        ),
    ),

    LØNN_MANUELT_BEREGNET(
        hentesAutomatisk = false,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = listOf(
            Inntektstype.LØNNSINNTEKT,
        ),
    ),

    NÆRINGSINNTEKT_MANUELT_BEREGNET(
        hentesAutomatisk = false,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = listOf(
            Inntektstype.NÆRINGSINNTEKT,
        ),
    ),

    YTELSE_FRA_OFFENTLIG_MANUELT_BEREGNET(
        hentesAutomatisk = false,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = listOf(
            Inntektstype.AAP,
            Inntektstype.DAGPENGER,
            Inntektstype.FORELDREPENGER,
            Inntektstype.INTRODUKSJONSSTØNAD,
            Inntektstype.KVALIFISERINGSSTØNAD,
            Inntektstype.OVERGANGSSTØNAD,
            Inntektstype.PENSJON,
            Inntektstype.SYKEPENGER,
        ),
    ),

    // Rapporteringer brukt i Bisys/BBM

    AINNTEKT_KORRIGERT_BARNETILLEGG(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    BARNETRYGD_MANUELL_VURDERING(
        hentesAutomatisk = false,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = emptyList(),
    ),

    BARNS_SYKDOM(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    DOKUMENTASJON_MANGLER_SKJØNN(
        hentesAutomatisk = false,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = emptyList(),
    ),

    FORDEL_SKATTEKLASSE2(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    FORDEL_SÆRFRADRAG_ENSLIG_FORSØRGER(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = emptyList(),
    ),

    FØDSEL_ADOPSJON(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    INNTEKTSOPPLYSNINGER_ARBEIDSGIVER(
        hentesAutomatisk = false,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = emptyList(),
    ),

    KAPITALINNTEKT_SKE(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    LIGNINGSOPPLYSNINGER_MANGLER(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    LIGNING_SKE(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    LØNN_SKE(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    LØNN_SKE_KORRIGERT_BARNETILLEGG(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    LØNN_TREKK(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    MANGLENDE_BRUK_EVNE_SKJØNN(
        hentesAutomatisk = false,
        kanLeggesInnManuelt = true,
        inneholderInntektstypeListe = emptyList(),
    ),

    NETTO_KAPITALINNTEKT(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    PENSJON_KORRIGERT_BARNETILLEGG(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    REHABILITERINGSPENGER(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    SKATTEGRUNNLAG_KORRIGERT_BARNETILLEGG(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),

    SKATTEGRUNNLAG_SKE(
        hentesAutomatisk = true,
        kanLeggesInnManuelt = false,
        inneholderInntektstypeListe = emptyList(),
    ),
    ;

    companion object {

        /**
         * Extension function som returnerer true hvis de to inntektrapporteringene som sammenlignes ikke inneholder noen like inntektstyper.
         * @param inntektsrapportering2 Verdi av type InntektRapportering som det skal sammenlignes med
         * @return true hvis inntektsrapporteringene som sammenlignes kan brukes samtidig
         */
        fun Inntektsrapportering.kanBrukesSammenMed(inntektsrapportering2: Inntektsrapportering) =
            !(this.inneholderInntektstypeListe.any { inntektsrapportering2.inneholderInntektstypeListe.contains(it) })
    }
}
