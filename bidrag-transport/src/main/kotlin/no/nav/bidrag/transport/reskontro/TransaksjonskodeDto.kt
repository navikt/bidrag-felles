package no.nav.bidrag.transport.reskontro

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.regnskap.Transaksjonskode

/**
 * Informasjon om en transaksjonskode.
 */
@Schema(
    name = "TransaksjonskodeDto",
    description = "Informasjon om en transaksjonskode.",
)
data class TransaksjonskodeDto(
    @field:Schema(
        description = "Transaksjonskoden.",
        example = "A1",
    )
    val kode: String,

    @field:Schema(
        description = "Korreksjonskode for transaksjonskoden, hvis det finnes.",
        example = "A3",
    )
    val korreksjonskode: String?,

    @field:Schema(
        description = "Beskrivelse av transaksjonskoden.",
        example = "Bidragsforskudd",
    )
    val beskrivelse: String,
)

@Schema(
    name = "TransaksjonskodeListeDto",
    description = "Liste med informasjon om alle transaksjonskoder.",
)
data class TransaksjonskodeListeDto(
    @field:Schema(
        description = "Liste med transaksjonskoder.",
    )
    val transaksjonskoder: List<TransaksjonskodeDto>,
)

fun List<Transaksjonskode>.tilDto(): TransaksjonskodeListeDto {
    return TransaksjonskodeListeDto(
        transaksjonskoder = this.map { transaksjon ->
            TransaksjonskodeDto(
                kode = transaksjon.name,
                korreksjonskode = transaksjon.korreksjonskode,
                beskrivelse = transaksjon.beskrivelse
            )
        }
    )
}