package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.module.kotlin.treeToValue
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.transport.felles.commonObjectmapper

inline fun <reified T : GrunnlagInnhold> BaseGrunnlag.innholdTilObjekt(): T {
    return commonObjectmapper.treeToValue(innhold)
}

inline fun <reified T : GrunnlagInnhold> List<BaseGrunnlag>.innholdTilObjekt(): List<T> = map(BaseGrunnlag::innholdTilObjekt)

fun List<BaseGrunnlag>.hentAllePersoner(): List<BaseGrunnlag> = filter { it.erPerson() }

fun BaseGrunnlag.erPerson(): Boolean = type.name.startsWith("PERSON_")

fun List<BaseGrunnlag>.filtrerBasertPåFremmedReferanse(
    grunnlagType: Grunnlagstype? = null,
    referanse: String = "",
): List<BaseGrunnlag> =
    filter { grunnlagType == null || it.type == grunnlagType }
        .filter { referanse.isEmpty() || it.grunnlagsreferanseListe.contains(referanse) || referanse == it.gjelderReferanse }

fun List<BaseGrunnlag>.filtrerBasertPåEgenReferanse(
    grunnlagType: Grunnlagstype? = null,
    referanse: String = "",
): List<BaseGrunnlag> =
    filter { grunnlagType == null || it.type == grunnlagType }
        .filter { referanse.isEmpty() || referanse == it.referanse }

fun List<BaseGrunnlag>.hentInntekter(): List<InntektsrapporteringPeriode> =
    filtrerBasertPåEgenReferanse(
        Grunnlagstype.INNTEKT_RAPPORTERING_PERIODE,
    ).innholdTilObjekt()

data class InnholdMedReferanse<T>(val referanse: String, val innhold: T)

inline fun <reified T : GrunnlagInnhold> List<BaseGrunnlag>.filtrerOgKonverterBasertPåEgenReferanse(
    grunnlagType: Grunnlagstype? = null,
    referanse: String = "",
): List<InnholdMedReferanse<T>> =
    filtrerBasertPåEgenReferanse(grunnlagType, referanse)
        .map {
            InnholdMedReferanse(it.referanse, it.innholdTilObjekt<T>())
        }

inline fun <reified T : GrunnlagInnhold> List<BaseGrunnlag>.filtrerOgKonverterBasertPåFremmedReferanse(
    grunnlagType: Grunnlagstype? = null,
    referanse: String = "",
): List<InnholdMedReferanse<T>> =
    filtrerBasertPåFremmedReferanse(grunnlagType, referanse)
        .map {
            InnholdMedReferanse(it.referanse, it.innholdTilObjekt<T>())
        }

fun Rolletype.tilGrunnlagstype() =
    when (this) {
        Rolletype.BIDRAGSPLIKTIG -> Grunnlagstype.PERSON_BIDRAGSPLIKTIG
        Rolletype.BARN -> Grunnlagstype.PERSON_SØKNADSBARN
        Rolletype.BIDRAGSMOTTAKER -> Grunnlagstype.PERSON_BIDRAGSMOTTAKER
        Rolletype.REELMOTTAKER -> Grunnlagstype.PERSON_REELL_MOTTAKER
        else -> throw RuntimeException("Mangler grunnlagsmapping for rolletype $this")
    }
