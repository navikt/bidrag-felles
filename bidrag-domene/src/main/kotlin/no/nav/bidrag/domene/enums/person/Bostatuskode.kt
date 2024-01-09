package no.nav.bidrag.domene.enums.person

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class Bostatuskode {
    MED_FORELDER,
    DOKUMENTERT_SKOLEGANG, // Hvis barnet er over 18 år, bor med forelder og går på skole
    IKKE_MED_FORELDER,
    MED_VERGE,
    ALENE,
    DELT_BOSTED,
    REGNES_IKKE_SOM_BARN, // TODO: Avklar (faglig) om dette skal brukes
}
