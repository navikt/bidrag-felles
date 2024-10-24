package no.nav.bidrag.commons.util

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.node.POJONode
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.enums.vedtak.Beslutningstype
import no.nav.bidrag.domene.enums.vedtak.Innkrevingstype
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.enums.vedtak.Vedtakskilde
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.organisasjon.Enhetsnummer
import no.nav.bidrag.domene.sak.Saksnummer
import no.nav.bidrag.domene.util.visningsnavn
import no.nav.bidrag.domene.util.visningsnavnMedÅrstall
import no.nav.bidrag.transport.behandling.felles.grunnlag.BaseGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.BostatusPeriode
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningBarnIHusstand
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningBidragsevne
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningBidragspliktigesAndel
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningBoforhold
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningSumInntekt
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningUtgift
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningVoksneIHustand
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import no.nav.bidrag.transport.behandling.felles.grunnlag.Grunnlagsreferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.InnhentetHusstandsmedlem
import no.nav.bidrag.transport.behandling.felles.grunnlag.InnhentetSkattegrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.InntektsrapporteringPeriode
import no.nav.bidrag.transport.behandling.felles.grunnlag.NotatGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.Person
import no.nav.bidrag.transport.behandling.felles.grunnlag.SivilstandPeriode
import no.nav.bidrag.transport.behandling.felles.grunnlag.SjablonSjablontallPeriode
import no.nav.bidrag.transport.behandling.felles.grunnlag.SluttberegningForskudd
import no.nav.bidrag.transport.behandling.felles.grunnlag.SluttberegningSærbidrag
import no.nav.bidrag.transport.behandling.felles.grunnlag.erPerson
import no.nav.bidrag.transport.behandling.felles.grunnlag.filtrerBasertPåEgenReferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.filtrerBasertPåFremmedReferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.hentPersonMedReferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.innholdTilObjekt
import no.nav.bidrag.transport.behandling.vedtak.request.OpprettVedtakRequestDto
import no.nav.bidrag.transport.behandling.vedtak.response.BehandlingsreferanseDto
import no.nav.bidrag.transport.behandling.vedtak.response.EngangsbeløpDto
import no.nav.bidrag.transport.behandling.vedtak.response.StønadsendringDto
import no.nav.bidrag.transport.behandling.vedtak.response.VedtakDto
import no.nav.bidrag.transport.behandling.vedtak.response.VedtakPeriodeDto
import no.nav.bidrag.transport.felles.commonObjectmapper
import no.nav.bidrag.transport.felles.toCompactString
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

fun MutableMap<String, MutableList<String>>.merge(with: Map<String, MutableList<String>>) {
    with.forEach { (key, value) ->
        val list = getOrDefault(key, mutableListOf())
        list.addAll(value)
        put(key, list)
    }
}

fun MutableMap<String, MutableList<String>>.add(
    subgraph: String,
    value: String,
) {
    val list = getOrDefault(subgraph, mutableListOf())
    list.add(value)
    put(subgraph, list)
}

enum class MermaidSubgraph {
    STONADSENDRING,
    PERIODE,
    NOTAT,
    SJABLON,
    PERSON,
    INGEN,
    ACTION,
}

enum class TreeChildType {
    FRITTSTÅENDE,
    VEDTAK,
    STØNADSENDRING,
    PERIODE,
    GRUNNLAG,
}

data class TreeChild(
    val name: String,
    val id: String,
    val type: TreeChildType,
    val children: MutableList<TreeChild> = mutableListOf(),
    @JsonIgnore
    val parent: TreeChild?,
    val innhold: POJONode? = null,
) {
    val grunnlagstype get() = grunnlag?.type

    val grunnlag
        get() =
            if (type == TreeChildType.GRUNNLAG) {
                innhold?.let { commonObjectmapper.convertValue(it, GrunnlagDto::class.java) }
            } else {
                null
            }
}

