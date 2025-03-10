package no.nav.bidrag.transport.behandling.beregning.felles

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import java.time.YearMonth

@Schema(description = "Grunnlaget for en beregning av barnebidrag, forskudd og særbidrag")
data class BeregnGrunnlag(
    @Schema(description = "Beregningsperiode") val periode: ÅrMånedsperiode,
    @Schema(description = "Dato hvis siste periode skal opphøres") val opphørsdato: YearMonth? = null,
    @Schema(description = "Stønadstype som skal beregnes") val stønadstype: Stønadstype = Stønadstype.BIDRAG,
    @Schema(description = "Referanse til Person-objekt som tilhører søknadsbarnet") val søknadsbarnReferanse: String,
    @Schema(description = "Periodisert liste over grunnlagselementer") val grunnlagListe: List<GrunnlagDto> = emptyList(),
) {
    val opphørSistePeriode get() = opphørsdato != null
}

fun BeregnGrunnlag.valider() {
    require(søknadsbarnReferanse.isNotEmpty()) { "søknadsbarnReferanse kan ikke være tom streng" }
    require(grunnlagListe.isNotEmpty()) { "grunnlagListe kan ikke være tom" }
    requireNotNull(periode.til) { "beregningsperiode til kan ikke være null" }
    grunnlagListe.forEach(GrunnlagDto::valider)
}
