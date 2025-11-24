package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.databind.node.POJONode
import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.dokumentmaler.DokumentmalSluttberegningBarnebidragDetaljer
import java.math.BigDecimal

fun List<GrunnlagDto>.finnDelberegningBidragspliktigesAndelSærbidrag(
    grunnlagsreferanseListe: List<Grunnlagsreferanse>,
): DelberegningBidragspliktigesAndel? {
    val sluttberegning = finnSluttberegningIReferanser(grunnlagsreferanseListe) ?: return null
    val delberegningBidragspliktigesAndel =
        find {
            it.type == Grunnlagstype.DELBEREGNING_BIDRAGSPLIKTIGES_ANDEL &&
                sluttberegning.grunnlagsreferanseListe.contains(
                    it.referanse,
                )
        } ?: return null
    return delberegningBidragspliktigesAndel.innholdTilObjekt<DelberegningBidragspliktigesAndel>()
}

fun List<GrunnlagDto>.finnSluttberegningIReferanser(grunnlagsreferanseListe: List<Grunnlagsreferanse>) =
    find {
        listOf(
            Grunnlagstype.SLUTTBEREGNING_FORSKUDD,
            Grunnlagstype.SLUTTBEREGNING_SÆRBIDRAG,
            Grunnlagstype.SLUTTBEREGNING_BARNEBIDRAG,
            Grunnlagstype.SLUTTBEREGNING_BARNEBIDRAG_ALDERSJUSTERING,
            Grunnlagstype.SLUTTBEREGNING_INDEKSREGULERING,
        ).contains(it.type) &&
            grunnlagsreferanseListe.contains(it.referanse)
    }

fun List<GrunnlagDto>.finnTotalInntektForRolleEllerIdent(
    grunnlagsreferanseListe: List<Grunnlagsreferanse>,
    rolletype: Rolletype? = null,
    personIdent: String? = null,
): BigDecimal {
    val sluttberegning =
        finnSluttberegningIReferanser(grunnlagsreferanseListe)
            ?: return BigDecimal.ZERO
    val gjelderReferanse =
        hentAllePersoner()
            .find {
                (rolletype == null || it.type == rolletype.tilGrunnlagstype()) &&
                    (personIdent == null || it.personIdent == personIdent)
            }?.referanse
    val delberegningSumInntekter = finnGrunnlagSomErReferertAv(Grunnlagstype.DELBEREGNING_SUM_INNTEKT, sluttberegning)
    val delberegningSumInntektForRolle =
        if (gjelderReferanse.isNullOrEmpty()) {
            delberegningSumInntekter.firstOrNull()
        } else {
            delberegningSumInntekter.find {
                it.gjelderReferanse ==
                    gjelderReferanse
            }
        }
    return delberegningSumInntektForRolle?.innholdTilObjekt<DelberegningSumInntekt>()?.totalinntekt
        ?: BigDecimal.ZERO
}

fun GrunnlagDto.sluttberegningPeriode(): ÅrMånedsperiode =
    if (erSluttberegningGammelStruktur()) {
        val sluttberegningObjekt = innholdTilObjekt<SluttberegningBarnebidrag>()
        sluttberegningObjekt.periode
    } else {
        val sluttberegningObjekt = innholdTilObjekt<SluttberegningBarnebidragV2>()
        sluttberegningObjekt.periode
    }

fun GrunnlagDto.hentBeregnetBeløp(): BigDecimal =
    if (erSluttberegningNyStruktur()) {
        innholdTilObjekt<SluttberegningBarnebidragV2>().beregnetBeløp ?: BigDecimal.ZERO
    } else {
        innholdTilObjekt<SluttberegningBarnebidrag>().beregnetBeløp ?: BigDecimal.ZERO
    }

fun GrunnlagDto.hentResultatBeløp(): BigDecimal =
    if (erSluttberegningNyStruktur()) {
        innholdTilObjekt<SluttberegningBarnebidragV2>().resultatBeløp!!
    } else {
        innholdTilObjekt<SluttberegningBarnebidrag>().resultatBeløp!!
    }

fun GrunnlagDto.erSluttberegningNyStruktur(): Boolean =
    type == Grunnlagstype.SLUTTBEREGNING_BARNEBIDRAG && !erSluttberegningGammelStruktur()

