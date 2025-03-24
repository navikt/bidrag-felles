package no.nav.bidrag.transport.behandling.vedtak.response

import no.nav.bidrag.domene.enums.behandling.TypeBehandling
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.beregning.Resultatkode.Companion.erDirekteAvslag
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.enums.vedtak.BehandlingsrefKilde
import no.nav.bidrag.domene.enums.vedtak.Engangsbeløptype
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.tid.Datoperiode
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningEndringSjekkGrense
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningEndringSjekkGrensePeriode
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import no.nav.bidrag.transport.behandling.felles.grunnlag.VirkningstidspunktGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.filtrerBasertPåEgenReferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.filtrerOgKonverterBasertPåFremmedReferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.finnSluttberegningBarnebidragGrunnlagIReferanser
import no.nav.bidrag.transport.behandling.felles.grunnlag.hentPersonMedIdent
import no.nav.bidrag.transport.behandling.felles.grunnlag.innholdTilObjekt
import java.time.YearMonth

val VedtakDto.saksnummer get() = stønadsendringListe.firstOrNull()?.sak?.verdi ?: engangsbeløpListe.firstOrNull()?.sak?.verdi
val VedtakDto.behandlingId get() =
    behandlingsreferanseListe
        .find {
            it.kilde == BehandlingsrefKilde.BEHANDLING_ID
        }?.referanse
        ?.toLong()

val List<BehandlingsreferanseDto>.søknadsid get() =
    find {
        it.kilde == BehandlingsrefKilde.BISYS_SØKNAD
    }?.referanse
        ?.toLong()

val List<BehandlingsreferanseDto>.søknadKlageRefId get() =
    find {
        it.kilde == BehandlingsrefKilde.BISYS_KLAGE_REF_SØKNAD
    }?.referanse
        ?.toLong()

val VedtakDto.søknadId get() = behandlingsreferanseListe.søknadsid
val VedtakDto.søknadKlageRefId get() = behandlingsreferanseListe.søknadKlageRefId

val VedtakForStønad.søknadsid get() = behandlingsreferanser.søknadsid
val VedtakForStønad.søknadKlageRefId get() = behandlingsreferanser.søknadKlageRefId

val VedtakDto.virkningstidspunkt get() =
    grunnlagListe
        .filtrerBasertPåEgenReferanse(
            Grunnlagstype.VIRKNINGSTIDSPUNKT,
        ).firstOrNull()
        ?.innholdTilObjekt<VirkningstidspunktGrunnlag>()
        ?.virkningstidspunkt

val VedtakDto.særbidragsperiode get() =
    virkningstidspunkt?.let {
        Datoperiode(
            it.withDayOfMonth(1),
            YearMonth.from(it).atEndOfMonth(),
        )
    }
        ?: if (engangsbeløpListe.firstOrNull()?.omgjørVedtakId == null) {
            Datoperiode(YearMonth.from(vedtakstidspunkt).atDay(1), YearMonth.from(vedtakstidspunkt).atEndOfMonth())
        } else {
            null
        }
val VedtakDto.erDirekteAvslag get(): Boolean {
    val virkningstidspunkt =
        grunnlagListe
            .filtrerBasertPåEgenReferanse(
                Grunnlagstype.VIRKNINGSTIDSPUNKT,
            ).firstOrNull()
            ?.innholdTilObjekt<VirkningstidspunktGrunnlag>()
    return virkningstidspunkt?.avslag != null ||
        engangsbeløpListe
            .firstOrNull()
            ?.resultatkode
            ?.let { Resultatkode.fraKode(it)?.erDirekteAvslag() } ?: false
}

fun StønadsendringDto.finnSistePeriode() = periodeListe.maxBy { it.periode.fom }

fun VedtakDto.finnSluttberegningBarnebidragIPeriode(periode: VedtakPeriodeDto) =
    grunnlagListe.finnSluttberegningBarnebidragGrunnlagIReferanser(periode.grunnlagReferanseListe)?.innhold

fun VedtakDto.finnSluttberegningBarnebidragGrunnlagIPeriode(periode: VedtakPeriodeDto) =
    grunnlagListe.finnSluttberegningBarnebidragGrunnlagIReferanser(periode.grunnlagReferanseListe)

val VedtakDto.typeBehandling get() =
    when {
        stønadsendringListe.isNotEmpty() && stønadsendringListe.first().type == Stønadstype.FORSKUDD -> TypeBehandling.FORSKUDD
        engangsbeløpListe.isNotEmpty() && engangsbeløpListe.first().type == Engangsbeløptype.SÆRBIDRAG -> TypeBehandling.SÆRBIDRAG
        else -> TypeBehandling.BIDRAG
    }

fun StønadsendringDto.finnSøknadsbarnReferanse(grunnlagListe: List<GrunnlagDto>): String {
    val kravhaverIdent = kravhaver.verdi
    return grunnlagListe.hentPersonMedIdent(kravhaverIdent)!!.referanse
}

fun List<GrunnlagDto>.erResultatEndringUnderGrense(søknadsbarnReferanse: String): Boolean {
    val delberegningGrense = finnDelberegningSjekkGrense(søknadsbarnReferanse)
    return delberegningGrense?.innhold?.endringErOverGrense == false
}

fun List<GrunnlagDto>.erResultatEndringUnderGrenseForPeriode(periode: ÅrMånedsperiode): Boolean {
    val delberegningGrense = finnDelberegningSjekkGrensePeriode(periode)
    return delberegningGrense?.innhold?.endringErOverGrense == false
}

fun List<GrunnlagDto>.finnDelberegningSjekkGrensePeriode(periode: ÅrMånedsperiode) =
    filtrerOgKonverterBasertPåFremmedReferanse<DelberegningEndringSjekkGrensePeriode>(
        Grunnlagstype.DELBEREGNING_ENDRING_SJEKK_GRENSE_PERIODE,
    ).find { it.innhold.periode == periode }

fun List<GrunnlagDto>.finnDelberegningSjekkGrense(søknadsbarnReferanse: String) =
    filtrerOgKonverterBasertPåFremmedReferanse<DelberegningEndringSjekkGrense>(
        Grunnlagstype.DELBEREGNING_ENDRING_SJEKK_GRENSE,
        gjelderBarnReferanse = søknadsbarnReferanse,
    ).firstOrNull()