data class TreeVedtak(
    val nodeId: String,
    val kilde: Vedtakskilde,
    val type: Vedtakstype,
    val opprettetAv: String,
    val opprettetAvNavn: String?,
    val kildeapplikasjon: String,
    val vedtakstidspunkt: LocalDateTime,
    val enhetsnummer: Enhetsnummer?,
    val innkrevingUtsattTilDato: LocalDate?,
    val fastsattILand: String?,
    val opprettetTidspunkt: LocalDateTime,
)

data class TreeStønad(
    val nodeId: String,
    val type: Stønadstype,
    val sak: Saksnummer,
    val skyldner: Personident,
    val kravhaver: Personident,
    val mottaker: Personident,
    val førsteIndeksreguleringsår: Int?,
    val innkreving: Innkrevingstype,
    val beslutning: Beslutningstype,
    val omgjørVedtakId: Int?,
    val eksternReferanse: String?,
)

data class TreePeriode(
    val nodeId: String,
    val beløp: BigDecimal?,
    val valutakode: String?,
    val resultatkode: String,
    val delytelseId: String?,
)

data class MermaidResponse(
    val mermaidGraph: String,
    val vedtak: TreeVedtak,
    val stønadsendringer: List<TreeStønad>,
    val perioder: List<TreePeriode>,
    val grunnlagListe: List<BaseGrunnlag>,
)

fun OpprettVedtakRequestDto.toMermaid(): MermaidResponse = tilVedtakDto().toMermaid()

fun OpprettVedtakRequestDto.toTree(): TreeChild = tilVedtakDto().toTree()

fun Map<String, List<String>>.toMermaid(): List<String> {
    val printList = mutableListOf<String>()
    val order = listOf("Delberegning", "Stønadsendring_", "ACTION")
    entries
        .sortedWith { a, b ->
            when {
                order.indexOf(a.key) < order.indexOf(b.key) -> -1
                order.indexOf(a.key) > order.indexOf(b.key) -> 1
                else -> 0
            }
        }.forEach {
            if (it.key != MermaidSubgraph.INGEN.name && it.key != MermaidSubgraph.ACTION.name) {
                printList.add("\tsubgraph ${it.key}\n")
                if (it.key == MermaidSubgraph.SJABLON.name || it.key == MermaidSubgraph.NOTAT.name) {
                    printList.add("\tdirection TB\n")
                }
                printList.addAll(it.value.map { "\t\t$it\n" })
                printList.add("\tend\n")
            } else {
                printList.addAll(it.value.map { "\t$it\n" })
            }
        }
    return printList
}

fun VedtakDto.toMermaid(): MermaidResponse {
    val printList = mutableListOf<String>()
    printList.add("\nflowchart LR\n")
    printList.addAll(
        toTree().toMermaidSubgraphMap().toMermaid().removeDuplicates(),
    )
    return MermaidResponse(
        mermaidGraph = printList.joinToString(""),
        grunnlagListe = grunnlagListe,
        vedtak = toTreeDto(),
        stønadsendringer = stønadsendringListe.map { it.toTreeDto() },
        perioder =
            stønadsendringListe.flatMap { st ->
                st.periodeListe.map {
                    it.toTreeDto(
                        st,
                    )
                }
            },
    )
}

fun TreeChild.tilSubgraph(): String? =
    when (type) {
        TreeChildType.STØNADSENDRING -> "Stønadsendring_${name.removeParanteses()}"
        TreeChildType.PERIODE -> parent?.tilSubgraph()
        TreeChildType.GRUNNLAG ->
            when (grunnlagstype) {
                Grunnlagstype.SJABLON -> MermaidSubgraph.SJABLON.name
                Grunnlagstype.NOTAT -> MermaidSubgraph.NOTAT.name
                Grunnlagstype.SLUTTBEREGNING_FORSKUDD -> parent?.tilSubgraph()
                Grunnlagstype.DELBEREGNING_SUM_INNTEKT -> "Delberegning"
                Grunnlagstype.DELBEREGNING_BARN_I_HUSSTAND -> "Delberegning"
                else -> if (this.name.startsWith("PERSON_")) MermaidSubgraph.PERSON.name else "Delberegning"
            }

        else -> MermaidSubgraph.INGEN.name
    }

