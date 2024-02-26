package no.nav.bidrag.transport.behandling.beregning.felles

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.inntekt.Inntektsrapportering
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningSumInntekt
import no.nav.bidrag.transport.behandling.felles.grunnlag.Grunnlagsreferanse
import java.math.BigDecimal

@Schema(description = "Grunnlag for kategorisering, gruppering og beregning av inntekter")
data class BeregnValgteInntekterGrunnlag(
    @Schema(description = "Beregningsperiode") val periode: ÅrMånedsperiode,
    @Schema(description = "Referanse til barn som det skal beregnes for") val barnReferanseListe: List<Grunnlagsreferanse>,
    @Schema(description = "Referanse til bidragsmottaker") val bidragsmottakerReferanse: Grunnlagsreferanse,
    @Schema(description = "Periodisert liste over inntekter") val grunnlagListe: List<InntektsgrunnlagPeriode> = emptyList(),
)

data class InntektsgrunnlagPeriode(
    @Schema(description = "Perioden inntekten gjelder for") val periode: ÅrMånedsperiode,
    @Schema(description = "Type inntektsrapportering") val inntektsrapportering: Inntektsrapportering,
    @Schema(description = "Referanse til barnet inntekten gjelder for") val gjelderBarn: Grunnlagsreferanse? = null,
    @Schema(description = "Inntekt beløp") val beløp: BigDecimal,
    @Schema(description = "Referanse til den som eier inntekten") val gjelderReferanse: Grunnlagsreferanse,
)

@Schema(description = "Resultatet av kategorisering, gruppering og beregning av inntekter")
data class BeregnValgteInntekterResultat(
    @Schema(description = "Liste over inntekter per barn") val inntektPerBarnListe: List<InntektPerBarn> = emptyList(),
)

@Schema(description = "Periodisert inntekt per barn")
data class InntektPerBarn(
    @Schema(description = "Referanse til barn") val gjelderBarn: Grunnlagsreferanse? = null,
    @Schema(description = "Liste over summerte inntektsperioder") var summertInntektListe: List<DelberegningSumInntekt> = emptyList(),
)
