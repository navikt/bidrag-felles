package no.nav.bidrag.transport.behandling.beregning.felles

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto

@Schema(description = "Grunnlaget for en beregning av barnebidrag, forskudd og særtilskudd")
data class BeregnGrunnlag(
    @Schema(description = "Beregningsperiode") val periode: ÅrMånedsperiode,
    @Schema(description = "Referanse til Person-objekt som tilhører søknadsbarnet") val søknadsbarnReferanse: String,
    @Schema(description = "Periodisert liste over grunnlagselementer") val grunnlagListe: List<GrunnlagDto> = emptyList(),
)

fun BeregnGrunnlag.valider() {
    require(søknadsbarnReferanse.isNotEmpty()) { "søknadsbarnReferanse kan ikke være tom strebg" }
    require(grunnlagListe.isNotEmpty()) { "grunnlagListe kan ikke være tom" }
    requireNotNull(periode.fom) { "beregningsperiode fom kan ikke være null" }
    requireNotNull(periode.til) { "beregningsperiode til kan ikke være null" }
    grunnlagListe.forEach(GrunnlagDto::valider)
}