fun String.removeParanteses() = this.replace("(", " ").replace(")", "")

val mermaidSkipGrunnlag =
    listOf(
        Grunnlagstype.SJABLON,
        Grunnlagstype.SØKNAD,
        Grunnlagstype.NOTAT,
        Grunnlagstype.VIRKNINGSTIDSPUNKT,
    )

fun TreeChild.toMermaidSubgraphMap(parent: TreeChild? = null): Map<String, MutableList<String>> {
    val mermaidSubgraphMap = mutableMapOf<String, MutableList<String>>()

    if (type == TreeChildType.FRITTSTÅENDE) return emptyMap()
    mermaidSubgraphMap.add(MermaidSubgraph.ACTION.name, "click $id call callback() \"$id\"")
    if (parent != null && !mermaidSkipGrunnlag.contains(grunnlagstype)) {
        if (parent.type == TreeChildType.PERIODE) {
            mermaidSubgraphMap.add(
                parent.tilSubgraph()!!,
                "${parent.id}[[\"${parent.name}\"]] --> $id",
            )
        } else if (type == TreeChildType.GRUNNLAG) {
            if (grunnlag?.erPerson() == true) {
                val subgraph = tilSubgraph()
//                mermaidSubgraphMap.add(
//                    parent.tilSubgraph()!!,
//                    "${parent.id}[\"${parent.name}\"] -.- $id[\"${name}\"]",
//                )
            } else if (grunnlagstype == Grunnlagstype.SLUTTBEREGNING_FORSKUDD) {
                mermaidSubgraphMap.add(
                    tilSubgraph()!!,
                    "${parent.id}[\"${parent.name}\"] -->$id{\"${name}\"}",
                )
            } else if (parent.grunnlagstype == Grunnlagstype.SLUTTBEREGNING_FORSKUDD &&
                (
                    grunnlagstype == Grunnlagstype.DELBEREGNING_SUM_INNTEKT ||
                        grunnlagstype == Grunnlagstype.DELBEREGNING_BARN_I_HUSSTAND
                )
            ) {
                mermaidSubgraphMap.add(
                    parent.tilSubgraph()!!,
                    "${parent.id}[\"${parent.name}\"] -->|\"${name}\"| $id[[\"${name}\"]]",
                )
            } else if (parent.grunnlagstype == Grunnlagstype.DELBEREGNING_SUM_INNTEKT ||
                parent.grunnlagstype == Grunnlagstype.DELBEREGNING_BARN_I_HUSSTAND
            ) {
                mermaidSubgraphMap.add(
                    parent.tilSubgraph()!!,
                    "${parent.id}[[\"${parent.name}\"]] --> $id[\"${name}\"]",
                )
            } else {
                mermaidSubgraphMap.add(
                    parent.tilSubgraph()!!,
                    "${parent.id}[\"${parent.name}\"] --> $id[\"${name}\"]",
                )
            }
        } else {
            mermaidSubgraphMap.add(
                parent.tilSubgraph()!!,
                "${parent.id}[\"${parent.name}\"] --> $id[\"${name}\"]",
            )
        }
    }

    children.forEach { mermaidSubgraphMap.merge(it.toMermaidSubgraphMap(this)) }

    return mermaidSubgraphMap
}

fun List<String>.removeDuplicates(): List<String> {
    val distinctList = mutableListOf<String>()
    val ignoreList = listOf("subgraph", "\tend", "flowchart")
    this.forEach {
        if (ignoreList.any { ignore -> it.contains(ignore) }) {
            distinctList.add(it)
        } else if (!distinctList.contains(it)) {
            distinctList.add(it)
        }
    }
    return distinctList
}

