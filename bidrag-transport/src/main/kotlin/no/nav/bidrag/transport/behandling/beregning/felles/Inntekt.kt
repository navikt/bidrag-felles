package no.nav.bidrag.transport.behandling.beregning.felles

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.inntekt.Inntektsrapportering
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningSumInntekt
import java.math.BigDecimal

@Schema(description = "Grunnlag for kategorisering, gruppering og beregning av inntekter")
data class BeregnValgteInntekterGrunnlag(
    @Schema(description = "Beregningsperiode") val periode: ÅrMånedsperiode,
    @Schema(description = "Om beregning skal opphøre siste periode") val opphørSistePeriode: Boolean = false,
    @Schema(description = "Liste over identer til barn som det skal beregnes for") val barnIdentListe: List<Personident>,
    @Schema(description = "Ident til person som inntekter skal beregnes for") val gjelderIdent: Personident,
    @Schema(description = "Periodisert liste over inntekter") val grunnlagListe: List<InntektsgrunnlagPeriode> = emptyList(),
)

data class InntektsgrunnlagPeriode(
    @Schema(description = "Perioden inntekten gjelder for") val periode: ÅrMånedsperiode,
    @Schema(description = "Type inntektsrapportering") val inntektsrapportering: Inntektsrapportering,
    @Schema(description = "Ident til barnet inntekten gjelder for") val inntektGjelderBarnIdent: Personident? = null,
    @Schema(description = "Inntekt beløp") val beløp: BigDecimal,
    @Schema(description = "Ident til den som eier inntekten") val inntektEiesAvIdent: Personident,
)

@Schema(description = "Resultatet av kategorisering, gruppering og beregning av inntekter")
data class BeregnValgteInntekterResultat(
    @Schema(description = "Liste over inntekter per barn") val inntektPerBarnListe: List<InntektPerBarn> = emptyList(),
)

@Schema(description = "Periodisert inntekt per barn")
data class InntektPerBarn(
    @Schema(description = "Referanse til barn") val inntektGjelderBarnIdent: Personident? = null,
    @Schema(description = "Liste over summerte inntektsperioder") var summertInntektListe: List<DelberegningSumInntekt> = emptyList(),
)
