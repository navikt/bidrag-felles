package no.nav.bidrag.transport.behandling.beregning.felles

import com.fasterxml.jackson.databind.JsonNode
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import no.nav.bidrag.transport.felles.commonObjectmapper

@Schema(description = "Grunnlaget for en beregning av barnebidrag, forskudd og særtilskudd")
data class BeregnGrunnlag(
    @Schema(description = "Beregningsperiode") val periode: ÅrMånedsperiode? = null,
    @Schema(description = "Referanse til Person-objekt som tilhører søknadsbarnet") val søknadsbarnReferanse: String? = null,
    @Schema(description = "Periodisert liste over grunnlagselementer") val grunnlagListe: List<GrunnlagDto>? = null,
)

fun BeregnGrunnlag.valider() {
    requireNotNull(periode) { "beregningsperiode kan ikke være null" }
    requireNotNull(periode.fom) { "beregningsperiode fom kan ikke være null" }
    requireNotNull(periode.til) { "beregningsperiode til kan ikke være null" }
    requireNotNull(søknadsbarnReferanse) { "søknadsbarnReferanse kan ikke være null" }
    requireNotNull(grunnlagListe) { "grunnlagListe kan ikke være null" }
}

data class InnholdMedReferanse<T>(val referanse: String, val innhold: T)

fun <T> BeregnGrunnlag.hentInnholdBasertPåEgenReferanse(
    grunnlagType: Grunnlagstype,
    clazz: Class<T>,
    referanse: String = "",
): List<InnholdMedReferanse<T>> =
    grunnlagListe!!
        .filter { it.type == grunnlagType }
        .filter { referanse.isEmpty() || referanse == it.referanse }
        .map {
            val innhold = commonObjectmapper.treeToValue(it.innhold, clazz)
            InnholdMedReferanse(it.referanse, innhold)
        }

fun <T> BeregnGrunnlag.hentInnholdBasertPåFremmedReferanse(
    grunnlagType: Grunnlagstype,
    clazz: Class<T>,
    referanse: String = "",
): List<InnholdMedReferanse<T>> =
    grunnlagListe!!
        .filter { it.type == grunnlagType }
        .filter { referanse.isEmpty() || it.grunnlagsreferanseListe!!.contains(referanse) }
        .map {
            val innhold = commonObjectmapper.treeToValue(it.innhold, clazz)
            InnholdMedReferanse(it.referanse, innhold)
        }

fun JsonNode.toString() = commonObjectmapper.writeValueAsString(this)