fun VedtakDto.toTree(): TreeChild {
    val vedtakParent =
        TreeChild(
            id = nodeId(),
            name = "Vedtak",
            type = TreeChildType.VEDTAK,
            parent = null,
            innhold =
                POJONode(
                    toTreeDto(),
                ),
        )
    val grunnlagSomIkkeErReferert =
        TreeChild(
            id = "ikke_referert",
            name = "Frittstående(Ikke refert av grunnlag eller stønadsendring)",
            parent = vedtakParent,
            type = TreeChildType.FRITTSTÅENDE,
        )
    vedtakParent.children.add(grunnlagSomIkkeErReferert)
    grunnlagSomIkkeErReferert.children.addAll(
        grunnlagListe
            .filter {
                grunnlagListe.filtrerBasertPåFremmedReferanse(referanse = it.referanse).isEmpty()
            }.filter {
                !stønadsendringListe.flatMap { it.grunnlagReferanseListe }.contains(it.referanse)
            }.filter {
                !stønadsendringListe
                    .flatMap { it.periodeListe.flatMap { it.grunnlagReferanseListe } }
                    .contains(it.referanse)
            }.map {
                it.referanse.toTree(grunnlagListe, vedtakParent)
            }.filterNotNull(),
    )
    stønadsendringListe.forEachIndexed { i, st ->
        val stønadsendringId = st.nodeId()
        val stønadsendringTree =
            TreeChild(
                id = stønadsendringId,
                name = "Stønadsendring Barn ${i + 1}",
                type = TreeChildType.STØNADSENDRING,
                parent = vedtakParent,
                innhold =
                    POJONode(
                        st.toTreeDto(),
                    ),
            )
        vedtakParent.children.add(stønadsendringTree)
        stønadsendringTree.children.addAll(
            st.grunnlagReferanseListe.toTree(
                grunnlagListe,
                stønadsendringTree,
            ),
        )
        st.periodeListe.forEach {
            val periodeTree =
                TreeChild(
                    id = it.nodeId(st),
                    name = "Periode(${it.periode.fom.toCompactString()})",
                    type = TreeChildType.PERIODE,
                    parent = stønadsendringTree,
                    innhold =
                        POJONode(
                            it.toTreeDto(st),
                        ),
                )

            periodeTree.children.addAll(
                it.grunnlagReferanseListe
                    .toTree(
                        grunnlagListe,
                        periodeTree,
                    ).toMutableList(),
            )
            stønadsendringTree.children.add(periodeTree)
        }
    }
    return vedtakParent
}

fun List<Grunnlagsreferanse>.toTree(
    grunnlagsListe: List<BaseGrunnlag>,
    parent: TreeChild?,
): List<TreeChild> =
    map {
        it.toTree(grunnlagsListe, parent)
    }.filterNotNull()

