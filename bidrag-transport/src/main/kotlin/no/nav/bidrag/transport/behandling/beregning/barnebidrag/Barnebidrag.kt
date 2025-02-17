package no.nav.bidrag.transport.behandling.beregning.barnebidrag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import java.math.BigDecimal

@Schema(description = "Resultatet av en barnebidragsberegning")
data class BeregnetBarnebidragResultat(
    @Schema(description = "Periodisert liste over resultat av barnebidragsberegning")
    var beregnetBarnebidragPeriodeListe: List<ResultatPeriode> = emptyList(),
    @Schema(description = "Liste over grunnlag brukt i beregning")
    var grunnlagListe: List<GrunnlagDto> = emptyList(),
)

@Schema(description = "Resultatet av en beregning for en gitt periode - barnebidrag")
data class ResultatPeriode(
    @Schema(description = "Beregnet resultat periode") var periode: ÅrMånedsperiode,
    @Schema(description = "Beregnet resultat") var resultat: ResultatBeregning,
    @Schema(description = "Beregnet grunnlag innhold") var grunnlagsreferanseListe: List<String>,
)

@Schema(description = "Resultatet av en beregning")
data class ResultatBeregning(
    @Schema(description = "Resultat beløp") var beløp: BigDecimal?,
)
