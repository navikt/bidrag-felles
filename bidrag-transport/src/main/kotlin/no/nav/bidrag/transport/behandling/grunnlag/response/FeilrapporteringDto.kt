package no.nav.bidrag.transport.behandling.grunnlag.response

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.grunnlag.GrunnlagRequestType
import org.springframework.http.HttpStatusCode
import java.time.LocalDate

data class FeilrapporteringDto(
    @Schema(description = "Type grunnlag")
    val grunnlagstype: GrunnlagRequestType,
    @Schema(description = "Id til personen grunnlaget er forsøkt hentet for")
    val personId: String?,
    @Schema(description = "Periode fra-dato")
    val periodeFra: LocalDate?,
    @Schema(description = "Periode til-dato")
    val periodeTil: LocalDate?,
    @Schema(description = "Http statuskode")
    val feilkode: HttpStatusCode?,
    @Schema(description = "Feilmelding")
    val feilmelding: String?,
)