fun Grunnlagsreferanse.toTree(
    grunnlagsListe: List<BaseGrunnlag>,
    parent: TreeChild?,
): TreeChild? {
    val grunnlagListe = grunnlagsListe.filtrerBasertPåEgenReferanse(referanse = this)
    if (grunnlagListe.isEmpty()) {
        return null
    }

    val grunnlag = grunnlagListe.first()
    val treeMap =
        grunnlagListe.flatMap {
            it.grunnlagsreferanseListe.map { it.toTree(grunnlagsListe, parent) } +
                it.gjelderReferanse?.toTree(grunnlagsListe, parent)
        }

    val gjelderGrunnlag = grunnlagsListe.hentPersonMedReferanse(grunnlag.gjelderReferanse)
    val rolleVisningsnavn =
        when (gjelderGrunnlag?.type) {
            Grunnlagstype.PERSON_SØKNADSBARN -> "søknadsbarn"
            Grunnlagstype.PERSON_BIDRAGSPLIKTIG -> "bidragspliktig"
            Grunnlagstype.PERSON_BIDRAGSMOTTAKER -> "bidragsmottaker"
            Grunnlagstype.PERSON_REELL_MOTTAKER -> "reell mottaker"
            Grunnlagstype.PERSON_HUSSTANDSMEDLEM -> "husstandsmedlem"
            else -> ""
        }
    return TreeChild(
        name =
            when (grunnlag.type) {
                Grunnlagstype.SLUTTBEREGNING_FORSKUDD ->
                    "Sluttberegning" +
                        "(${grunnlag.innholdTilObjekt<SluttberegningForskudd>().periode.fom.toCompactString()})"

                Grunnlagstype.SLUTTBEREGNING_SÆRBIDRAG ->
                    "Sluttberegning" +
                        "(${grunnlag.innholdTilObjekt<SluttberegningSærbidrag>().periode.fom.toCompactString()})"

// TODO SJABLON kan mappes til 8 ulike objekter (SjablonSjablontallPeriode, SjablonSamværsfradragPeriode, SjablonTrinnvisSkattesatsPeriode,
// TODO SjablonBarnetilsynPeriode, SjablonForbruksutgifterPeriode, SjablonMaksFradragPeriode, SjablonMaksTilsynPeriode og
// TODO SjablonBidragsevnePeriode)
                Grunnlagstype.SJABLON ->
                    "Sjablon(" +
                        "${grunnlag.innholdTilObjekt<SjablonSjablontallPeriode>().sjablon})"

                Grunnlagstype.DELBEREGNING_SUM_INNTEKT -> {
                    val delberegning = grunnlag.innholdTilObjekt<DelberegningSumInntekt>()
                    "Delberegning sum inntekt(${delberegning.periode.fom.toCompactString()})"
                }

                Grunnlagstype.DELBEREGNING_BARN_I_HUSSTAND -> {
                    val barnIHusstand = grunnlag.innholdTilObjekt<DelberegningBarnIHusstand>()
                    "Delberegning barn i husstand(${barnIHusstand.periode.fom.toCompactString()})"
                }

                Grunnlagstype.INNTEKT_RAPPORTERING_PERIODE -> {
                    val inntekt = grunnlag.innholdTilObjekt<InntektsrapporteringPeriode>()
                    inntekt.inntektsrapportering.visningsnavnMedÅrstall(inntekt.periode.fom.year) +
                        if (inntekt.manueltRegistrert) " (manuelt registrert)" else ""
                }

                Grunnlagstype.SIVILSTAND_PERIODE -> {
                    val sivilstand = grunnlag.innholdTilObjekt<SivilstandPeriode>()
                    "Sivilstand(${sivilstand.sivilstand.visningsnavn.intern}/${sivilstand.periode.fom.toCompactString()})"
                }

                Grunnlagstype.BOSTATUS_PERIODE -> {
                    val bosstatus = grunnlag.innholdTilObjekt<BostatusPeriode>()
                    "Bosstatus(${bosstatus.bostatus.visningsnavn.intern}/${bosstatus.periode.fom.toCompactString()})"
                }

                Grunnlagstype.NOTAT -> "Notat(${grunnlag.innholdTilObjekt<NotatGrunnlag>().type})"
                Grunnlagstype.INNHENTET_HUSSTANDSMEDLEM -> {
                    val innhentetHusstandsmedlem = grunnlag.innholdTilObjekt<InnhentetHusstandsmedlem>()
                    "Innhentet husstandsmedlem(${innhentetHusstandsmedlem.grunnlag.fødselsdato?.toCompactString()})"
                }

                Grunnlagstype.INNHENTET_INNTEKT_SKATTEGRUNNLAG_PERIODE -> {
                    val skattegrunnlag = grunnlag.innholdTilObjekt<InnhentetSkattegrunnlag>()
                    "Innhentet skattegrunnlag(${skattegrunnlag.år})"
                }

                Grunnlagstype.INNHENTET_SIVILSTAND -> {
                    "Innhentet sivilstand (Alle)"
                }

                Grunnlagstype.DELBEREGNING_BIDRAGSEVNE -> {
                    val bidragevne = grunnlag.innholdTilObjekt<DelberegningBidragsevne>()
                    "Delberegning bidragsevne(${bidragevne.periode.fom.toCompactString()})"
                }

                Grunnlagstype.DELBEREGNING_UTGIFT -> {
                    val godkjentBeløp = grunnlag.innholdTilObjekt<DelberegningUtgift>()
                    "Delberegning utgift særbidrag(${godkjentBeløp.periode.fom.toCompactString()})"
                }

                Grunnlagstype.DELBEREGNING_BIDRAGSPLIKTIGES_ANDEL_SÆRBIDRAG -> {
                    @Suppress("ktlint:standard:property-naming")
                    val BPsAndelSærbidrag = grunnlag.innholdTilObjekt<DelberegningBidragspliktigesAndel>()
                    "Delberegning BPs andel særbidrag(${BPsAndelSærbidrag.periode.fom.toCompactString()})"
                }

                Grunnlagstype.DELBEREGNING_VOKSNE_I_HUSSTAND -> {
                    val voksneIHusstand = grunnlag.innholdTilObjekt<DelberegningVoksneIHustand>()
                    "Delberegning voksne i husstand(${voksneIHusstand.periode.fom.toCompactString()})"
                }

                Grunnlagstype.DELBEREGNING_BOFORHOLD -> {
                    val voksneIHusstand = grunnlag.innholdTilObjekt<DelberegningBoforhold>()
                    "Delberegning voksne i husstand(${voksneIHusstand.periode.fom.toCompactString()})"
                }

                else ->
                    if (grunnlag.erPerson()) {
                        "${grunnlag.type}(${grunnlag.innholdTilObjekt<Person>().fødselsdato.toCompactString()})"
                    } else if (grunnlag.type.name.startsWith("INNHENTET")) {
                        val type =
                            when (grunnlag.type) {
                                Grunnlagstype.INNHENTET_ARBEIDSFORHOLD -> "Arbeidforshold"
                                Grunnlagstype.INNHENTET_INNTEKT_SMÅBARNSTILLEGG -> "Småbarnstillegg"
                                Grunnlagstype.INNHENTET_INNTEKT_AINNTEKT -> "Ainntekt"
                                Grunnlagstype.INNHENTET_INNTEKT_UTVIDETBARNETRYGD -> "Utvidetbarneygd"
                                Grunnlagstype.INNHENTET_INNTEKT_KONTANTSTØTTE -> "Kontantstøtte"
                                Grunnlagstype.INNHENTET_INNTEKT_BARNETILSYN -> "Barnetilsyn"
                                Grunnlagstype.INNHENTET_INNTEKT_BARNETILLEGG -> "Barnetillegg"
                                else -> ""
                            }
                        "Innhentet $type($rolleVisningsnavn)"
                    } else {
                        this
                    }
            },
        id = this,
        innhold = POJONode(grunnlag),
        type = TreeChildType.GRUNNLAG,
        parent = parent,
        children = treeMap.filterNotNull().toMutableList(),
    )
}

