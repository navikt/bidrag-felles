package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.inntekt.Inntektsrapportering
import no.nav.bidrag.domene.enums.inntekt.Inntektstype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

@Schema(description = "Rapportert inntekt for person")
data class InntektRapporteringPeriode(
    override val periode: ÅrMånedsperiode,
    override val manueltRegistrert: Boolean,
    @Schema(description = "Type inntektsrapportering") val inntektsrapportering: Inntektsrapportering,
    @Schema(description = "Referanse til barnet inntekten gjelder for") val gjelderBarn: String? = null,
    @Schema(description = "Inntekt beløp") val beløp: BigDecimal,
    @Schema(description = "Om inntekt er tatt med i beregningen") val valgt: Boolean,
    @Schema(
        description = "Liste med inntekstposter som inntekten består av. Vil være tom hvis det er manuelt registrert",
    ) val inntekstpostListe: List<InntektPost>,
) : GrunnlagPeriodeInnhold {
    data class InntektPost(
        @Schema(description = "Kode for inntektspost", example = "bonus")
        val kode: String,
        @Schema(description = "Inntekstype inntekstposten er knyttet til", example = "PENSJON")
        val inntekstype: Inntektstype? = null,
        @Schema(description = "Beløp som utgør inntektsposten", example = "60000")
        val beløp: BigDecimal,
    )
}
