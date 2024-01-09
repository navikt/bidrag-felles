package no.nav.bidrag.domene.enums.inntekt

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class Inntektstype {
    AAP, // Ytelse fra offentlig
    DAGPENGER, // Ytelse fra offentlig
    FORELDREPENGER, // Ytelse fra offentlig
    INTRODUKSJONSSTØNAD, // Ytelse fra offentlig
    KVALIFISERINGSSTØNAD, // Ytelse fra offentlig
    OVERGANGSSTØNAD, // Ytelse fra offentlig
    PENSJON, // Ytelse fra offentlig
    SYKEPENGER, // Ytelse fra offentlig
    KONTANTSTØTTE, // Ytelse fra offentlig
    SMÅBARNSTILLEGG, // Ytelse fra offentlig
    UTVIDET_BARNETRYGD, // Ytelse fra offentlig
    KAPITALINNTEKT,
    LØNNSINNTEKT,
    NÆRINGSINNTEKT,
    ;

    companion object {

        /**
         * Extension function som returnerer en liste av inntektrapporteringer som inneholder den aktuelle inntekttypen.
         * @return Liste av type InntektRapportering
         */
        fun Inntektstype.inngårIInntektRapporteringer() =
            Inntektsrapportering.entries.filter { this in it.inneholderInntektstypeListe }
    }
}