fun OpprettVedtakRequestDto.tilVedtakDto(): VedtakDto =
    VedtakDto(
        type = type,
        opprettetAv = opprettetAv ?: "",
        opprettetAvNavn = opprettetAv,
        kilde = kilde,
        kildeapplikasjon = "behandling",
        vedtakstidspunkt = vedtakstidspunkt,
        enhetsnummer = enhetsnummer,
        innkrevingUtsattTilDato = innkrevingUtsattTilDato,
        fastsattILand = fastsattILand,
        opprettetTidspunkt = LocalDateTime.now(),
        behandlingsreferanseListe =
            behandlingsreferanseListe.map {
                BehandlingsreferanseDto(
                    kilde = it.kilde,
                    referanse = it.referanse,
                )
            },
        stønadsendringListe =
            stønadsendringListe.map {
                StønadsendringDto(
                    innkreving = it.innkreving,
                    skyldner = it.skyldner,
                    kravhaver = it.kravhaver,
                    mottaker = it.mottaker,
                    sak = it.sak,
                    type = it.type,
                    beslutning = it.beslutning,
                    grunnlagReferanseListe = it.grunnlagReferanseListe,
                    eksternReferanse = it.eksternReferanse,
                    omgjørVedtakId = it.omgjørVedtakId,
                    førsteIndeksreguleringsår = it.førsteIndeksreguleringsår,
                    periodeListe =
                        it.periodeListe.map {
                            VedtakPeriodeDto(
                                periode = it.periode,
                                beløp = it.beløp,
                                valutakode = it.valutakode,
                                resultatkode = it.resultatkode,
                                delytelseId = it.delytelseId,
                                grunnlagReferanseListe = it.grunnlagReferanseListe,
                            )
                        },
                )
            },
        engangsbeløpListe =
            engangsbeløpListe.map {
                EngangsbeløpDto(
                    beløp = it.beløp,
                    valutakode = it.valutakode,
                    resultatkode = it.resultatkode,
                    delytelseId = it.delytelseId,
                    grunnlagReferanseListe = it.grunnlagReferanseListe,
                    beslutning = it.beslutning,
                    innkreving = it.innkreving,
                    skyldner = it.skyldner,
                    kravhaver = it.kravhaver,
                    mottaker = it.mottaker,
                    sak = it.sak,
                    type = it.type,
                    eksternReferanse = it.eksternReferanse,
                    omgjørVedtakId = it.omgjørVedtakId,
                    referanse = it.referanse ?: "",
                )
            },
        grunnlagListe =
            grunnlagListe.map {
                GrunnlagDto(
                    referanse = it.referanse,
                    type = it.type,
                    innhold = it.innhold,
                    grunnlagsreferanseListe = it.grunnlagsreferanseListe,
                    gjelderReferanse = it.gjelderReferanse,
                )
            },
    )

