package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

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

data class ManuellVedtakGrunnlag(
    val vedtaksid: Long,
    val fattetTidspunkt: LocalDateTime,
    val virkningsDato: LocalDate,
    val vedtakstype: Vedtakstype,
    val stønadstype: Stønadstype,
    val egetTiltak: Boolean = false,
    val begrensetRevurdering: Boolean = false,
    val privatAvtale: Boolean = false,
    val resultatkodeSistePeriode: String,
    val resultatSistePeriode: String,
    val manglerGrunnlag: Boolean = false,
) : GrunnlagInnhold

data class AldersjusteringDetaljerGrunnlag(
    override val periode: ÅrMånedsperiode,
    override val manueltRegistrert: Boolean = false,
    val grunnlagFraVedtak: Long? = null,
    val aldersjustert: Boolean = true,
    @Schema(description = "Er sann hvis automatiske løsningen ikke kunne aldersjustere og det må utføres manuelt")
    val aldersjusteresManuelt: Boolean = false,
    @Schema(description = "Er sann hvis aldersjustering er gjort manuelt")
    val aldersjustertManuelt: Boolean = false,
    val begrunnelser: List<String>? = null,
) : GrunnlagPeriodeInnhold {
    @JsonIgnore
    val begrunnelserVisningsnavn: String? =
        begrunnelser
            ?.joinToString(", ") {
                it.lowercase().replace("_", " ")
            }?.replaceFirstChar { fc -> fc.uppercase() }

    @JsonIgnore
    val førsteBegrunnelseVisningsnavn: String? =
        begrunnelser
            ?.map {
                it.lowercase().replaceFirstChar { fc -> fc.uppercase() }.replace("_", " ")
            }?.firstOrNull()
}