fun GrunnlagDto.erSluttberegningGammelStruktur(): Boolean =
    type == Grunnlagstype.SLUTTBEREGNING_BARNEBIDRAG &&
        if (innhold is POJONode) {
            innhold.pojo is SluttberegningBarnebidrag
        } else {
            (
                innhold.has("bruttoBidragEtterBarnetilleggBM") ||
                    innhold.has("bruttoBidragJustertForEvneOg25Prosent")
            )
        }

fun List<GrunnlagDto>.finnSamværsklasse(sluttberegningGrunnlag: GrunnlagDto): Samværsklasse =
    finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<SamværsperiodeGrunnlag>(
        Grunnlagstype.SAMVÆRSPERIODE,
        sluttberegningGrunnlag.grunnlagsreferanseListe,
    ).first().innhold.samværsklasse

fun List<GrunnlagDto>.finnBidragTilFordeling(sluttberegningGrunnlag: GrunnlagDto): BigDecimal {
    if (sluttberegningGrunnlag.erSluttberegningGammelStruktur()) {
        val sluttberegningObjekt = sluttberegningGrunnlag.innholdTilObjekt<SluttberegningBarnebidrag>()
        return sluttberegningObjekt.bruttoBidragEtterBarnetilleggBM
    }
    return finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<DelberegningBidragTilFordeling>(
        Grunnlagstype.DELBEREGNING_BIDRAG_TIL_FORDELING,
        sluttberegningGrunnlag.grunnlagsreferanseListe,
    ).first().innhold.bidragTilFordeling
}

fun List<GrunnlagDto>.finnBidragJustertForBarnetilleggBP(sluttberegningGrunnlag: GrunnlagDto): BigDecimal {
    if (sluttberegningGrunnlag.erSluttberegningGammelStruktur()) {
        val sluttberegningObjekt = sluttberegningGrunnlag.innholdTilObjekt<SluttberegningBarnebidrag>()
        return sluttberegningObjekt.bruttoBidragEtterBarnetilleggBP
    }
    return finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<DelberegningBidragJustertForBPBarnetillegg>(
        Grunnlagstype.DELBEREGNING_BIDRAG_JUSTERT_FOR_BP_BARNETILLEGG,
        sluttberegningGrunnlag.grunnlagsreferanseListe,
    ).firstOrNull()?.innhold?.bidragJustertForNettoBarnetilleggBP ?: BigDecimal.ZERO
}

