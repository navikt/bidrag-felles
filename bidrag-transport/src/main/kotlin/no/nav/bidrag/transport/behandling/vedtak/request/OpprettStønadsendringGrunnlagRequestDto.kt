package no.nav.bidrag.transport.behandling.vedtak.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min

@Schema
data class OpprettStønadsendringGrunnlagRequestDto(

    @Schema(description = "Stønadsendring-id")
    @Min(0)
    val stønadsendringId: Int,

    @Schema(description = "grunnlag-id")
    @Min(0)
    val grunnlagId: Int,
)
