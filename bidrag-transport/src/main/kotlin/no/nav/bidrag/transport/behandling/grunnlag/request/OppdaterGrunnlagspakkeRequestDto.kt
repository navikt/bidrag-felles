package no.nav.bidrag.transport.behandling.grunnlag.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import no.nav.bidrag.domene.enums.grunnlag.GrunnlagRequestType
import java.time.LocalDate

data class OppdaterGrunnlagspakkeRequestDto(

    @Schema(description = "Opplysningene som hentes er gyldige til (men ikke med) denne datoen (YYYY-MM-DD")
    val gyldigTil: LocalDate? = null,

    @Schema(description = "Liste over hvilke typer grunnlag som skal hentes inn. På nivået under er personId og perioder angitt")
    @field:Valid
    @field:NotEmpty(message = "Listen kan ikke være null eller tom.")
    val grunnlagRequestDtoListe: List<GrunnlagRequestDto>,
)

data class GrunnlagRequestDto(

    @Schema(description = "Hvilken type grunnlag skal hentes")
    val type: GrunnlagRequestType,

    @Schema(description = "Angir personId som grunnlag skal hentes for")
    @field:Pattern(
        regexp = "^[0-9]{11}\$",
        message = "Ugyldig format. Må inneholde eksakt 11 siffer.",
    )
    val personId: String,

    @Schema(description = "Første periode det skal hentes ut grunnlag for (på formatet YYYY-MM-DD)")
    val periodeFra: LocalDate,

    @Schema(description = "Grunnlag skal hentes TIL denne perioden, på formatet YYYY-MM-DD")
    val periodeTil: LocalDate,
)
