package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.treeToValue
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.transport.felles.commonObjectmapper

inline fun <reified T : List<GrunnlagInnhold>> BaseGrunnlag.innholdTilObjektListe(): T =
    try {
        commonObjectmapper.treeToValue(innhold)
    } catch (e: Exception) {
        commonObjectmapper.readValue(commonObjectmapper.writeValueAsString(innhold))
    }

inline fun <reified T : GrunnlagInnhold> BaseGrunnlag.innholdTilObjekt(): T =
    try {
        commonObjectmapper.treeToValue(innhold)
    } catch (e: Exception) {
        commonObjectmapper.readValue(commonObjectmapper.writeValueAsString(innhold))
    }

inline fun <reified T : GrunnlagInnhold> List<BaseGrunnlag>.innholdTilObjekt(): List<T> = map(BaseGrunnlag::innholdTilObjekt)

inline fun <reified T : List<GrunnlagInnhold>> List<BaseGrunnlag>.innholdTilObjektListe(): List<T> =
    map(BaseGrunnlag::innholdTilObjektListe)

fun List<BaseGrunnlag>.finnGrunnlagSomErReferertFraGrunnlagsreferanseListe(
    type: Grunnlagstype,
    grunnlagsreferanseListe: List<Grunnlagsreferanse>,
): Set<BaseGrunnlag> {
    val grunnlag = filtrerBasertPåEgenReferanser(type, grunnlagsreferanseListe)
    if (grunnlag.isEmpty()) {
        return grunnlagsreferanseListe
            .flatMap { referanse ->
                filtrerBasertPåEgenReferanse(null, referanse)
                    .flatMap {
                        finnGrunnlagSomErReferertAv(type, it)
                    }
            }.toSet()
    }
    return grunnlag.toSet()
}

fun List<BaseGrunnlag>.finnGrunnlagSomErReferertAv(
    type: Grunnlagstype,
    fraGrunnlag: BaseGrunnlag,
): Set<BaseGrunnlag> {
    val grunnlag = filtrerBasertPåEgenReferanser(type, fraGrunnlag.grunnlagsreferanseListe)
    if (grunnlag.isEmpty()) {
        return fraGrunnlag.grunnlagsreferanseListe
            .flatMap { referanse ->
                filtrerBasertPåEgenReferanse(null, referanse)
                    .flatMap {
                        finnGrunnlagSomErReferertAv(type, it)
                    }
            }.toSet()
    }
    return grunnlag.toSet()
}

fun List<BaseGrunnlag>.filtrerBasertPåEgenReferanser(
    type: Grunnlagstype,
    referanser: List<Grunnlagsreferanse>,
): List<BaseGrunnlag> =
    filtrerBasertPåEgenReferanse(type)
        .filter { referanser.contains(it.referanse) }

fun List<BaseGrunnlag>.filtrerBasertPåFremmedReferanse(
    grunnlagType: Grunnlagstype? = null,
    referanse: String? = null,
    gjelderBarnReferanse: String? = null,
): List<BaseGrunnlag> =
    filter { grunnlagType == null || it.type == grunnlagType }
        .filter {
            referanse.isNullOrEmpty() &&
                gjelderBarnReferanse.isNullOrEmpty() ||
                it.grunnlagsreferanseListe.contains(referanse) ||
                (referanse.isNullOrEmpty() || referanse == it.gjelderReferanse) &&
                (gjelderBarnReferanse.isNullOrEmpty() || gjelderBarnReferanse == it.gjelderBarnReferanse)
        }

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

data class InnholdMedReferanse<T>(
    val referanse: String,
    val gjelderBarnReferanse: String? = null,
    val gjelderReferanse: String? = null,
    val innhold: T,
    val grunnlag: BaseGrunnlag,
)

inline fun <reified T : GrunnlagInnhold> List<BaseGrunnlag>.finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe(
    type: Grunnlagstype,
    grunnlagsreferanseListe: List<Grunnlagsreferanse>,
): List<InnholdMedReferanse<T>> =
    finnGrunnlagSomErReferertFraGrunnlagsreferanseListe(type, grunnlagsreferanseListe).map {
        it.tilInnholdMedReferanse<T>()
    }

inline fun <reified T : GrunnlagInnhold> List<BaseGrunnlag>.finnOgKonverterGrunnlagSomErReferertAv(
    type: Grunnlagstype,
    fraGrunnlag: BaseGrunnlag,
): List<InnholdMedReferanse<T>> =
    finnGrunnlagSomErReferertAv(type, fraGrunnlag).map {
        it.tilInnholdMedReferanse<T>()
    }

inline fun <reified T : GrunnlagInnhold> List<BaseGrunnlag>.filtrerOgKonverterBasertPåEgenReferanse(
    grunnlagType: Grunnlagstype? = null,
    referanse: String = "",
): List<InnholdMedReferanse<T>> =
    filtrerBasertPåEgenReferanse(grunnlagType, referanse)
        .map {
            it.tilInnholdMedReferanse<T>()
        }