fun List<GrunnlagDto>.byggSluttberegningBarnebidragDetaljer(
    grunnlagsreferanseListe: List<Grunnlagsreferanse>,
): DokumentmalSluttberegningBarnebidragDetaljer? {
    val sluttberegning = finnSluttberegningIReferanser(grunnlagsreferanseListe) ?: return null
    if (sluttberegning.erSluttberegningGammelStruktur()) {
        val sbInnhold = sluttberegning.innholdTilObjekt<SluttberegningBarnebidrag>()
        return DokumentmalSluttberegningBarnebidragDetaljer(
            bidragJustertNedTilEvne = sbInnhold.bidragJustertNedTilEvne,
            nettoBidragEtterSamværsfradrag = sbInnhold.nettoBidragEtterSamværsfradrag,
            uMinusNettoBarnetilleggBM = sbInnhold.uMinusNettoBarnetilleggBM,
            bpAndelAvUVedDeltBostedBeløp = sbInnhold.bpAndelAvUVedDeltBostedBeløp,
            bpAndelAvUVedDeltBostedFaktor = sbInnhold.bpAndelAvUVedDeltBostedFaktor,
            bidragJustertForNettoBarnetilleggBP = sbInnhold.bidragJustertForNettoBarnetilleggBP,
            bruttoBidragEtterBarnetilleggBP = sbInnhold.bruttoBidragEtterBarnetilleggBP,
            bidragJustertNedTil25ProsentAvInntekt = sbInnhold.bidragJustertNedTil25ProsentAvInntekt,
            bruttoBidragJustertForEvneOg25Prosent = sbInnhold.bruttoBidragJustertForEvneOg25Prosent,
            barnetErSelvforsørget = sbInnhold.barnetErSelvforsørget,
            beregnetBeløp = sbInnhold.beregnetBeløp,
            resultatBeløp = sbInnhold.resultatBeløp,
            nettoBidragEtterBarnetilleggBM = sbInnhold.nettoBidragEtterBarnetilleggBM,
            bruttoBidragEtterBarnetilleggBM = sbInnhold.bruttoBidragEtterBarnetilleggBM,
            bidragJustertForNettoBarnetilleggBM = sbInnhold.bidragJustertForNettoBarnetilleggBM,
            resultat = resultatSluttberegning(grunnlagsreferanseListe),
            resultatVisningsnavn = sbInnhold.resultatVisningsnavn,
        )
    }
    val beregnetBeløp = sluttberegning.hentBeregnetBeløp()
    val resultatBeløp = sluttberegning.hentResultatBeløp()
    val andelDeltBosted =
        finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<DelberegningBidragspliktigesAndelDeltBosted>(
            Grunnlagstype.DELBEREGNING_BIDRAGSPLIKTIGES_ANDEL_DELT_BOSTED,
            sluttberegning.grunnlagsreferanseListe,
        ).firstOrNull()

    val bidragTilFordeling =
        finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<DelberegningBidragTilFordeling>(
            Grunnlagstype.DELBEREGNING_BIDRAG_TIL_FORDELING,
            sluttberegning.grunnlagsreferanseListe,
        ).firstOrNull() ?: return null
    val andelAvBidragsevne =
        finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<DelberegningAndelAvBidragsevne>(
            Grunnlagstype.DELBEREGNING_ANDEL_AV_BIDRAGSEVNE,
            sluttberegning.grunnlagsreferanseListe,
        ).firstOrNull() ?: return null
    val bpsBarnetillegg =
        finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<DelberegningBidragJustertForBPBarnetillegg>(
            Grunnlagstype.DELBEREGNING_BIDRAG_JUSTERT_FOR_BP_BARNETILLEGG,
            sluttberegning.grunnlagsreferanseListe,
        ).firstOrNull() ?: return null
    val bpsAndel =
        finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<DelberegningBidragspliktigesAndel>(
            Grunnlagstype.DELBEREGNING_BIDRAGSPLIKTIGES_ANDEL,
            sluttberegning.grunnlagsreferanseListe,
        ).firstOrNull() ?: return null
    val samværsfradrag =
        finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<DelberegningSamværsfradrag>(
            Grunnlagstype.DELBEREGNING_SAMVÆRSFRADRAG,
            sluttberegning.grunnlagsreferanseListe,
        ).firstOrNull() ?: return null

    val nettoBidragEtterBarnetilleggBM = bidragTilFordeling.innhold.bidragTilFordeling.subtract(samværsfradrag.innhold.beløp)

    val bidragJustertNedTil25ProsentAvInntekt = erBidragJustertNedTil25ProsentAvInntekt(grunnlagsreferanseListe)
    return DokumentmalSluttberegningBarnebidragDetaljer(
        bidragJustertNedTilEvne = !andelAvBidragsevne.innhold.harBPFullEvne,
        nettoBidragEtterSamværsfradrag = beregnetBeløp ?: BigDecimal.ZERO,
        uMinusNettoBarnetilleggBM = bidragTilFordeling.innhold.uMinusNettoBarnetilleggBM,
        bpAndelAvUVedDeltBostedBeløp = andelDeltBosted?.innhold?.bpAndelAvUVedDeltBostedBeløp ?: BigDecimal.ZERO,
        bpAndelAvUVedDeltBostedFaktor = andelDeltBosted?.innhold?.bpAndelAvUVedDeltBostedFaktor ?: BigDecimal.ZERO,
        bidragJustertForNettoBarnetilleggBP = bpsBarnetillegg.innhold.erBidragJustertTilNettoBarnetilleggBP,
        bruttoBidragEtterBarnetilleggBP = bpsBarnetillegg.innhold.bidragJustertForNettoBarnetilleggBP,
        bidragJustertNedTil25ProsentAvInntekt = bidragJustertNedTil25ProsentAvInntekt,
        bruttoBidragJustertForEvneOg25Prosent = andelAvBidragsevne.innhold.bruttoBidragJustertForEvneOg25Prosent,
        barnetErSelvforsørget = bpsAndel.innhold.barnetErSelvforsørget,
        beregnetBeløp = beregnetBeløp,
        resultatBeløp = resultatBeløp,
        nettoBidragEtterBarnetilleggBM = nettoBidragEtterBarnetilleggBM,
        bruttoBidragEtterBarnetilleggBM = bidragTilFordeling.innhold.bidragTilFordeling,
        bidragJustertForNettoBarnetilleggBM = bidragTilFordeling.innhold.uMinusNettoBarnetilleggBM == nettoBidragEtterBarnetilleggBM,
        resultat = resultatSluttberegning(grunnlagsreferanseListe),
        resultatVisningsnavn = tilResultatVisningsnavn(grunnlagsreferanseListe),
    )
}
