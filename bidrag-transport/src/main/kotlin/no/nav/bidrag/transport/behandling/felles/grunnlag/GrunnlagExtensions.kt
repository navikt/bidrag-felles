package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.module.kotlin.treeToValue
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.transport.felles.commonObjectmapper

inline fun <reified T : GrunnlagInnhold> BaseGrunnlag.innholdTilObjekt(): T {
    return commonObjectmapper.treeToValue(innhold)
}

inline fun <reified T : GrunnlagInnhold> List<GrunnlagDto>.innholdTilObjekt(): List<T> = map(BaseGrunnlag::innholdTilObjekt)

fun List<GrunnlagDto>.filtrerBasertPåFremmedReferanse(
    grunnlagType: Grunnlagstype,
    referanse: String = "",
): List<GrunnlagDto> =
    filter { it.type == grunnlagType }
        .filter { referanse.isEmpty() || it.grunnlagsreferanseListe.contains(referanse) }

fun List<GrunnlagDto>.filtrerBasertPåEgenReferanse(
    grunnlagType: Grunnlagstype,
    referanse: String = "",
): List<GrunnlagDto> =
    filter { it.type == grunnlagType }
        .filter { referanse.isEmpty() || referanse == it.referanse }

fun List<GrunnlagDto>.hentInntekter(): List<InntektsrapporteringPeriode> =
    filtrerBasertPåEgenReferanse(
        Grunnlagstype.INNTEKT_RAPPORTERING_PERIODE,
    ).innholdTilObjekt()

data class InnholdMedReferanse<T>(val referanse: String, val innhold: T)

inline fun <reified T : GrunnlagInnhold> List<GrunnlagDto>.filtrerOgKonverterBasertPåEgenReferanse(
    grunnlagType: Grunnlagstype,
    referanse: String = "",
): List<InnholdMedReferanse<T>> =
    filtrerBasertPåEgenReferanse(grunnlagType, referanse)
        .map {
            InnholdMedReferanse(referanse, it.innholdTilObjekt<T>())
        }

inline fun <reified T : GrunnlagInnhold> List<GrunnlagDto>.filtrerOgKonverterBasertPåFremmedReferanse(
    grunnlagType: Grunnlagstype,
    referanse: String = "",
): List<InnholdMedReferanse<T>> =
    filtrerBasertPåFremmedReferanse(grunnlagType, referanse)
        .map {
            InnholdMedReferanse(referanse, it.innholdTilObjekt<T>())
        }
