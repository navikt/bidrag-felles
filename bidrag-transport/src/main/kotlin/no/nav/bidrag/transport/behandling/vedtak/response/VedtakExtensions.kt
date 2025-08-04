package no.nav.bidrag.transport.behandling.vedtak.response

import no.nav.bidrag.domene.enums.behandling.TypeBehandling
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.beregning.Resultatkode.Companion.erDirekteAvslag
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.enums.vedtak.BehandlingsrefKilde
import no.nav.bidrag.domene.enums.vedtak.Beslutningstype
import no.nav.bidrag.domene.enums.vedtak.Engangsbeløptype
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.sak.Stønadsid
import no.nav.bidrag.domene.tid.Datoperiode
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.AldersjusteringDetaljerGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningEndringSjekkGrense
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningEndringSjekkGrensePeriode
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import no.nav.bidrag.transport.behandling.felles.grunnlag.Grunnlagsreferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.InnholdMedReferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.ResultatFraVedtakGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.SluttberegningBarnebidragAldersjustering
import no.nav.bidrag.transport.behandling.felles.grunnlag.VirkningstidspunktGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.filtrerBasertPåEgenReferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.filtrerOgKonverterBasertPåEgenReferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.filtrerOgKonverterBasertPåFremmedReferanse
import no.nav.bidrag.transport.behandling.felles.grunnlag.finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe
import no.nav.bidrag.transport.behandling.felles.grunnlag.finnSluttberegningBarnebidragGrunnlagIReferanser
import no.nav.bidrag.transport.behandling.felles.grunnlag.hentAldersjusteringDetaljerGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.hentPersonMedIdent
import no.nav.bidrag.transport.behandling.felles.grunnlag.hentPersonMedIdentKonvertert
import no.nav.bidrag.transport.behandling.felles.grunnlag.hentPersonMedReferanseKonvertert
import no.nav.bidrag.transport.behandling.felles.grunnlag.innholdTilObjekt
import no.nav.bidrag.transport.felles.tilVisningsnavn
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

fun VedtakDto.tilBatchHendelseResultattekst(): String {
    val vedtakstype = type.name.lowercase().replaceFirstChar { it.uppercase() }
    val stønadstype =
        stønadsendringListe
            .firstOrNull()
            ?.type
            ?.name
            ?.lowercase()
    return "$vedtakstype${stønadstype.let { " $it" }}"
}

fun VedtakDto.finnStønadsendring(stønad: Stønadsid) =
    stønadsendringListe.find {
        it.kravhaver == stønad.kravhaver &&
            it.skyldner == stønad.skyldner &&
            it.type == stønad.type
    }

fun tilAldersjusteringResultattekst(
    vedtak: VedtakDto,
    stønadsendring: StønadsendringDto,
): String? {
    if (vedtak.type != Vedtakstype.ALDERSJUSTERING) return vedtak.tilBatchHendelseResultattekst()
    if (stønadsendring.beslutning == Beslutningstype.AVVIST) {
        val aldersjusteringDetaljerGrunnlag =
            vedtak.grunnlagListe.hentAldersjusteringDetaljerGrunnlag(stønadsendring.grunnlagReferanseListe)

        if (aldersjusteringDetaljerGrunnlag != null) {
            val person =
                vedtak.grunnlagListe
                    .hentPersonMedReferanseKonvertert(aldersjusteringDetaljerGrunnlag.gjelderBarnReferanse)
                    ?: vedtak.grunnlagListe.hentPersonMedIdentKonvertert(stønadsendring.kravhaver.verdi)

            val stønadstype = if (stønadsendring.type == Stønadstype.FORSKUDD) "Forskuddet" else "Bidraget"
            return if (aldersjusteringDetaljerGrunnlag.innhold.aldersjustertManuelt) {
                val barnInfo = person?.fødselsdato?.tilVisningsnavn()?.let { "født $it" } ?: stønadsendring.kravhaver.verdi
                "$stønadstype til barn $barnInfo ble ikke aldersjustert etter manuell vurdering. ${aldersjusteringDetaljerGrunnlag.innhold.førsteBegrunnelseVisningsnavn}"
            } else if (aldersjusteringDetaljerGrunnlag.innhold.aldersjusteresManuelt) {
                val barnInfo = person?.fødselsdato?.tilVisningsnavn()?.let { "født $it" } ?: stønadsendring.kravhaver.verdi
                "$stønadstype til barn $barnInfo skal aldersjusteres manuelt. ${aldersjusteringDetaljerGrunnlag.innhold.førsteBegrunnelseVisningsnavn}"
            } else {
                val barnInfo = person?.fødselsdato?.tilVisningsnavn()?.let { "født $it" } ?: stønadsendring.kravhaver.verdi
                "$stønadstype til barn $barnInfo ble ikke aldersjustert. ${aldersjusteringDetaljerGrunnlag.innhold.førsteBegrunnelseVisningsnavn}"
            }
        }
    }
    return vedtak.tilBatchHendelseResultattekst()
}