inline fun <reified T : GrunnlagInnhold> List<BaseGrunnlag>.filtrerOgKonverterBasertPåFremmedReferanse(
    grunnlagType: Grunnlagstype? = null,
    referanse: String? = null,
    gjelderBarnReferanse: String? = null,
): List<InnholdMedReferanse<T>> =
    filtrerBasertPåFremmedReferanse(grunnlagType, referanse, gjelderBarnReferanse)
        .map {
            it.tilInnholdMedReferanse<T>()
        }

fun Rolletype.tilGrunnlagstype() =
    when (this) {
        Rolletype.BIDRAGSPLIKTIG -> Grunnlagstype.PERSON_BIDRAGSPLIKTIG
        Rolletype.BARN -> Grunnlagstype.PERSON_SØKNADSBARN
        Rolletype.BIDRAGSMOTTAKER -> Grunnlagstype.PERSON_BIDRAGSMOTTAKER
        Rolletype.REELMOTTAKER -> Grunnlagstype.PERSON_REELL_MOTTAKER
        else -> throw RuntimeException("Mangler grunnlagsmapping for rolletype $this")
    }

// Hjelpefunksjoner for personobjekter

fun Collection<BaseGrunnlag>.hentAllePersoner(): Collection<BaseGrunnlag> = filter { it.erPerson() }

fun BaseGrunnlag.erPerson(): Boolean = type.name.startsWith("PERSON_")

val BaseGrunnlag.personObjekt get() = commonObjectmapper.treeToValue(innhold, Person::class.java)!!
val BaseGrunnlag.personIdent get() = personObjekt.ident?.verdi
val Collection<BaseGrunnlag>.bidragspliktig
    get() =
        find { it.type == Grunnlagstype.PERSON_BIDRAGSPLIKTIG }

val Collection<BaseGrunnlag>.bidragsmottaker
    get() =
        find { it.type == Grunnlagstype.PERSON_BIDRAGSMOTTAKER }
val Collection<BaseGrunnlag>.barn
    get() =
        filter {
            it.type == Grunnlagstype.PERSON_SØKNADSBARN || it.type == Grunnlagstype.PERSON_HUSSTANDSMEDLEM
        }.toSet()

val Collection<BaseGrunnlag>.søknadsbarn
    get() =
        filter {
            it.type == Grunnlagstype.PERSON_SØKNADSBARN
        }.toSet()

fun Collection<GrunnlagDto>.hentPerson(ident: String?) = filter { it.erPerson() }.find { it.personIdent == ident }

fun Collection<BaseGrunnlag>.hentPersonMedIdent(ident: String?) = hentAllePersoner().find { it.personIdent == ident }

fun Collection<BaseGrunnlag>.hentPersonMedReferanse(referanse: Grunnlagsreferanse?) =
    referanse?.let {
        toList()
            .filtrerBasertPåEgenReferanse(referanse = referanse)
            .firstOrNull()
    }

// Særbidrag grunnlag
val List<BaseGrunnlag>.særbidragskategori get() =
    filtrerBasertPåEgenReferanse(Grunnlagstype.SÆRBIDRAG_KATEGORI).firstOrNull()?.innholdTilObjekt<SærbidragskategoriGrunnlag>()

val List<BaseGrunnlag>.utgiftDirekteBetalt get() =
    filtrerBasertPåEgenReferanse(
        Grunnlagstype.UTGIFT_DIREKTE_BETALT,
    ).firstOrNull()?.innholdTilObjekt<UtgiftDirekteBetaltGrunnlag>()

val List<BaseGrunnlag>.utgiftMaksGodkjentBeløp get() =
    filtrerBasertPåEgenReferanse(
        Grunnlagstype.UTGIFT_MAKS_GODKJENT_BELØP,
    ).firstOrNull()?.innholdTilObjekt<UtgiftMaksGodkjentBeløpGrunnlag>()

val List<BaseGrunnlag>.utgiftsposter get() =
    filtrerBasertPåEgenReferanse(
        Grunnlagstype.UTGIFTSPOSTER,
    ).firstOrNull()?.innholdTilObjektListe<List<UtgiftspostGrunnlag>>() ?: emptyList()

val List<BaseGrunnlag>.delberegningSamværsklasse get() =
    find {
        it.type == Grunnlagstype.DELBEREGNING_SAMVÆRSKLASSE
    }!!
        .innholdTilObjekt<DelberegningSamværsklasse>()

fun List<BaseGrunnlag>.finnSluttberegningBarnebidragGrunnlagIReferanser(grunnlagsreferanseListe: List<Grunnlagsreferanse>) =
    finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<SluttberegningBarnebidrag>(
        Grunnlagstype.SLUTTBEREGNING_BARNEBIDRAG,
        grunnlagsreferanseListe,
    ).firstOrNull()

inline fun <reified T : GrunnlagInnhold> BaseGrunnlag.tilInnholdMedReferanse() =
    InnholdMedReferanse(
        referanse,
        gjelderBarnReferanse,
        gjelderReferanse,
        innholdTilObjekt<T>(),
        this,
    )
