package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.annotation.JsonIgnore
import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

data class KopiDelberegningUnderholdskostnad(
    override val periode: ÅrMånedsperiode,
    override val fraVedtakId: Long,
    val nettoTilsynsutgift: BigDecimal?,
    val barnetilsynMedStønad: BigDecimal?,
) : GrunnlagPeriodeInnholdKopi

data class KopiDelberegningBidragspliktigesAndel(
    override val periode: ÅrMånedsperiode,
    override val fraVedtakId: Long,
    val endeligAndelFaktor: BigDecimal,
) : GrunnlagPeriodeInnholdKopi

data class KopiSamværsperiodeGrunnlag(
    override val periode: ÅrMånedsperiode,
    override val fraVedtakId: Long,
    val samværsklasse: Samværsklasse,
) : GrunnlagPeriodeInnholdKopi

data class AldersjusteringDetaljerGrunnlag(
    val grunnlagFraVedtak: Long,
    val aldersjustert: Boolean = true,
    val aldersjusteresManuelt: Boolean = false,
    val begrunnelser: List<String>? = null,
) : GrunnlagInnhold {
    @JsonIgnore
    val begrunnelserVisningsnavn: String? =
        begrunnelser?.joinToString(", ") {
            it.lowercase().replaceFirstChar { fc -> fc.uppercase() }.replace("_", " ")
        }

    @JsonIgnore
    val førsteBegrunnelseVisningsnavn: String? =
        begrunnelser
            ?.map {
                it.lowercase().replaceFirstChar { fc -> fc.uppercase() }.replace("_", " ")
            }?.firstOrNull()
}