fun VedtakDto.finnVirkningstidspunkt(stønadsendringDto: StønadsendringDto): InnholdMedReferanse<VirkningstidspunktGrunnlag>? =
    grunnlagListe
        .finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<VirkningstidspunktGrunnlag>(
            Grunnlagstype.VIRKNINGSTIDSPUNKT,
            stønadsendringDto.grunnlagReferanseListe,
        ).firstOrNull()

fun VedtakDto.finnAldersjusteringDetaljerGrunnlag(
    stønadsendringDto: StønadsendringDto,
): InnholdMedReferanse<AldersjusteringDetaljerGrunnlag>? =
    grunnlagListe
        .finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<AldersjusteringDetaljerGrunnlag>(
            Grunnlagstype.ALDERSJUSTERING_DETALJER,
            stønadsendringDto.grunnlagReferanseListe,
        ).firstOrNull()

fun StønadsendringDto.finnSistePeriode() = periodeListe.maxByOrNull { it.periode.fom }

fun StønadsendringDto.hentSisteLøpendePeriode() =
    periodeListe
        .maxByOrNull { it.periode.fom }
        ?.takeIf { it.periode.til == null || it.periode.til!!.isAfter(YearMonth.now()) }

fun VedtakDto.finnSluttberegningBarnebidragAldersjusteringIPeriode(periode: VedtakPeriodeDto) =
    grunnlagListe
        .finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<SluttberegningBarnebidragAldersjustering>(
            Grunnlagstype.SLUTTBEREGNING_BARNEBIDRAG_ALDERSJUSTERING,
            periode.grunnlagReferanseListe,
        ).firstOrNull()
        ?.innhold

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

fun VedtakDto.erVedtaksforslag() = vedtakstidspunkt == null

fun List<GrunnlagDto>.finnResultatFraAnnenVedtak(
    grunnlagsreferanseListe: List<Grunnlagsreferanse> = emptyList(),
    finnFørsteTreff: Boolean = false,
): ResultatFraVedtakGrunnlag? =
    if (!finnFørsteTreff && grunnlagsreferanseListe.isEmpty()) {
        null
    } else if (grunnlagsreferanseListe.isEmpty()) {
        filtrerOgKonverterBasertPåEgenReferanse<ResultatFraVedtakGrunnlag>(Grunnlagstype.RESULTAT_FRA_VEDTAK).firstOrNull()?.innhold
    } else {
        finnOgKonverterGrunnlagSomErReferertFraGrunnlagsreferanseListe<ResultatFraVedtakGrunnlag>(
            Grunnlagstype.RESULTAT_FRA_VEDTAK,
            grunnlagsreferanseListe,
        ).firstOrNull()?.innhold
    }

val VedtakDto.referertVedtaksid get() =
    if (erOrkestrertVedtak) {
        stønadsendringListe.firstNotNullOfOrNull { se ->
            se.periodeListe.firstNotNullOfOrNull { p ->
                val resultatFraAnnenVedtak = this.grunnlagListe.finnResultatFraAnnenVedtak(p.grunnlagReferanseListe)
                if (resultatFraAnnenVedtak?.klagevedtak == true) resultatFraAnnenVedtak.vedtaksid else null
            }
        }
    } else if (erDelvedtak) {
        stønadsendringListe.firstNotNullOfOrNull { se ->
            se.periodeListe.firstNotNullOfOrNull { p ->
                val resultatFraAnnenVedtak = this.grunnlagListe.finnResultatFraAnnenVedtak(p.grunnlagReferanseListe)
                resultatFraAnnenVedtak?.vedtaksid
            }
        }
    } else {
        null
    }

val VedtakDto.harResultatFraAnnenVedtak get() = this.grunnlagListe.finnResultatFraAnnenVedtak(finnFørsteTreff = true) != null

val VedtakDto.erDelvedtak get() =
    this.stønadsendringListe.any { se ->
        se.beslutning == Beslutningstype.DELVEDTAK
    }

val VedtakDto.erOrkestrertVedtak get() =
    this.stønadsendringListe.all { se ->
        se.beslutning != Beslutningstype.DELVEDTAK &&
            se.periodeListe.all { p ->
                this.grunnlagListe.finnResultatFraAnnenVedtak(p.grunnlagReferanseListe) != null
            }
    }