fun VedtakDto.nodeId() = "Vedtak"

fun StønadsendringDto.nodeId() = "Stønadsendring_${type}_${kravhaver.verdi}"

fun VedtakPeriodeDto.nodeId(st: StønadsendringDto) = "Periode${periode.fom.toCompactString()}${st.kravhaver.verdi}"

fun VedtakDto.toTreeDto() =
    TreeVedtak(
        nodeId = nodeId(),
        kilde = kilde,
        type = type,
        opprettetAv = opprettetAv,
        opprettetAvNavn = opprettetAv,
        kildeapplikasjon = kildeapplikasjon,
        vedtakstidspunkt = vedtakstidspunkt,
        enhetsnummer = enhetsnummer,
        innkrevingUtsattTilDato = innkrevingUtsattTilDato,
        fastsattILand = fastsattILand,
        opprettetTidspunkt = opprettetTidspunkt,
    )

fun VedtakPeriodeDto.toTreeDto(st: StønadsendringDto) =
    TreePeriode(
        nodeId = nodeId(st),
        beløp = beløp,
        valutakode = valutakode,
        resultatkode = resultatkode,
        delytelseId = delytelseId,
    )

fun StønadsendringDto.toTreeDto() =
    TreeStønad(
        nodeId = nodeId(),
        type = type,
        sak = sak,
        skyldner = skyldner,
        kravhaver = kravhaver,
        mottaker = mottaker,
        førsteIndeksreguleringsår = førsteIndeksreguleringsår,
        innkreving = innkreving,
        beslutning = beslutning,
        omgjørVedtakId = omgjørVedtakId,
        eksternReferanse = eksternReferanse,
    )
